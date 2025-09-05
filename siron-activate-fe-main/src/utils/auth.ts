/* eslint-disable @typescript-eslint/no-non-null-assertion */

import { JWT } from "next-auth/jwt";

export async function refreshAccessToken(token: JWT) {
  // Subsequent logins, but the `access_token` has expired, try to refresh it
  if (!token.refresh_token) throw new TypeError("Missing refresh_token");

  try {
    // The `token_endpoint` can be found in the provider's documentation. Or if they support OIDC,
    // at their `/.well-known/openid-configuration` endpoint.
    const response = await fetch(
      process.env.AUTH_OIDC_ISSUER! + "/protocol/openid-connect/token",
      {
        method: "POST",
        body: new URLSearchParams({
          client_id: process.env.AUTH_OIDC_CLIENT_ID!,
          client_secret: process.env.AUTH_OIDC_CLIENT_SECRET!,
          grant_type: "refresh_token",
          refresh_token: token.refresh_token,
        }),
      },
    );

    const tokensOrError: unknown = await response.json();

    if (!response.ok) throw tokensOrError;

    const newTokens = tokensOrError as JWT;

    token.access_token = newTokens.access_token;
    token.expires_at = Math.floor(
      Date.now() / 1000 + (newTokens.expires_in ?? 0),
    );
    // Some providers only issue refresh tokens once, so preserve if we did not get a new one
    if (newTokens.refresh_token) {
      token.refresh_token = newTokens.refresh_token;
    }

    return token;
  } catch (error) {
    // eslint-disable-next-line no-console
    console.log("Failed to refresh token", error);

    throw new Error("Failed to refresh token");
  }
}

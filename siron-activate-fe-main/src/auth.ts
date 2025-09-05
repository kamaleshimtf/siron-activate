/* eslint-disable @typescript-eslint/no-non-null-assertion */

import NextAuth from "next-auth";

import { refreshAccessToken } from "./utils/auth";
import { parseJwt } from "./utils/parseJwt";

export const { handlers, signIn, signOut, auth } = NextAuth({
  providers: [
    {
      type: "oidc",
      name: process.env.AUTH_OIDC_CLIENT_ID!,
      id: process.env.AUTH_OIDC_CLIENT_ID!,
      issuer: process.env.AUTH_OIDC_ISSUER!,
      clientId: process.env.AUTH_OIDC_CLIENT_ID!,
      clientSecret: process.env.AUTH_OIDC_CLIENT_SECRET!,
    },
  ],
  session: {
    strategy: "jwt",
  },
  callbacks: {
    async jwt({ token, account }) {
      // First-time login, save the `access_token`, its expiry and the `refresh_token`
      if (account?.access_token) {
        const parsedToken = parseJwt(account.access_token);

        // Extract the roles from the token
        // Please note that the `resource_access` key is specific to Keycloak
        const { roles } = (
          parsedToken.resource_access as Record<string, { roles: string[] }>
        )[process.env.AUTH_OIDC_CLIENT_ID!];

        return {
          ...token,
          roles,
          access_token: account.access_token,
          expires_at: account.expires_at,
          refresh_token: account.refresh_token,
        } as typeof token;
      }

      // Subsequent logins, but the `access_token` is still valid
      if (Date.now() < token.expires_at * 1000) {
        return token;
      }

      // Subsequent logins, but the `access_token` has expired, try to refresh it
      return await refreshAccessToken(token);
    },
    session({ session, token }) {
      return {
        ...session,
        roles: token.roles,
        accessToken: token.access_token,
      };
    },
  },
});

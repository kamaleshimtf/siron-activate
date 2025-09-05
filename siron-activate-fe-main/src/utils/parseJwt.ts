import { JWT } from "next-auth/jwt";

/**
 * Parse a JWT token and return the payload.
 * This only works on the server side.
 */
export function parseJwt(token: string) {
  return JSON.parse(
    Buffer.from(token.split(".")[1], "base64").toString(),
  ) as JWT;
}

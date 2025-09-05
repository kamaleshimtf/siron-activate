import { auth } from "@/auth";

export default async function hasRole(roles: string | string[]) {
  const session = await auth();

  if (typeof roles === "string") {
    return session?.roles?.includes(roles) ?? false;
  }

  // In case of multiple roles, check if the user has at least one of them
  return roles.some((role) => session?.roles?.includes(role)) || false;
}

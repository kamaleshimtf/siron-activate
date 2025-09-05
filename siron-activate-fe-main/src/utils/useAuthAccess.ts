import { useSession } from "next-auth/react";
import { useCallback } from "react";

/**
 * Return a function to check if the current user has a specific role(s).
 */
function useAuthAccess() {
  const { data: session } = useSession();

  const hasRole = useCallback(
    (roles: string | string[]) => {
      if (typeof roles === "string") {
        return session?.roles?.includes(roles);
      }

      // In case of multiple roles, check if the user has at least one of them
      return roles.some((role) => session?.roles?.includes(role));
    },
    [session?.roles],
  );

  return { hasRole };
}

export default useAuthAccess;

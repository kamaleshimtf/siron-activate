import { UpdateSession } from "next-auth/react";

let refreshPromise: ReturnType<UpdateSession> | null = null;

const createFetcher = (
  API_URL: string | undefined,
  access_token: string,
  updateSession?: UpdateSession,
): ((url: string, init?: RequestInit) => Promise<unknown>) => {
  return async (url, init) => {
    if (!API_URL) {
      throw new Error("API_URL is not defined");
    }

    const serviceURL = `${API_URL}/licensing/api/${url}`;

    const response = await fetch(serviceURL, {
      ...init,
      headers: {
        ...(init?.headers as Record<string, string>),
        Authorization: `Bearer ${access_token}`,
        "Content-Type": "application/json",
      },
    });

    if (response.status === 404) {
      throw new Error("404 Not Found");
    }

    if (response.status === 401 && updateSession) {
      // Prevent multiple refresh requests
      if (!refreshPromise) {
        refreshPromise = updateSession();

        // Clear the promise after it resolves
        void refreshPromise.then(() => {
          refreshPromise = null;
        });
      }

      // Refresh the session and try again
      const newAccessToken = (await refreshPromise)?.accessToken;

      // Replay the request with the new access token
      if (newAccessToken) {
        return createFetcher(API_URL, newAccessToken)(url, init);
      }
    }

    if (!response.ok) {
      throw new Error(response.statusText || "An error occurred");
    }

    return response.json() as Promise<unknown>;
  };
};

export default createFetcher;

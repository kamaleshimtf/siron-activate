import { auth } from "@/auth";

export async function fetcher<T>(api: string, init?: RequestInit) {
  const session = (await auth()) as { accessToken: string } | null;
  const API_URL = process.env.API_URL;

  const response = await fetch(`${API_URL ?? ""}/licensing/api/${api}`, {
    ...init,
    headers: {
      ...init?.headers,
      Authorization: `Bearer ${session?.accessToken ?? ""}`,
      "Content-Type": "application/json",
    },
  });

  console.log("Api URL : ",API_URL + "/licensing/api/" + api);

  if (response.ok) {
    // If there is a response body, parse it as JSON and return it
    if (response.body) {
      return (await response.json()) as T;
    }

    return undefined;
  }

  if (response.status === 404) throw new Error("404, Not found");
  if (response.status === 500) throw new Error("500, internal server error");

  // For any other server error
  throw new Error(String(response.status), {
    cause: response,
  });
}

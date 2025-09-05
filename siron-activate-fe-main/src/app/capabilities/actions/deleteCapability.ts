"use server";

import { fetcher } from "@/utils/fetcher";

export async function deleteCapability(oid: string) {
  return fetcher(`v1/capabilities/${oid}`, {
    method: "DELETE",
  });
}

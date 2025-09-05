"use server";

import { fetcher } from "@/utils/fetcher";

export async function deleteLicense(oid: string) {
  return fetcher(`v1/licenses/${oid}`, {
    method: "DELETE",
  });
}

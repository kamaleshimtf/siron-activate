"use server";

import { fetcher } from "@/utils/fetcher";

export async function deleteCustomer(oid: string) {
  return fetcher(`v1/customers/${oid}`, {
    method: "DELETE",
  });
}

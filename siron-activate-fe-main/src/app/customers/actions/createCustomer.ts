"use server";

import { Customer } from "@/types/siron-activate";
import { fetcher } from "@/utils/fetcher";

export default async function createCustomer(
  previousState: unknown,
  formData: FormData,
) {
  const customerName = formData.get("customerName") as string;

  // Return error if customer name is empty
  if (!customerName || customerName.trim().length < 3) {
    return {
      error: "Customer name is required and must be at least 3 characters",
    };
  }

  try {
    const customer = await fetcher<Customer>("v1/customers", {
      method: "POST",
      body: JSON.stringify({ name: customerName.trim() }),
    });

    return {
      customer,
    };
  } catch (error) {
    return {
      error: String(error),
    };
  }
}

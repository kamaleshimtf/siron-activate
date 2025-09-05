"use server";

import { Capability } from "@/types/siron-activate";
import { fetcher } from "@/utils/fetcher";

export default async function createCapability(
  previousState: unknown,
  formData: FormData,
) {
  const capabilityName = formData.get("capabilityName") as string;

  // Return error if capability name is empty
  if (!capabilityName || capabilityName.trim().length < 3) {
    return {
      error: "Capability name is required and must be at least 3 characters",
    };
  }

  try {
    const capability = await fetcher<Capability>("v1/capabilities", {
      method: "POST",
      body: JSON.stringify({ name: capabilityName.trim() }),
    });

    return {
      success: true,
      capability,
    };
  } catch (error) {
    return {
      error: String(error),
    };
  }
}

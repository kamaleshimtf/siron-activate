"use server";

import { parse } from "date-fns";

import { License } from "@/types/siron-activate";
import { fetcher } from "@/utils/fetcher";

const isValidEmail = (email: string) =>
  /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

export default async function createLicense(
  prevState: unknown,
  formData: FormData,
) {
  const {
    capabilities: capabilitiesString,
    customerId,
    customerName,
    expirationDate: expirationDateString,
    contactEmail,
  } = Object.fromEntries(formData.entries()) as Record<string, string>;

  const expirationDate = parse(expirationDateString, "dd/MM/yyyy", new Date());
  const capabilities = capabilitiesString ? capabilitiesString.split(",") : [];

  const errors = [];

  // Check that at least one capability is selected
  if (capabilities.length === 0) {
    errors.push("At least one capability must be selected");
  }

  // Check data that date is not in the past
  if (expirationDate < new Date()) {
    errors.push("Expiration date must be in the future");
  }

  // Check that no field is empty
  if (!customerId || !customerName || !expirationDateString || !contactEmail) {
    errors.push("All fields are required");
  }

  // Check the email format (string comma-separated)
  if (!contactEmail.split(",").every(isValidEmail)) {
    errors.push("One or more contact emails are invalid");
  }

  if (errors.length > 0) {
    return {
      errors,
      payload: formData,
    };
  }

  try {
    const license = await fetcher<License>("v1/licenses", {
      method: "POST",
      body: JSON.stringify({
        capabilities,
        customerId,
        customerName,
        expirationDate: expirationDate.toISOString().split("T").at(0),
        contactEmail,
      }),
    });

    return { license };
  } catch (error) {
    return {
      error: String(error),
      payload: formData,
    };
  }
}

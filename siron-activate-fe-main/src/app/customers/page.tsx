import ErrorPage from "@/components/ErrorPage";
import { Customer } from "@/types/siron-activate";
import { fetcher } from "@/utils/fetcher";

import CustomersList from "./components/CustomerList";

export default async function Customers() {
  try {
    const customers = await fetcher<Customer[]>(`v1/customers`);
    return <CustomersList customers={customers ?? []} />;
  } catch (error) {
    return <ErrorPage message={String(error)} />;
  }
}

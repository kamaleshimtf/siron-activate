import ErrorPage from "@/components/ErrorPage";
import { License } from "@/types/siron-activate";
import { fetcher } from "@/utils/fetcher";

import LicensesList from "./components/LicensesList";

export default async function Licenses() {
  try {
    const licenses = await fetcher<License[]>(`v1/licenses`);
    return <LicensesList licenses={licenses ?? []} />;
  } catch (error) {
    return <ErrorPage message={String(error)} />;
  }
}

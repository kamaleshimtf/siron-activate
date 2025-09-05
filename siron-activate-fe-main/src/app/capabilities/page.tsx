import ErrorPage from "@/components/ErrorPage";
import { Capability } from "@/types/siron-activate";
import { fetcher } from "@/utils/fetcher";

import CapabilitiesList from "./components/CapabilitiesList";

export default async function Capabilities() {
  try {
    const capabilities = await fetcher<Capability[]>(`v1/capabilities`);
    return <CapabilitiesList capabilities={capabilities ?? []} />;
  } catch (error) {
    return <ErrorPage title="Failed to render page" message={String(error)} />;
  }
}

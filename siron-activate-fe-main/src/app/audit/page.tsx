import ErrorPage from "@/components/ErrorPage";
import { AuditLog } from "@/types/siron-activate";
import { fetcher } from "@/utils/fetcher";
import hasRole from "@/utils/hasRole";

import AuditList from "./components/AuditList";

export default async function Customers() {
  if (await hasRole("admin")) {
    try {
      const data = await fetcher<AuditLog[]>(`v1/audit`);
      return <AuditList data={data ?? []} />;
    } catch (error) {
      return <ErrorPage message={String(error)} />;
    }
  }

  return (
    <ErrorPage message="You do not have the required permissions to access this page." />
  );
}

"use client";

import { LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import { enGB } from "date-fns/locale";
import { useSession } from "next-auth/react";
import { ReactNode } from "react";
import { SWRConfig } from "swr";

import createFetcher from "@/utils/createFetcher";

type Props = {
  apiUrl: string;
  children: ReactNode;
};

export default function ClientProviders({ children, apiUrl }: Props) {
  const { data: session, update } = useSession();

  const accessToken = session?.accessToken;

  return (
    <SWRConfig
      value={{
        fetcher: createFetcher(apiUrl, accessToken ?? "", update),
      }}
    >
      <LocalizationProvider dateAdapter={AdapterDateFns} adapterLocale={enGB}>
        {children}
      </LocalizationProvider>
    </SWRConfig>
  );
}

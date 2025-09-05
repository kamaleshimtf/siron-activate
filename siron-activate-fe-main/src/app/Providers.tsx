import { IconProvider } from "@imtf/icons";
import { PanacheProvider } from "@imtf/panache";
import { ThemeProvider } from "@mui/material";
import { AppRouterCacheProvider } from "@mui/material-nextjs/v15-appRouter";
import Link from "next/link";
import { SessionProvider } from "next-auth/react";
import { ReactNode } from "react";

import { AuthGuard } from "@/features/auth/components/AuthGuard";

import { auth } from "@/auth";

import { theme } from "../theme";

type Props = {
  children: ReactNode;
};

async function Providers({ children }: Props) {
  const session = await auth();

  return (
    <AppRouterCacheProvider options={{ enableCssLayer: true }}>
      <ThemeProvider theme={theme}>
        <PanacheProvider locale="en" LinkComponent={Link}>
          <IconProvider>
            <AuthGuard>
              <SessionProvider session={session}>{children}</SessionProvider>
            </AuthGuard>
          </IconProvider>
        </PanacheProvider>
      </ThemeProvider>
    </AppRouterCacheProvider>
  );
}

export default Providers;

// In order to have the css layers set up correctly, we need to import the global styles
// before any other stylesheets or js files that might declare layered css.
import "@/app/globals.css";

import { AppLayout, BoxLayout } from "@imtf/panache";
import { CssBaseline } from "@mui/material";
import type { Metadata } from "next";

import AppBar from "@/components/AppBar";
import { Navigation } from "@/components/Navigation";

import ClientProviders from "./ClientProviders";
import Providers from "./Providers";

export const metadata: Metadata = {
  title: "Siron®One :: License Manager",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body>
        <Providers>
          <CssBaseline />
          <ClientProviders
            // eslint-disable-next-line @typescript-eslint/prefer-nullish-coalescing
            apiUrl={process.env.API_URL_PUBLIC || process.env.API_URL || ""}
          >
            <AppLayout
              appBar={<AppBar />}
              navigation={<Navigation />}
              sidebarDefaultWidth={300}
            >
              <BoxLayout>{children}</BoxLayout>
            </AppLayout>
          </ClientProviders>
        </Providers>
      </body>
    </html>
  );
}

"use client";

import { SectionedNavigation, usePanacheContext } from "@imtf/panache";
import { usePathname } from "next/navigation";

import useAuthAccess from "@/utils/useAuthAccess";

const sections = [
  {
    label: "Licenses",
    href: "/licenses",
  },
  {
    label: "Capabilities",
    href: "/capabilities",
  },
  {
    label: "Customers",
    href: "/customers",
  },
];

export function Navigation() {
  const pathname = usePathname();
  const { hasRole } = useAuthAccess();
  const { toggleAppDrawer } = usePanacheContext();

  const finalSections = hasRole("admin")
    ? [
        ...sections,
        {
          label: "Audit Log",
          href: "/audit",
        },
      ]
    : sections;

  return (
    <SectionedNavigation<{
      label: string;
      href: string;
    }>
      renderItemLabel={(item) => item.label}
      getItemKey={(item) => item.label}
      getItemProps={(item) => ({ href: item.href })}
      isItemActive={(item) => {
        // News posts are under the news section, but the news section itself
        // should be considered active on the index page.
        if (item.href === "/") {
          return pathname === "/" || pathname.startsWith("/news");
        }

        return pathname.startsWith(item.href);
      }}
      onItemClick={() => {
        toggleAppDrawer();
      }}
      withIndent
      sections={finalSections}
    />
  );
}

"use client";

import { NavigationPane, TwoPaneLayout } from "@imtf/panache";
import { Add as AddIcon } from "@mui/icons-material";
import { useRouter } from "next/navigation";
import { useCallback, useMemo, useState } from "react";

import { NothingSelected } from "@/components/NothingSelected";
import { License } from "@/types/siron-activate";
import useAuthAccess from "@/utils/useAuthAccess";

import { deleteLicense } from "../actions/deleteLicense";

import CreateDialog from "./CreateDialog";
import LicenseDetails from "./LicenceDetails";

type Props = {
  licenses: License[];
};

export default function LicensesList({ licenses }: Props) {
  const [selectedOid, setSelectedOid] = useState<string>();
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false);
  const router = useRouter();
  const { hasRole } = useAuthAccess();

  const [activeTab, setActiveTab] = useState<"active" | "expired">("active");

  const selectedItem = useMemo(
    () => licenses.find((app) => app.oid === selectedOid),
    [licenses, selectedOid],
  );

  // Get all email addresses from the licenses
  const allEmails = useMemo(() => {
    return licenses
      .flatMap((license) => license.contactEmail.split(","))
      .map((email) => email.trim())
      .filter((email, index, self) => self.indexOf(email) === index);
  }, [licenses]);

  const handleCloseDialog = useCallback(
    (license?: License) => {
      if (license) {
        setSelectedOid(license.oid);
      }

      setIsCreateDialogOpen(false);
      router.refresh();
    },
    [router],
  );

  const handleDelete = useCallback(
    async (licenseOid: string) => {
      await deleteLicense(licenseOid);

      setSelectedOid(undefined);
      router.refresh();
    },
    [router],
  );

  const filteredLicenses = useMemo(() => {
    return licenses.filter((license) => {
      if (activeTab === "active") {
        return new Date(license.expirationDate) > new Date();
      }
      return new Date(license.expirationDate) <= new Date();
    });
  }, [licenses, activeTab]);

  const renderContent = () => {
    if (selectedItem) {
      // Set a key to clear component state when the selected item changes
      return (
        <LicenseDetails
          onClose={() => {
            setSelectedOid(undefined);
          }}
          deleteLicense={handleDelete}
          key={selectedItem.oid}
          license={selectedItem}
        />
      );
    }

    return (
      <NothingSelected description="Select a license to view its details" />
    );
  };

  return (
    <>
      <TwoPaneLayout
        prioritizeSidebar={selectedItem === undefined}
        sidebarDefaultWidth={420}
        sidebar={
          <NavigationPane
            title="Licenses"
            entities={filteredLicenses}
            actions={
              hasRole(["license-issuer", "admin"])
                ? [
                    {
                      key: "create",
                      icon: AddIcon,
                      label: "Create",
                      onClick: () => {
                        setIsCreateDialogOpen(true);
                      },
                    },
                  ]
                : undefined
            }
            getItemKey={(item) => item.oid}
            renderItemLabel={(item) => item.customerName}
            renderSectionLabel={(section) => section.key}
            withIndent
            tabs={[
              {
                key: "active",
                label: "Active",
              },
              {
                key: "expired",
                label: "Expired",
              },
            ]}
            activeTab={activeTab}
            onTabChange={setActiveTab}
            groupingPredicates={[
              {
                groupingPredicate: "customerId",
                label: "customer",
                value: "customerId",
              },
              {
                groupingPredicate: "contactEmail",
                label: "contact emails",
                value: "contactEmail",
              },
              {
                label: "capabilities",
                value: "capabilities",
                groupingPredicate: (item) =>
                  item.capabilities.sort().join(", "),
              },
              {
                label: "ungrouped",
                value: "ungrouped",
                groupingPredicate: undefined,
              },
            ]}
            sortingMethods={[
              {
                label: "alphabetically",
                value: "customerName",
                sortFn: (a, b) => a.customerName.localeCompare(b.customerName),
              },
              {
                label: "by expiration date",
                value: "expirationDate",
                sortFn: (a, b) =>
                  new Date(a.expirationDate).getTime() -
                  new Date(b.expirationDate).getTime(),
              },
            ]}
            searchKeys={[
              "productName",
              "customerId",
              "customerName",
              "creationDate",
              "expirationDate",
              "contactEmail",
              "capabilities",
            ]}
            isItemActive={(item) => item.oid === selectedOid}
            onItemClick={(e, item) => {
              setSelectedOid(selectedOid === item.oid ? undefined : item.oid);
            }}
          />
        }
      >
        {renderContent()}
      </TwoPaneLayout>
      <CreateDialog
        emails={allEmails}
        isOpen={isCreateDialogOpen}
        close={handleCloseDialog}
      />
    </>
  );
}

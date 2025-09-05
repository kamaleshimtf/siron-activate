"use client";

import { NavigationPane, TwoPaneLayout } from "@imtf/panache";
import { Add as AddIcon } from "@mui/icons-material";
import { useRouter } from "next/navigation";
import { useCallback, useMemo, useState } from "react";

import { NothingSelected } from "@/components/NothingSelected";
import { Customer } from "@/types/siron-activate";
import useAuthAccess from "@/utils/useAuthAccess";

import { deleteCustomer } from "../actions/deleteCustomer";

import { CreateDialog } from "./CreateDialog";
import CustomerDetails from "./CustomerDetails";

type Props = {
  customers: Customer[];
};

export default function CustomersList({ customers }: Props) {
  const [selectedOid, setSelectedOid] = useState<string>();
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false);
  const router = useRouter();

  const { hasRole } = useAuthAccess();

  const selectedItem = useMemo(
    () => customers.find((app) => app.oid === selectedOid),
    [customers, selectedOid],
  );

  const handleCloseDialog = useCallback(
    (customer?: Customer) => {
      if (customer) {
        setSelectedOid(customer.oid);
      }

      setIsCreateDialogOpen(false);

      router.refresh();
    },
    [router],
  );

  const handleDelete = useCallback(
    async (customerOid: string) => {
      await deleteCustomer(customerOid);

      setSelectedOid(undefined);
      router.refresh();
    },
    [router],
  );

  const renderContent = () => {
    if (selectedItem) {
      return (
        <CustomerDetails
          onClose={() => {
            setSelectedOid(undefined);
          }}
          customer={selectedItem}
          deleteCustomer={handleDelete}
        />
      );
    }

    return (
      <NothingSelected description="Select a customer to view its details" />
    );
  };

  return (
    <>
      <TwoPaneLayout
        prioritizeSidebar={selectedItem === undefined}
        sidebar={
          <NavigationPane
            title="Customers"
            entities={customers}
            actions={
              hasRole(["license-issuer", "admin"])
                ? [
                    {
                      key: "create",
                      icon: AddIcon,
                      label: "Add",
                      onClick: () => {
                        setIsCreateDialogOpen(true);
                      },
                    },
                  ]
                : undefined
            }
            getItemKey={(item) => item.oid}
            renderItemLabel={(item) => item.name}
            searchKeys={["name"]}
            isItemActive={(item) => item.oid === selectedOid}
            onItemClick={(e, item) => {
              setSelectedOid(selectedOid === item.oid ? undefined : item.oid);
            }}
          />
        }
      >
        {renderContent()}
      </TwoPaneLayout>
      <CreateDialog isOpen={isCreateDialogOpen} close={handleCloseDialog} />
    </>
  );
}

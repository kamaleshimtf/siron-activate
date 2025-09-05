"use client";

import { NavigationPane, TwoPaneLayout } from "@imtf/panache";
import { Add as AddIcon } from "@mui/icons-material";
import { useRouter } from "next/navigation";
import { useCallback, useMemo, useState } from "react";

import { NothingSelected } from "@/components/NothingSelected";
import { Capability } from "@/types/siron-activate";
import useAuthAccess from "@/utils/useAuthAccess";

import { deleteCapability } from "../actions/deleteCapability";

import CapabilityDetails from "./CapabilityDetails";
import { CreateDialog } from "./CreateDialog";

type Props = {
  capabilities: Capability[];
};

export default function CapabilitiesList({ capabilities }: Props) {
  const [selectedOid, setSelectedOid] = useState<string>();
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false);
  const router = useRouter();
  const { hasRole } = useAuthAccess();

  const selectedItem = useMemo(
    () => capabilities.find((item) => item.oid === selectedOid),
    [capabilities, selectedOid],
  );

  const handleCloseDialog = useCallback(
    (capability?: Capability) => {
      if (capability) {
        setSelectedOid(capability.oid);
      }

      setIsCreateDialogOpen(false);

      router.refresh();
    },
    [router],
  );

  const handleDelete = useCallback(
    async (capabilityOid: string) => {
      await deleteCapability(capabilityOid);

      setSelectedOid(undefined);
      router.refresh();
    },
    [router],
  );

  const renderContent = () => {
    if (selectedItem) {
      return (
        <CapabilityDetails
          capability={selectedItem}
          onClose={() => {
            setSelectedOid(undefined);
          }}
          deleteCapability={handleDelete}
        />
      );
    }

    return (
      <NothingSelected description="Select a capability to view its details" />
    );
  };

  return (
    <>
      <TwoPaneLayout
        prioritizeSidebar={selectedItem === undefined}
        sidebar={
          <NavigationPane
            title="Capabilities"
            entities={capabilities}
            actions={
              hasRole("admin")
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

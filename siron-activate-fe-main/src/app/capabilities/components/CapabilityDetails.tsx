import { Listmaker } from "@imtf/panache";
import { Button, Typography } from "@mui/material";
import { useState } from "react";

import ConfirmDialog from "@/components/ConfirmDialog";
import DangerZone from "@/components/DangerZone";
import { DetailsView } from "@/components/DetailsView";
import { Capability } from "@/types/siron-activate";
import useAuthAccess from "@/utils/useAuthAccess";

type Props = {
  capability: Capability;
  deleteCapability: (oid: string) => void | Promise<void>;
  onClose: () => void;
};

export default function CapabilityDetails({
  capability,
  deleteCapability,
  onClose,
}: Props) {
  const { hasRole } = useAuthAccess();

  const [isConfirmDialogOpen, setIsConfirmDialogOpen] = useState(false);

  return (
    <DetailsView
      onClose={onClose}
      header={
        <Typography variant="h6" my={3}>
          Cabability details
        </Typography>
      }
    >
      <div>
        <Listmaker
          data={capability}
          config={{
            properties: [
              { key: "name", label: "Name" },
              { key: "oid", label: "OID" },
            ],
          }}
        />
        {hasRole(["admin"]) && (
          <DangerZone>
            <Button
              color="error"
              variant="contained"
              onClick={() => {
                setIsConfirmDialogOpen(true);
              }}
            >
              Delete...
            </Button>
            <ConfirmDialog
              color="error"
              isOpen={isConfirmDialogOpen}
              title="Delete capability"
              message="Are you sure you want to delete this capability?"
              onConfirm={() => {
                void deleteCapability(capability.oid);
              }}
              onClose={() => {
                setIsConfirmDialogOpen(false);
              }}
            />
          </DangerZone>
        )}
      </div>
    </DetailsView>
  );
}

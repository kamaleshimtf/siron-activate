import { Listmaker } from "@imtf/panache";
import { Button, Typography } from "@mui/material";
import { useState } from "react";

import ConfirmDialog from "@/components/ConfirmDialog";
import DangerZone from "@/components/DangerZone";
import { DetailsView } from "@/components/DetailsView";
import { Customer } from "@/types/siron-activate";
import useAuthAccess from "@/utils/useAuthAccess";

type Props = {
  customer: Customer;
  deleteCustomer: (oid: string) => void | Promise<void>;
  onClose: () => void;
};

export default function CustomerDetails({
  customer,
  deleteCustomer,
  onClose,
}: Props) {
  const { hasRole } = useAuthAccess();

  const [isConfirmDialogOpen, setIsConfirmDialogOpen] = useState(false);

  return (
    <DetailsView
      onClose={onClose}
      header={
        <Typography variant="h6" my={3}>
          Customer Details
        </Typography>
      }
    >
      <div>
        <Listmaker
          data={customer}
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
              title="Delete Customer"
              message="Are you sure you want to delete this customer?"
              onConfirm={() => {
                void deleteCustomer(customer.oid);
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

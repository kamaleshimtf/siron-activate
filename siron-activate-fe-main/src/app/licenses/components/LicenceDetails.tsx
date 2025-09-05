import { Listmaker } from "@imtf/panache";
import { GppGood } from "@mui/icons-material";
import {
  Alert,
  Button,
  Chip,
  Snackbar,
  Stack,
  Typography,
} from "@mui/material";
import { useState } from "react";

import ConfirmDialog from "@/components/ConfirmDialog";
import DangerZone from "@/components/DangerZone";
import { DetailsView } from "@/components/DetailsView";
import { License } from "@/types/siron-activate";
import useAuthAccess from "@/utils/useAuthAccess";

import { SignLicenseDialog } from "./SignLicenseDialog";

type Props = {
  license: License;
  deleteLicense: (oid: string) => void | Promise<void>;
  onClose: () => void;
};

export default function LicenseDetails({
  license,
  deleteLicense,
  onClose,
}: Props) {
  const { hasRole } = useAuthAccess();

  const [isConfirmDeleteDialogOpen, setIsConfirmDeleteDialogOpen] =
    useState(false);
  const [isSignLicenseDialogOpen, setIsSignLicenseDialogOpen] = useState(false);

  const [snackbarMessage, setSnackbarMessage] = useState<string>();

  // Reassign productName to undefined if null to hide it
  if (license.productName === null) {
    Object.assign(license, {
      productName: undefined,
    });
  }

  return (
    <DetailsView
      onClose={onClose}
      header={
        <Stack
          direction="row"
          spacing={2}
          justifyContent="space-between"
          my={3}
        >
          <Typography variant="h6">License Details</Typography>
          <Button
            disabled={!hasRole(["license-user", "license-issuer", "admin"])}
            startIcon={<GppGood />}
            variant="contained"
            color="primary"
            onClick={() => {
              setIsSignLicenseDialogOpen(true);
            }}
          >
            License key...
          </Button>
        </Stack>
      }
    >
      <div>
        <Listmaker
          data={license}
          config={{
            properties: [
              { key: "oid", label: "OID" },
              { key: "productName", label: "Product", optional: true },
              { key: "customerId", label: "Customer" },
              { key: "capabilities", label: "Capabilities" },
              { key: "customerName", label: "Licence information" },
              { key: "creationDate", label: "Creation Date" },
              { key: "expirationDate", label: "Expiration Date" },
              { key: "keyVersionId", label: "Key version Id" },
              {
                key: "contactEmail",
                label: "Contact email",
                renderer: "emailChips",
              },
            ],
          }}
          renderers={{
            emailChips: ({ value }) => {
              return (value as string)
                .split(",")
                .map((email) => (
                  <Chip size="small" sx={{ mr: 1 }} key={email} label={email} />
                ));
            },
          }}
        />

        {hasRole("admin") ? (
          <DangerZone>
            <Button
              color="error"
              variant="contained"
              onClick={() => {
                setIsConfirmDeleteDialogOpen(true);
              }}
            >
              Delete...
            </Button>
            <ConfirmDialog
              color="error"
              isOpen={isConfirmDeleteDialogOpen}
              title="Delete License"
              message="Are you sure you want to delete this license?"
              onConfirm={() => {
                void deleteLicense(license.oid);
              }}
              onClose={() => {
                setIsConfirmDeleteDialogOpen(false);
              }}
            />
          </DangerZone>
        ) : null}
      </div>
      <Snackbar
        open={!!snackbarMessage}
        anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
        onClose={() => {
          setSnackbarMessage(undefined);
        }}
        autoHideDuration={3000}
      >
        <Alert
          severity="success"
          variant="filled"
          onClose={() => {
            setSnackbarMessage(undefined);
          }}
        >
          {snackbarMessage}
        </Alert>
      </Snackbar>
      <SignLicenseDialog
        license={license}
        open={isSignLicenseDialogOpen}
        setSnackbarMessage={setSnackbarMessage}
        onClose={() => {
          setIsSignLicenseDialogOpen(false);
        }}
      />
    </DetailsView>
  );
}

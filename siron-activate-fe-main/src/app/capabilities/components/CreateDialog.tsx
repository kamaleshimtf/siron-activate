import {
  Alert,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
} from "@mui/material";
import { useActionState, useCallback, useEffect } from "react";

import createCapability from "@/app/capabilities/actions/createCapability";
import { Capability } from "@/types/siron-activate";

type Props = {
  isOpen: boolean;
  close: (capability?: Capability) => void;
};

const initialState: {
  success?: boolean;
  error?: string;
  capability?: Capability;
} = {};

export function CreateDialog({ isOpen, close }: Props) {
  const [formState, formAction] = useActionState(
    createCapability,
    initialState,
  );

  const resetForm = useCallback(() => {
    delete formState.capability;
    delete formState.success;
    delete formState.error;
  }, [formState]);

  useEffect(() => {
    if (formState.success) {
      resetForm();
      close(formState.capability);
    }
  }, [formState.success, close, formState, resetForm]);

  return (
    <Dialog
      open={isOpen}
      onClose={() => {
        resetForm();
        close();
      }}
      fullWidth
      maxWidth="sm"
    >
      <DialogTitle>Create new capability</DialogTitle>
      <form autoComplete="off" action={formAction}>
        <DialogContent>
          {formState.error ? (
            <Alert severity="error" sx={{ mb: 4 }}>
              {formState.error}
            </Alert>
          ) : null}
          <TextField fullWidth label="capability name" name="capabilityName" />
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() => {
              resetForm();
              close();
            }}
          >
            Cancel
          </Button>
          <Button type="submit">Save</Button>
        </DialogActions>
      </form>
    </Dialog>
  );
}

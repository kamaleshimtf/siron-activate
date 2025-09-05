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

import { Customer } from "@/types/siron-activate";

import createCustomer from "../actions/createCustomer";

type Props = {
  isOpen: boolean;
  close: (customer?: Customer) => void;
};

const initialState: {
  error?: string;
  customer?: Customer;
} = {};

export function CreateDialog({ isOpen, close }: Props) {
  const [formState, formAction] = useActionState(createCustomer, initialState);

  const resetForm = useCallback(() => {
    delete formState.customer;
    delete formState.error;
  }, [formState]);

  useEffect(() => {
    if (formState.customer) {
      resetForm();
      close(formState.customer);
    }
  }, [formState.customer, close, formState, resetForm]);

  return (
    <Dialog
      open={isOpen}
      onClose={() => {
        close();
      }}
      fullWidth
      maxWidth="sm"
    >
      <DialogTitle>Create new customer</DialogTitle>
      <form autoComplete="off" action={formAction}>
        <DialogContent>
          {formState.error ? (
            <Alert severity="error" sx={{ mb: 4 }}>
              {formState.error}
            </Alert>
          ) : null}
          <TextField fullWidth label="Customer name" name="customerName" />
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

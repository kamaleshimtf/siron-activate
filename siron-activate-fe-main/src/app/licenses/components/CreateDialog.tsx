"use client";

import {
  Alert,
  Autocomplete,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
} from "@mui/material";
import { DatePicker } from "@mui/x-date-pickers";
import { addDays } from "date-fns";
import { memo, useActionState, useCallback, useEffect, useRef } from "react";
import useSWR from "swr";

import { EmailsCreatable } from "@/app/licenses/components/EmailsCreatable";
import { Capability, Customer, License } from "@/types/siron-activate";

import createLicense from "../actions/createLicense";

type Props = {
  isOpen: boolean;
  close: (license?: License) => void;
  emails: string[];
};

const initialState: {
  errors?: string[];
  license?: License;
  payload?: FormData;
} = {};

function CreateDialog({ isOpen, close, emails }: Props) {
  const [formState, formAction] = useActionState(createLicense, initialState);

  const {
    data: capabilities,
    isLoading: isCapabilitiesLoading,
    error: capabilitiesError,
  } = useSWR<Capability[], TypeError>(isOpen ? "v1/capabilities" : null);

  const {
    data: customers,
    isLoading: isCustomersLoading,
    error: customersError,
  } = useSWR<Customer[], TypeError>(isOpen ? "v1/customers" : null);

  // Reset state
  const resetState = useCallback(() => {
    delete formState.license;
    delete formState.errors;
    delete formState.payload;
  }, [formState]);

  // Close dialog & clear the form state when the dialog is closed
  useEffect(() => {
    if (formState.license) {
      close(formState.license);
      resetState();
    }
  }, [formState.license, close, formState, resetState]);

  const capabilitiesInputRef = useRef<HTMLInputElement>(null);

  return (
    <Dialog
      open={isOpen}
      onClose={() => {
        resetState();
        close();
      }}
      fullWidth
      maxWidth="sm"
    >
      <DialogTitle>Create new license</DialogTitle>
      <form autoComplete="off" noValidate action={formAction}>
        <DialogContent>
          {formState.errors ? (
            <Alert severity="error" sx={{ mb: 4 }}>
              {formState.errors.map((error) => (
                <div key={error}>{error}</div>
              ))}
            </Alert>
          ) : null}
          <Autocomplete
            fullWidth
            sx={{ mb: 2 }}
            multiple
            disableCloseOnSelect
            options={capabilities ?? []}
            loading={isCapabilitiesLoading}
            getOptionLabel={(option) => option.name}
            getOptionKey={(option) => option.oid}
            onChange={(_, value) => {
              // capabilities value is manually set as a string, as we
              // can't pass an array to the form data with autocomplete
              if (!capabilitiesInputRef.current) return;

              capabilitiesInputRef.current.value = value
                .map((v) => v.name)
                .join(",");
            }}
            renderInput={(params) => (
              <>
                <input
                  type="hidden"
                  name="capabilities"
                  ref={capabilitiesInputRef}
                />
                <TextField
                  {...params}
                  error={!!capabilitiesError}
                  helperText={capabilitiesError?.message}
                  required
                  label="Capabilities"
                />
              </>
            )}
          ></Autocomplete>

          <Autocomplete
            fullWidth
            sx={{ mb: 2 }}
            options={customers ?? []}
            loading={isCustomersLoading}
            getOptionLabel={(option) => option.name}
            getOptionKey={(option) => option.oid}
            renderInput={(params) => (
              <TextField
                {...params}
                error={!!customersError}
                helperText={customersError?.message}
                required
                name="customerId"
                label="Customer"
              />
            )}
          />
          <TextField
            required
            sx={{ mb: 2 }}
            fullWidth
            label="Licence information (displayed in application)"
            name="customerName"
            defaultValue={formState.payload?.get("customerName")}
          />
          <DatePicker
            sx={{ mb: 2 }}
            slotProps={{
              textField: {
                required: true,
                fullWidth: true,
                defaultValue: formState.payload?.get("expirationDate"),
              },
            }}
            minDate={addDays(new Date(), 1)}
            label="Expiration date"
            name="expirationDate"
          />
          <EmailsCreatable
            label="Contact emails"
            name="contactEmail"
            emails={emails}
            defaultValue={formState.payload?.get("contactEmail") as string}
          />
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() => {
              close();
              resetState();
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

export default memo(CreateDialog);

"use client";

import {
  Autocomplete,
  AutocompleteProps,
  ListItemButton,
  ListItemButtonProps,
  TextField,
  createFilterOptions,
} from "@mui/material";
import { useMemo, useRef, useState } from "react";

const filter = createFilterOptions<EmailOption>();

type EmailOption = {
  email: string;
  valid: boolean;
  inputValue?: string;
};

type EmailsCreatableProps = {
  label: string;
  name: string;
  defaultValue?: string;
  value?: string;
  onChange?: (value: string) => void;
  emails?: string[];
} & Omit<
  AutocompleteProps<EmailOption, true, false, true>,
  "value" | "defaultValue" | "onChange" | "renderInput" | "options"
>;

function isValidEmail(email: string): boolean {
  return /\S+@\S+\.\S+/.test(email);
}

export function EmailsCreatable({
  name,
  defaultValue,
  value: valueProp,
  label,
  onChange,
  emails,
  ...autocompleteProps
}: EmailsCreatableProps) {
  const [valueState, setValueState] = useState<string>(
    defaultValue ?? valueProp ?? "",
  );

  // Keep the value in sync with the prop value
  const prevValueRef = useRef(valueProp);
  if (valueProp !== prevValueRef.current) {
    prevValueRef.current = valueProp;
    setValueState(valueProp ?? "");
  }

  // Generate options from the value string
  const value: EmailOption[] = useMemo(
    () =>
      valueState
        ? valueState.split(",").map((email) => ({
            email,
            valid: isValidEmail(email),
          }))
        : [],
    [valueState],
  );

  const options: EmailOption[] = useMemo(() => {
    if (!emails) return [];
    return emails.map((email) => ({
      email,
      valid: isValidEmail(email),
    }));
  }, [emails]);

  return (
    <Autocomplete
      {...autocompleteProps}
      value={value}
      options={options}
      multiple
      selectOnFocus
      clearOnBlur
      disableCloseOnSelect
      forcePopupIcon
      autoHighlight
      freeSolo
      renderOption={({ key, ...props }, option) => (
        <ListItemButton
          key={key as string}
          {...(props as ListItemButtonProps)}
          selected={value.map((o) => o.email).includes(option.email)}
        >
          {option.email}
        </ListItemButton>
      )}
      onChange={(_, newValue, reason) => {
        // When the user create a new option, ensure it's a valid non already existing email
        if (reason === "createOption") {
          const newEmail = newValue.at(-1) as string;

          if (
            !isValidEmail(newEmail) ||
            value.some((o) => o.email === newEmail)
          ) {
            // If the new email is not valid and already exists, do not update
            return valueState;
          }
        }

        const nextValue = newValue
          .map((option) =>
            typeof option === "string"
              ? option
              : (option.inputValue ?? option.email).trim(),
          )
          .join(",");

        setValueState(nextValue);
        onChange?.(nextValue);
      }}
      filterOptions={(_options, params) => {
        const filtered = filter(_options, params);
        const { inputValue } = params;

        // Suggest the creation of a new value if it doesn't already exist and it's a valid email
        if (inputValue) {
          filtered.push({
            email: `add "${inputValue}"`,
            valid:
              isValidEmail(inputValue) &&
              !value.some((o) => o.email === inputValue) &&
              !options.some((o) => o.email === inputValue),
            inputValue,
          });
        }

        return filtered;
      }}
      getOptionKey={(option) =>
        typeof option === "string" ? option : option.email
      }
      getOptionLabel={(option) =>
        typeof option === "string" ? option : option.email
      }
      getOptionDisabled={(option) => !option.valid}
      renderInput={(params) => (
        <>
          <input type="hidden" name={name} value={valueState} />
          <TextField {...params} label={label} />
        </>
      )}
    />
  );
}

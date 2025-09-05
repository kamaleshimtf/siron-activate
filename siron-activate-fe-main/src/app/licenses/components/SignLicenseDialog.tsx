import { ContentCopy, Download, GppGood } from "@mui/icons-material";
import {
  Alert,
  Button,
  ButtonGroup,
  Dialog,
  DialogContent,
  DialogProps,
  DialogTitle,
  Stack,
  TextField,
  useMediaQuery,
} from "@mui/material";
import { kebabCase } from "lodash-es";
import { useRouter } from "next/navigation";
import { BareFetcher, useSWRConfig } from "swr";
import useSWRMutation from "swr/mutation";

import { License } from "@/types/siron-activate";
import useAuthAccess from "@/utils/useAuthAccess";

const signLicense = (fetcher: BareFetcher) => async (url: string) => {
  const { value } = (await fetcher(url, {
    method: "POST",
  })) as { value: string };

  return value;
};

export function SignLicenseDialog({
  license,
  setSnackbarMessage,
  ...props
}: DialogProps & {
  license: License;
  setSnackbarMessage: (message: string) => void;
}) {
  const { hasRole } = useAuthAccess();
  const router = useRouter();
  const { fetcher } = useSWRConfig() as { fetcher: BareFetcher };
  const sm = useMediaQuery("(width < 600px)");

  const {
    trigger: signLicence,
    data: licenceString,

    error,
  } = useSWRMutation<string, Error>(
    `v1/licenses/${license.oid}/sign`,
    signLicense(fetcher),
    {
      throwOnError: false,
      onSuccess: () => {
        router.refresh();
      },
    },
  );

  const handleCopy = () => {
    navigator.clipboard.writeText(licenceString ?? "").then(
      () => {
        setSnackbarMessage("Copied to clipboard");
      },
      () => {
        setSnackbarMessage("Failed to copy to clipboard");
      },
    );
  };

  const licenseFile = licenceString
    ? new Blob([licenceString], {
        type: "text/plain",
      })
    : undefined;

  return (
    <Dialog {...props}>
      <DialogTitle>License key</DialogTitle>
      <DialogContent>
        <Alert sx={{ mt: 2 }} severity="info">
          Once you have generated the license key, please get in touch with the
          software operations team in order to deploy the license key.
        </Alert>
        {error?.message ? (
          <Alert sx={{ mt: 2 }} severity="error">
            {error.message}
          </Alert>
        ) : null}

        <TextField
          sx={{ my: 3 }}
          label="License string"
          fullWidth
          multiline
          minRows={5}
          disabled
          value={licenceString ?? ""}
        ></TextField>
        <Stack
          justifyContent="space-between"
          direction={sm ? "column" : "row"}
          spacing={1}
        >
          <Button
            disabled={!hasRole(["license-user", "license-issuer", "admin"])}
            startIcon={<GppGood />}
            variant="contained"
            color="primary"
            onClick={() => {
              void signLicence();
            }}
          >
            Generate license key
          </Button>
          <ButtonGroup disabled={!licenceString} fullWidth={sm}>
            <Button startIcon={<ContentCopy />} onClick={handleCopy}>
              Copy
            </Button>
            <Button startIcon={<Download />}>
              <a
                download={kebabCase(`${license.customerId}.lic`)}
                target="_blank"
                rel="noreferrer"
                href={licenseFile ? URL.createObjectURL(licenseFile) : ""}
                style={{
                  textDecoration: "inherit",
                  color: "inherit",
                }}
              >
                Download
              </a>
            </Button>
          </ButtonGroup>
        </Stack>
      </DialogContent>
    </Dialog>
  );
}

import { Alert, AlertTitle } from "@mui/material";

type Props = {
  title?: string;
  message?: string;
};

export default function ErrorPage({
  title,
  message = "An error occured",
}: Props) {
  return (
    <Alert
      sx={{
        mx: 4,
        my: 2,
      }}
      severity="error"
    >
      {title ? <AlertTitle>{title}</AlertTitle> : null}
      {message}
    </Alert>
  );
}

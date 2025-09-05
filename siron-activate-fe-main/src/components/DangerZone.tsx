import { Divider, Typography } from "@mui/material";
import { ReactNode } from "react";

type Props = {
  children: ReactNode;
};

export default function DangerZone({ children }: Props) {
  return (
    <>
      <Divider sx={{ my: 3 }} />
      <Typography mb={2}>Danger Zone</Typography>
      {children}
    </>
  );
}

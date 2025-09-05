import { Paper, Typography } from "@mui/material";

export default function Home() {
  return (
    <Paper sx={{ p: 2 }}>
      <Typography variant="h4">
        Siron<sup>®</sup>One – Activate
      </Typography>
      <Typography variant="body1">
        Manage licenses for your IMTF Siron<sup>®</sup>One modules.
      </Typography>
      <Typography variant="body1">
        Please use the navigation links to access the different sections of the
        application.
      </Typography>
    </Paper>
  );
}

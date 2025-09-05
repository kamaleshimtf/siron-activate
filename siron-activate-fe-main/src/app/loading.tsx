import { Paper, Skeleton } from "@mui/material";

export default function Loading() {
  // You can add any UI inside Loading, including a Skeleton.
  return (
    <Paper elevation={2} sx={{ flex: 1, p: 2 }}>
      <Skeleton variant="text" width="50%" height={40} sx={{ mb: 1 }} />
      <Skeleton variant="text" width="75%" height={40} sx={{ mb: 1 }} />
      <Skeleton variant="text" width="50%" height={40} sx={{ mb: 1 }} />
      <Skeleton variant="text" width="60%" height={40} sx={{ mb: 1 }} />
      <Skeleton variant="text" width="50%" height={40} sx={{ mb: 1 }} />
    </Paper>
  );
}

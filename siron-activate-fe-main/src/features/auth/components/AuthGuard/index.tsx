import { AppLayout, AuthCard } from "@imtf/panache";
import { Box } from "@mui/material";
import React from "react";

import { auth } from "@/auth";
import { PRODUCT_NAME } from "@/constants/misc";

import { SignIn } from "../SignIn";

import classes from "./styles.module.css";

type Props = {
  children: React.ReactElement;
};

export const AuthGuard = async ({ children }: Props) => {
  const session = await auth();

  if (!session?.user)
    return (
      <AppLayout className={classes.container}>
        <Box display="grid" justifyContent="center" alignItems="center">
          <AuthCard platform={PRODUCT_NAME}>
            <Box mt={12} display="flex" justifyContent="center">
              <SignIn />
            </Box>
          </AuthCard>
        </Box>
      </AppLayout>
    );

  return children;
};

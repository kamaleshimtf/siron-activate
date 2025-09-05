"use client";

import { getMuiTheme } from "@imtf/panache";
import { createTheme } from "@mui/material";

import Link from "@/components/Link";

export const theme = createTheme(
  {
    ...getMuiTheme("light"),
    cssVariables: true,
  },
  {
    components: {
      MuiAppBar: {
        defaultProps: {
          color: "secondary",
          position: "static",
        },
      },
      MuiLink: {
        defaultProps: {
          component: Link,
        },
      },
      MuiButtonBase: {
        defaultProps: {
          LinkComponent: Link,
        },
      },
    },
  },
);

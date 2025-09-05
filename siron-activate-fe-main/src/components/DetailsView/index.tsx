import { PaneLayout, PaneLayoutProps } from "@imtf/panache";
import { Button } from "@mui/material";
import { clsx } from "clsx";

import classes from "./styles.module.css";

type Props = Omit<PaneLayoutProps, "footer"> & { onClose: () => void };

export function DetailsView(props: Props) {
  const { onClose, ...paneLayoutProps } = props;
  return (
    <PaneLayout
      {...paneLayoutProps}
      classes={{
        ...paneLayoutProps.classes,
        footer: clsx(classes.footer, paneLayoutProps.classes?.footer),
      }}
      footer={
        <Button
          onClick={onClose}
          variant="outlined"
          sx={{
            justifySelf: "flex-start",
          }}
        >
          Return
        </Button>
      }
    />
  );
}

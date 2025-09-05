"use client";

import { ContextMenu, ContextMenuItem, usePopup } from "@imtf/panache";
import { Logout, Person } from "@mui/icons-material";
import { IconButton, IconButtonProps } from "@mui/material";
import { Session } from "next-auth";
import { forwardRef } from "react";

import { signOut } from "@/features/auth/actions/signOut";

const ToggleButton = forwardRef<HTMLButtonElement, IconButtonProps>(
  (props, ref) => (
    <IconButton ref={ref} color="inherit" {...props}>
      <Person />
    </IconButton>
  ),
) as Required<Parameters<typeof ContextMenu>[0]>["ToggleButton"];

ToggleButton.displayName = "ToggleButton";

type Props = {
  session: Session | null;
};

function UserMenu({ session }: Props) {
  const popupProps = usePopup();

  return (
    <ContextMenu
      {...popupProps}
      label={session?.user?.name ?? undefined}
      tooltipProps={{ title: null }}
      items={[
        <ContextMenuItem
          key="sign-out"
          label="Sign out"
          icon={Logout}
          onClick={() => void signOut()}
        />,
      ]}
      ToggleButton={ToggleButton}
    />
  );
}

export default UserMenu;

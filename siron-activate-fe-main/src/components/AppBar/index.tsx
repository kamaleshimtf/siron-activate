import {
  AppBarAppDrawerButton,
  AppBarNavigationItemProps,
  AppBarNavigationItemType,
  AppBar as IMTFAppBar,
  getAppBarNavigationDefaultLeftItems,
} from "@imtf/panache";

import { auth } from "@/auth";
import { PRODUCT_NAME } from "@/constants/misc";

import UserMenu from "../UserMenu";

import classes from "./styles.module.css";

const leftItems = getAppBarNavigationDefaultLeftItems({
  id: "app-bar-left-items",
  brandLogoLink: "/",
  productIdentityProps: {
    moduleName: PRODUCT_NAME,
    className: classes.identity,
  },
});

export default async function AppBar() {
  const session = await auth();

  return (
    <IMTFAppBar
      leftItems={[
        <AppBarAppDrawerButton key="app-drawer-button" />,
        ...leftItems,
      ]}
      rightItems={[
        {
          type: AppBarNavigationItemType.CUSTOM_COMPONENT,
          props: {
            id: "user-menu",
            session,
          },
          Component: UserMenu,
        } as AppBarNavigationItemProps<{
          id: string;
          session: typeof session;
        }>,
      ]}
    />
  );
}

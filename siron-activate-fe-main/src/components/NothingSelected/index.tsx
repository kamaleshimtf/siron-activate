import { Unselected } from "@imtf/panache";

import classes from "./styles.module.css";

type Props = {
  heading?: string;
  description?: string;
};

export function NothingSelected({
  heading = "Nothing selected",
  description,
}: Props) {
  return (
    <Unselected
      heading={heading}
      description={description}
      className={classes.main}
    />
  );
}

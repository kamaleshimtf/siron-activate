import { Button, ButtonProps } from "@mui/material";

import { signIn } from "@/auth";

export function SignIn({ ...props }: ButtonProps) {
  return (
    <form
      action={async () => {
        "use server";
        await signIn(process.env.AUTH_OIDC_CLIENT_ID);
      }}
    >
      <Button type="submit" {...props}>
        Sign In
      </Button>
    </form>
  );
}

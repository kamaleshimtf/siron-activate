import { FlatCompat } from "@eslint/eslintrc";
import { react, recommended } from "@imtf/eslint-config";

const compat = new FlatCompat({
  baseDirectory: import.meta.dirname,
});

const config = [
  ...compat.extends("next/typescript"),
  ...recommended,
  ...react,
  {
    languageOptions: {
      parserOptions: {
        projectService: {
          allowDefaultProject: ["eslint.config.mjs"],
        },
      },
    },
  },
];

export default config;

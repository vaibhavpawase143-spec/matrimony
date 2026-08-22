import js from "@eslint/js";
import globals from "globals";
import reactHooks from "eslint-plugin-react-hooks";
import reactRefresh from "eslint-plugin-react-refresh";

export default [
  { ignores: ["dist", "target", "src/main/resources", "src/components/ui/**"] },
  {
    files: ["src/**/*.{js,jsx}"],
    languageOptions: {
      ecmaVersion: 2020,
      sourceType: "module",
      globals: {
        ...globals.browser,
        ...globals.node,
      },
      parserOptions: {
        ecmaFeatures: {
          jsx: true,
        },
      },
    },
    plugins: {
      "react-hooks": reactHooks,
      "react-refresh": reactRefresh,
    },
    rules: {
      ...js.configs.recommended.rules,
      "react-refresh/only-export-components": "off",
      "react-hooks/rules-of-hooks": "warn",
      "react-hooks/exhaustive-deps": "warn",
      "no-unused-vars": "off",
      "no-empty": "off",
      "no-dupe-keys": "off",
      "no-undef": "off",
      "no-case-declarations": "off",
      "no-useless-escape": "off",
      "no-control-regex": "off",
      "no-regex-spaces": "off",
      "no-async-promise-executor": "off",
      "no-constant-binary-expression": "off",
    },
  },
];

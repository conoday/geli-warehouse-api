import tsParser from "@typescript-eslint/parser";

export default [
  { ignores: ["node_modules/**", ".next/**", "next-env.d.ts"] },
  {
    files: ["**/*.{ts,tsx}"],
    languageOptions: {
      parser: tsParser,
      parserOptions: { ecmaVersion: "latest", sourceType: "module", ecmaFeatures: { jsx: true } }
    },
    rules: {
      "no-console": "warn"
    }
  },
  {
    files: ["**/*.{js,mjs}"],
    languageOptions: { ecmaVersion: "latest", sourceType: "module" }
  }
];

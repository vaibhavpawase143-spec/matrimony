import { defineConfig, devices } from "@playwright/test";

export default defineConfig({
  testDir: "./tests/e2e",

  // प्रत्येक test साठी max time
  timeout: 45 * 1000,

  // Tests एकाच worker मध्ये sequential चालतील
  // त्यामुळे Vite/SPA वर unnecessary parallel load येणार नाही
  workers: 1,

  expect: {
    timeout: 10 * 1000,
  },

  fullyParallel: false,
  forbidOnly: !!process.env.CI,

  retries: process.env.CI ? 2 : 0,

  reporter: [
    ["html", { open: "never" }],
    ["list"],
  ],

  use: {
    baseURL: "http://localhost:5173",

    headless: false,

    // Navigation साठी separate timeout
    navigationTimeout: 20 * 1000,

    screenshot: "only-on-failure",
    video: "retain-on-failure",
    trace: "retain-on-failure",
  },

  projects: [
    {
      name: "chromium",
      use: {
        ...devices["Desktop Chrome"],
      },
    },
  ],
});
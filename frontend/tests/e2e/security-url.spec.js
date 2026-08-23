import { test, expect } from "@playwright/test";

const maliciousRedirects = [
  "//evil.com",
  "https://evil.com",
  "http://evil.com",
  "javascript:alert(1)",
  "data:text/html,<script>alert(1)</script>",
  "/\\evil.com",
  "\\evil.com",
];

test.describe("Task 12 - URL Security", () => {
  for (const redirect of maliciousRedirects) {
    test(`blocks unsafe redirect: ${redirect}`, async ({ page }) => {
  await page.goto(
    `/login?redirect=${encodeURIComponent(redirect)}`,
    {
      waitUntil: "domcontentloaded",
    }
  );

      // Login page should remain inside local application
      await expect(page).toHaveURL(
        /localhost/
      );

      // Must never navigate to evil.com
      expect(page.url()).not.toContain("evil.com");
    });
  }
});
import { test, expect } from "@playwright/test";

const protectedRoutes = [
  "/home",
  "/profile/create",
  "/search",
  "/profile/12345",
  "/kundli",
  "/messages",
  "/chat/12345/67890",
  "/notifications/12345",
  "/sent-interests",
  "/received-interests",
  "/likes",
  "/profile-visitors",
  "/shortlists",
  "/settings",
  "/account",
  "/upgrade",
  "/subscription-history",
  "/support/tickets",
  "/support/tickets/TEST-123",
];

test.describe("Task 12 - Protected Route Security", () => {
  for (const route of protectedRoutes) {
    test(`blocks unauthenticated access to ${route}`, async ({ page }) => {
      // Open same-origin page first so storage can be safely cleared
      await page.goto("/", {
        waitUntil: "domcontentloaded",
      });

      // Ensure no authentication/session remains
      await page.evaluate(() => {
        sessionStorage.clear();
        localStorage.clear();
      });

      // Try direct access to protected route
      await page.goto(route, {
        waitUntil: "domcontentloaded",
      });

      // Give React Router/auth guard time to process
      await page.waitForTimeout(500);

      const currentUrl = page.url();
      const currentPath = new URL(currentUrl).pathname;

      // Protected route must not remain accessible
      expect(currentPath).not.toBe(route);

      // Valid safe outcomes:
      // 1. Redirect to login
      // 2. Redirect to home/root
      // 3. Redirect to another safe internal page
      expect(currentUrl).toMatch(/^http:\/\/localhost:5173\//);
    });
  }
});
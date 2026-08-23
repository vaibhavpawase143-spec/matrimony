import { describe, it, expect } from "vitest";
import { validateSafeRedirect, isSafeUrl, sanitizeUrl } from "../utils/urlSecurity";

// Formula injection test utility matching the admin implementation
const sanitizeSpreadsheetCell = (val) => {
  if (val === null || val === undefined) return "";
  const str = String(val);
  if (/^[=+\-@\t\r|]/.test(str)) {
    return `'${str}`;
  }
  return str;
};

describe("Frontend URL & Open Redirect Security", () => {
  describe("validateSafeRedirect", () => {
    it("should accept valid relative application paths", () => {
      expect(validateSafeRedirect("/home")).toBe("/home");
      expect(validateSafeRedirect("/profile/create")).toBe("/profile/create");
      expect(validateSafeRedirect("/settings?tab=privacy")).toBe("/settings?tab=privacy");
      expect(validateSafeRedirect("/search?city=Pune&age=25")).toBe("/search?city=Pune&age=25");
    });

    it("should reject protocol-relative external redirects", () => {
      expect(validateSafeRedirect("//evil.example.com", "/home")).toBe("/home");
      expect(validateSafeRedirect("///evil.example.com", "/home")).toBe("/home");
      expect(validateSafeRedirect("//google.com/test", "/fallback")).toBe("/fallback");
    });

    it("should reject backslash evasion techniques", () => {
      expect(validateSafeRedirect("/\\evil.example.com", "/home")).toBe("/home");
      expect(validateSafeRedirect("/\\\\evil.example.com", "/home")).toBe("/home");
      expect(validateSafeRedirect("\\evil.example.com", "/home")).toBe("/home");
      expect(validateSafeRedirect("/\tevil.example.com", "/home")).toBe("/home");
    });

    it("should reject absolute external URLs", () => {
      expect(validateSafeRedirect("https://evil.example.com", "/home")).toBe("/home");
      expect(validateSafeRedirect("http://evil.example.com", "/home")).toBe("/home");
      expect(validateSafeRedirect("ftp://evil.example.com", "/home")).toBe("/home");
    });

    it("should reject dangerous URI schemes", () => {
      expect(validateSafeRedirect("javascript:alert(document.cookie)", "/home")).toBe("/home");
      expect(validateSafeRedirect("javascript://alert(1)", "/home")).toBe("/home");
      expect(validateSafeRedirect("data:text/html,<script>alert(1)</script>", "/home")).toBe("/home");
      expect(validateSafeRedirect("vbscript:MsgBox(1)", "/home")).toBe("/home");
    });

    it("should reject control characters and invalid inputs", () => {
      expect(validateSafeRedirect("/home\x00evil", "/home")).toBe("/home");
      expect(validateSafeRedirect("/home\x1fevil", "/home")).toBe("/home");
      expect(validateSafeRedirect(null, "/home")).toBe("/home");
      expect(validateSafeRedirect(undefined, "/home")).toBe("/home");
      expect(validateSafeRedirect("", "/home")).toBe("/home");
      expect(validateSafeRedirect(12345, "/home")).toBe("/home");
    });
  });

  describe("isSafeUrl & sanitizeUrl", () => {
    it("should allow safe web URLs and relative paths", () => {
      expect(isSafeUrl("https://example.com/photo.jpg")).toBe(true);
      expect(isSafeUrl("http://localhost:3000/api/users")).toBe(true);
      expect(isSafeUrl("/uploads/photo123.jpg")).toBe(true);
      expect(isSafeUrl("blob:https://example.com/uuid-1234")).toBe(true);
      expect(isSafeUrl("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==")).toBe(true);
      expect(isSafeUrl("data:image/jpeg;base64,/9j/4AAQSkZJRg==")).toBe(true);
      expect(isSafeUrl("data:image/webp;base64,UklGRg==")).toBe(true);
    });

    it("should reject malicious execution schemes", () => {
      expect(isSafeUrl("javascript:alert(1)")).toBe(false);
      expect(isSafeUrl("JAVASCRIPT:alert(1)")).toBe(false);
      expect(isSafeUrl("data:text/html;base64,PHNjcmlwdD5hbGVydCgxKTwvc2NyaXB0Pg==")).toBe(false);
      expect(isSafeUrl("data:application/javascript;base64,YWxlcnQoMSk=")).toBe(false);
      expect(isSafeUrl("vbscript:alert(1)")).toBe(false);
      expect(isSafeUrl("file:///etc/passwd")).toBe(false);
    });

    it("should sanitize unsafe URLs with fallback", () => {
      expect(sanitizeUrl("javascript:alert(1)", "/default.png")).toBe("/default.png");
      expect(sanitizeUrl("https://example.com/image.jpg", "/default.png")).toBe("https://example.com/image.jpg");
    });
  });
});

describe("Spreadsheet & CSV Formula Injection (CWE-1236) Protection", () => {
  it("should escape formula trigger prefixes", () => {
    expect(sanitizeSpreadsheetCell("=cmd|' /C calc'!A0")).toBe("'=cmd|' /C calc'!A0");
    expect(sanitizeSpreadsheetCell("+1+2")).toBe("'+1+2");
    expect(sanitizeSpreadsheetCell("-1+1")).toBe("'-1+1");
    expect(sanitizeSpreadsheetCell("@SUM(A1:A10)")).toBe("'@SUM(A1:A10)");
    expect(sanitizeSpreadsheetCell("\t=calc")).toBe("'\t=calc");
    expect(sanitizeSpreadsheetCell("\r=calc")).toBe("'\r=calc");
    expect(sanitizeSpreadsheetCell("|calc")).toBe("'|calc");
  });

  it("should preserve legitimate names, Marathi text, numbers and text intact", () => {
    expect(sanitizeSpreadsheetCell("Rahul Sharma")).toBe("Rahul Sharma");
    expect(sanitizeSpreadsheetCell("राहुल पवार")).toBe("राहुल पवार");
    expect(sanitizeSpreadsheetCell("पुणे, महाराष्ट्र")).toBe("पुणे, महाराष्ट्र");
    expect(sanitizeSpreadsheetCell("Software Engineer")).toBe("Software Engineer");
    expect(sanitizeSpreadsheetCell(100)).toBe("100");
    expect(sanitizeSpreadsheetCell("")).toBe("");
    expect(sanitizeSpreadsheetCell(null)).toBe("");
    expect(sanitizeSpreadsheetCell(undefined)).toBe("");
  });
});

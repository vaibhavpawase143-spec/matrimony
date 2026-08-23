import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";

describe("Frontend Infrastructure Smoke Test", () => {
  it("renders a basic element using React Testing Library and Vitest", () => {
    render(<div data-testid="smoke-element">Gathbandhan Matrimony Test Infrastructure</div>);
    const element = screen.getByTestId("smoke-element");
    expect(element).toBeInTheDocument();
    expect(element.textContent).toContain("Gathbandhan Matrimony");
  });
});

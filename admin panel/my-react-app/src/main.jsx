import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { GoogleReCaptchaProvider } from "react-google-recaptcha-v3";
import { Toaster } from "sonner";

import "./index.css";
import App from "./App.jsx";

import { initGA } from "./utils/analytics";

// Initialize Google Analytics
initGA();

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <GoogleReCaptchaProvider
      reCaptchaKey={import.meta.env.VITE_RECAPTCHA_SITE_KEY}
    >
      <App />

      <Toaster
        position="top-right"
        richColors
        closeButton
        duration={2500}
      />
    </GoogleReCaptchaProvider>
  </StrictMode>
);
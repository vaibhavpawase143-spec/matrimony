import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";

export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
  ],

  define: {
    global: "globalThis",
  },

  server: {
    port: 5173,

    proxy: {
      "/api": {
        target: "https://localhost:9090",
        changeOrigin: true,
        secure: false,
        ws: true,
      },

      "/ws": {
        target: "wss://localhost:9090",
        changeOrigin: true,
        secure: false,
        ws: true,
      },
    },
  },
});
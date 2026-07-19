import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import path from "path";
import { componentTagger } from "lovable-tagger";

export default defineConfig(({ mode }) => ({
  server: {
    // Allow access from other devices on the network
    host: true,
    port: 3000,
    strictPort: true,

    hmr: {
      overlay: false,
    },

    proxy: {
      "/api": {
        target: "http://localhost:9090",
        changeOrigin: true,
        secure: false,
        ws: true,
      },

      "/ws": {
        target: "ws://localhost:9090",
        ws: true,
        changeOrigin: true,
      },
    },
  },

  plugins: [
    react(),
    mode === "development" && componentTagger(),
  ].filter(Boolean),

  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },

  define: {
    global: "window",
  },
}));
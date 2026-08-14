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
        target: "http://localhost:9090",
        changeOrigin: true,
        secure: false,
        ws: true,
      },

      "/uploads": {
        target: "http://localhost:9090",
        changeOrigin: true,
        secure: false,
      },

      "/ws": {
        target: "ws://localhost:9090",
        changeOrigin: true,
        secure: false,
        ws: true,
      },
    },
  },

  build: {
    target: "esnext",
    minify: "esbuild",
    cssCodeSplit: true,
    chunkSizeWarningLimit: 1000,
    rollupOptions: {
      output: {
        manualChunks(id) {
          if (id.includes("node_modules")) {
            if (id.includes("react-dom") || id.includes("react-router-dom") || id.includes("react") || id.includes("redux")) {
              return "vendor-react";
            }
            if (id.includes("recharts") || id.includes("jspdf") || id.includes("xlsx")) {
              return "vendor-charts-reports";
            }
            if (id.includes("lucide-react") || id.includes("react-icons")) {
              return "vendor-icons";
            }
          }
        },
      },
    },
  },

  optimizeDeps: {
    include: [
      "react",
      "react-dom",
      "react-router-dom",
      "axios",
      "recharts",
      "lucide-react",
    ],
  },
});
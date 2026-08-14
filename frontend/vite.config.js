import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import path from "path";
import { componentTagger } from "lovable-tagger";

export default defineConfig(({ mode }) => ({
  server: {
    // Dev server settings
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

      "/uploads": {
        target: "http://localhost:9090",
        changeOrigin: true,
        secure: false,
      },

      "/ws": {
        target: "ws://localhost:9090",
        ws: true,
        changeOrigin: true,
        secure: false,
      },
    },
  },

  // 👇 Add this for preview mode
  preview: {
    host: true,
    port: 3000,
    strictPort: true,
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

  build: {
    target: "esnext",
    minify: "esbuild",
    cssCodeSplit: true,
    chunkSizeWarningLimit: 1000,
    rollupOptions: {
      output: {
        manualChunks(id) {
          if (id.includes("node_modules")) {
            if (id.includes("react-dom") || id.includes("react-router-dom") || id.includes("react")) {
              return "vendor-react";
            }
            if (id.includes("@radix-ui") || id.includes("lucide-react")) {
              return "vendor-ui";
            }
            if (id.includes("recharts") || id.includes("framer-motion")) {
              return "vendor-charts";
            }
            if (id.includes("@stomp/stompjs") || id.includes("sockjs-client") || id.includes("axios") || id.includes("@tanstack")) {
              return "vendor-network";
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
      "lucide-react",
      "axios",
      "recharts",
      "framer-motion",
    ],
  },
}));
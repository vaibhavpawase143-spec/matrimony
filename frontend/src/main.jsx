window.global = window;

import { createRoot } from "react-dom/client";
import App from "./App.jsx";
import { LanguageProvider } from "./context/LanguageContext.jsx";
import { Toaster } from "react-hot-toast";

import "./index.css";

createRoot(
document.getElementById("root")
).render(

<LanguageProvider>

<App />

<Toaster

position="top-right"

toastOptions={{

duration:3000,

style:{

background:"#1E293B",
color:"#ffffff",
padding:"16px",
borderRadius:"14px",
fontWeight:"600",
boxShadow:"0 8px 25px rgba(0,0,0,0.25)"

},

success:{

iconTheme:{

primary:"#22C55E",
secondary:"#ffffff"

},

style:{

border:"1px solid rgba(34,197,94,0.4)"

}

},

error:{

iconTheme:{

primary:"#EC4899",
secondary:"#ffffff"

},

style:{

background:
"linear-gradient(135deg,#1E293B,#2D1B3D)",

border:
"1px solid rgba(236,72,153,0.45)"

}

}

}}

/>

</LanguageProvider>

);
import { useState } from "react";
import { Send, Search } from "lucide-react";
import Navbar from "@/components/Navbar";
import { useLanguage } from "@/context/LanguageContext";
import profile1 from "@/assets/profile1.jpg";
import profile2 from "@/assets/profile2.jpg";
import profile3 from "@/assets/profile3.jpg";
import profile4 from "@/assets/profile4.jpg";

const contacts = [
  { id: 1, name: "Priya Sharma", avatar: profile1, lastMsg: "Hi! I'd like to know you.", time: "2m ago", unread: 2 },
  { id: 2, name: "Sneha Patel", avatar: profile2, lastMsg: "Let's connect further.", time: "1h ago", unread: 0 },
  { id: 3, name: "Aarushi Gupta", avatar: profile3, lastMsg: "That sounds great!", time: "3h ago", unread: 1 },
  { id: 4, name: "Neha Verma", avatar: profile4, lastMsg: "Thanks for the interest!", time: "1d ago", unread: 0 },
];

const chatMessages = [
  { id: 1, sender: "them", text: "Hi! I saw your profile and I'd like to know more about you.", time: "10:30 AM" },
  { id: 2, sender: "me", text: "Hello! Thank you for reaching out. I'd love to connect.", time: "10:32 AM" },
  { id: 3, sender: "them", text: "That's great! What do you do for a living?", time: "10:33 AM" },
  { id: 4, sender: "me", text: "I'm a software engineer based in Pune. What about you?", time: "10:35 AM" },
  { id: 5, sender: "them", text: "I'm a doctor in Mumbai. I love traveling and reading in my free time.", time: "10:37 AM" },
  { id: 6, sender: "me", text: "That's wonderful! I enjoy traveling too. Have you been to Rajasthan?", time: "10:38 AM" },
];

const Messages = () => {
  const { t } = useLanguage();
  const [selected, setSelected] = useState(contacts[0]);
  const [msg, setMsg] = useState("");

  return (
    <div className="min-h-screen bg-muted/30 flex flex-col">
      <Navbar />

      <div className="flex-1 container mx-auto px-4 py-6">
        <div className="bg-card rounded-xl border border-border overflow-hidden flex h-[calc(100vh-120px)]">
          {/* Contacts list */}
          <div className="w-80 border-r border-border flex flex-col">
            <div className="p-4 border-b border-border">
              <h2 className="text-sm font-semibold text-foreground mb-3">{t.messages.title}</h2>
              <div className="relative">
                <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <input className="w-full bg-background border border-border rounded-lg pl-9 pr-4 py-2 text-xs text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20" placeholder={t.messages.searchPlaceholder} />
              </div>
            </div>
            <div className="flex-1 overflow-y-auto">
              {contacts.map((c) => (
                <button key={c.id} onClick={() => setSelected(c)} className={`w-full flex items-center gap-3 px-4 py-3 hover:bg-muted/50 transition-colors text-left ${selected.id === c.id ? "bg-muted" : ""}`}>
                  <img src={c.avatar} alt={c.name} className="h-10 w-10 rounded-full object-cover flex-shrink-0" />
                  <div className="min-w-0 flex-1">
                    <div className="flex items-center justify-between">
                      <span className="text-sm font-semibold text-foreground truncate">{c.name}</span>
                      <span className="text-[10px] text-muted-foreground">{c.time}</span>
                    </div>
                    <p className="text-xs text-muted-foreground truncate">{c.lastMsg}</p>
                  </div>
                  {c.unread > 0 && (
                    <span className="h-5 w-5 bg-primary text-primary-foreground text-[10px] font-bold rounded-full flex items-center justify-center flex-shrink-0">{c.unread}</span>
                  )}
                </button>
              ))}
            </div>
          </div>

          {/* Chat window */}
          <div className="flex-1 flex flex-col">
            <div className="px-5 py-3 border-b border-border flex items-center gap-3">
              <img src={selected.avatar} alt={selected.name} className="h-9 w-9 rounded-full object-cover" />
              <div>
                <p className="text-sm font-semibold text-foreground">{selected.name}</p>
                <p className="text-[10px] text-emerald-badge">{t.messages.onlineStatus}</p>
              </div>
            </div>

            <div className="flex-1 overflow-y-auto px-5 py-4 space-y-3">
              {chatMessages.map((m) => (
                <div key={m.id} className={`flex ${m.sender === "me" ? "justify-end" : "justify-start"}`}>
                  <div className={`max-w-[70%] px-4 py-2.5 rounded-2xl text-sm ${
                    m.sender === "me"
                      ? "bg-primary text-primary-foreground rounded-br-sm"
                      : "bg-muted text-foreground rounded-bl-sm"
                  }`}>
                    <p>{m.text}</p>
                    <p className={`text-[10px] mt-1 ${m.sender === "me" ? "text-primary-foreground/60" : "text-muted-foreground"}`}>{m.time}</p>
                  </div>
                </div>
              ))}
            </div>

            <div className="p-4 border-t border-border flex items-center gap-3">
              <input
                value={msg}
                onChange={(e) => setMsg(e.target.value)}
                className="flex-1 bg-background border border-border rounded-lg px-4 py-2.5 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary/20"
                placeholder={t.messages.typeMessagePlaceholder}
              />
              <button className="h-10 w-10 rounded-lg bg-primary text-primary-foreground flex items-center justify-center hover:bg-primary/90 transition-colors">
                <Send className="h-4 w-4" />
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Messages;

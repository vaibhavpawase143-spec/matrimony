import { ChevronDown } from "lucide-react";

const MessagePreview = () => {
  return (
    <div className="container mx-auto px-4 pb-12">
      <div className="max-w-3xl ml-auto">
        <div className="bg-card rounded-xl shadow-md border border-border p-4 flex items-center gap-4">
          <div className="h-10 w-10 rounded-full bg-muted overflow-hidden flex-shrink-0">
            <div className="h-full w-full bg-purple-light/30 flex items-center justify-center text-accent font-bold text-sm">P</div>
          </div>
          <div className="flex-1 min-w-0">
            <p className="text-sm font-body">
              <span className="font-bold text-foreground">Priya.</span>{" "}
              <span className="text-muted-foreground">Flow:</span>
            </p>
            <p className="text-sm text-muted-foreground truncate">Hi! I'd like to know you.</p>
          </div>
          <ChevronDown className="h-4 w-4 text-muted-foreground flex-shrink-0" />
        </div>
      </div>
    </div>
  );
};

export default MessagePreview;

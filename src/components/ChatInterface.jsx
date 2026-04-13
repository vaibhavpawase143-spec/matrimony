import { useState, useEffect, useRef } from 'react';
import { Send, ChevronLeft } from 'lucide-react';
import { Input } from '@/components/ui/input';

const ChatInterface = ({ conversations = [], selectedConversationId = null, onSelectConversation, onSendMessage, onBack }) => {
  const [messages, setMessages] = useState([]);
  const [messageInput, setMessageInput] = useState('');
  const [loadingMessages, setLoadingMessages] = useState(false);
  const messagesEndRef = useRef(null);

  const selectedConversation = conversations.find(c => c.id === selectedConversationId);

  useEffect(() => {
    if (selectedConversationId) {
      setLoadingMessages(true);
      // Simulate loading messages
      setTimeout(() => {
        setMessages(selectedConversation?.messages || []);
        setLoadingMessages(false);
      }, 300);
    }
  }, [selectedConversationId, selectedConversation]);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const handleSendMessage = () => {
    if (!messageInput.trim()) return;

    const newMessage = {
      id: Date.now(),
      text: messageInput,
      sender: 'user',
      timestamp: new Date(),
    };

    setMessages(prev => [...prev, newMessage]);
    onSendMessage && onSendMessage(selectedConversationId, messageInput);
    setMessageInput('');

    // Simulate reply
    setTimeout(() => {
      setMessages(prev => [
        ...prev,
        {
          id: Date.now(),
          text: 'Thanks for your message! I will reply soon.',
          sender: 'other',
          timestamp: new Date(),
        },
      ]);
    }, 1000);
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSendMessage();
    }
  };

  // List View
  if (!selectedConversationId) {
    return (
      <div className="bg-background dark:bg-background rounded-lg border border-border h-full overflow-hidden flex flex-col">
        {/* Header */}
        <div className="border-b border-border p-4 bg-muted dark:bg-muted/50">
          <h2 className="text-lg font-semibold text-foreground">Messages</h2>
        </div>

        {/* Conversations List */}
        <div className="flex-1 overflow-y-auto">
          {conversations.length === 0 ? (
            <div className="flex items-center justify-center h-full text-center p-4">
              <div>
                <p className="text-muted-foreground mb-2">No conversations yet</p>
                <p className="text-sm text-muted-foreground">Start chatting with profiles you like</p>
              </div>
            </div>
          ) : (
            <div className="divide-y divide-border">
              {conversations.map(conversation => (
                <button
                  key={conversation.id}
                  onClick={() => onSelectConversation(conversation.id)}
                  className="w-full p-4 hover:bg-muted dark:hover:bg-muted/50 transition-colors text-left"
                >
                  <div className="flex items-center justify-between mb-2">
                    <h3 className="font-semibold text-foreground">{conversation.name}</h3>
                    <span className="text-xs text-muted-foreground">{conversation.timestamp}</span>
                  </div>
                  <p className="text-sm text-muted-foreground truncate">{conversation.lastMessage}</p>
                </button>
              ))}
            </div>
          )}
        </div>
      </div>
    );
  }

  // Chat View
  return (
    <div className="bg-background dark:bg-background rounded-lg border border-border h-full overflow-hidden flex flex-col">
      {/* Header */}
      <div className="border-b border-border p-4 bg-muted dark:bg-muted/50 flex items-center justify-between">
        <button
          onClick={onBack}
          className="md:hidden p-1 hover:bg-muted-foreground/20 rounded-lg transition-colors mr-2"
        >
          <ChevronLeft className="h-5 w-5" />
        </button>
        <div className="flex-1">
          <h2 className="font-semibold text-foreground">{selectedConversation?.name}</h2>
          <p className="text-xs text-muted-foreground">{selectedConversation?.status || 'Active'}</p>
        </div>
      </div>

      {/* Messages */}
      <div className="flex-1 overflow-y-auto p-4 space-y-3 bg-gradient-to-b from-background to-muted/30 dark:from-background dark:to-muted/20">
        {loadingMessages ? (
          <div className="flex items-center justify-center h-full">
            <div className="animate-spin rounded-full h-8 w-8 border-2 border-primary border-t-transparent"></div>
          </div>
        ) : messages.length === 0 ? (
          <div className="flex items-center justify-center h-full text-center">
            <div>
              <p className="text-muted-foreground">No messages yet</p>
              <p className="text-sm text-muted-foreground">Say hello to start the conversation!</p>
            </div>
          </div>
        ) : (
          messages.map(message => (
            <div
              key={message.id}
              className={`flex ${message.sender === 'user' ? 'justify-end' : 'justify-start'}`}
            >
              <div
                className={`max-w-xs px-4 py-2 rounded-lg ${
                  message.sender === 'user'
                    ? 'bg-primary text-primary-foreground rounded-br-none'
                    : 'bg-muted dark:bg-muted/70 text-foreground rounded-bl-none'
                }`}
              >
                <p className="text-sm">{message.text}</p>
                <p className={`text-xs mt-1 ${message.sender === 'user' ? 'text-primary-foreground/70' : 'text-muted-foreground'}`}>
                  {new Date(message.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                </p>
              </div>
            </div>
          ))
        )}
        <div ref={messagesEndRef} />
      </div>

      {/* Input */}
      <div className="border-t border-border p-3 bg-background dark:bg-background space-y-2">
        <div className="flex gap-2">
          <Input
            value={messageInput}
            onChange={(e) => setMessageInput(e.target.value)}
            onKeyPress={handleKeyPress}
            placeholder="Type a message..."
            className="flex-1"
          />
          <button
            onClick={handleSendMessage}
            disabled={!messageInput.trim()}
            className="bg-primary hover:bg-primary/90 disabled:bg-primary/50 text-primary-foreground rounded-lg p-2.5 transition-colors"
          >
            <Send className="h-5 w-5" />
          </button>
        </div>
        <p className="text-xs text-muted-foreground">Press Enter to send</p>
      </div>
    </div>
  );
};

export default ChatInterface;
import EmojiPicker from "emoji-picker-react";
import React, { useEffect, useState, useRef } from "react";
import { useSearchParams,useNavigate } from "react-router-dom";
import {Search,Mic} from "lucide-react";
import {
    connectCallSocket,
    sendCallRequest,
    disconnectCallSocket,
    sendSignal
} from "@/services/callSocket";
import { subscriptionAPI } from "@/services/api";
import {

connectNotifications,

disconnectNotifications,

sendTyping,

stopTyping

} from "../services/websocket";

import {
    Phone,
    Video
} from "lucide-react";



import {
    getConversations,
    getMessages,
    sendMessage,
    getChatUser,
    markSeen,
    uploadAudio,
    uploadImage,
    uploadVideo,
    uploadDocument,
    deleteForMe,
    deleteForEveryone,
    pinMessage,
    unpinMessage,
    starMessage,
    unstarMessage,
    forwardMessage
} from "@/services/chatApi";


const Messages=()=>{

const [searchParams] = useSearchParams();
const navigate = useNavigate();
const receiverId = searchParams.get("receiverId");

console.log("receiverId =", receiverId);

const [conversations,setConversations]=useState([]);

const [selected,setSelected]=useState(null);
useEffect(() => {

    if (selected?.otherUserId) {

        localStorage.setItem(
            "activeChatUserId",
            selected.otherUserId
        );

    } else {

        localStorage.removeItem(
            "activeChatUserId"
        );

    }

    return () => {

        localStorage.removeItem(
            "activeChatUserId"
        );

    };

}, [selected]);
const [isPremium, setIsPremium] = useState(false);
const [showUpgradePopup, setShowUpgradePopup] = useState(false);


const [showReactionPicker,setShowReactionPicker] =useState(false);

const [showCallModal,setShowCallModal] =useState(false);

const [callType,setCallType] =useState(null);

const [messages,setMessages]=useState([]);
const [incomingCall, setIncomingCall] = useState(null);

const [checkingPremium, setCheckingPremium] = useState(true);
const [isTyping, setIsTyping] = useState(false);

const typingTimeout = useRef(null);

const [replyingTo, setReplyingTo] =useState(null);

const [contextMenu,setContextMenu] = useState(null);

const [selectedMessage,setSelectedMessage] = useState(null);

const [showForwardModal, setShowForwardModal] = useState(false);

const [forwardSearch, setForwardSearch] = useState("");

const [selectedForwardUser, setSelectedForwardUser] = useState(null);

const [selectionMode, setSelectionMode] = useState(false);

const [selectedMessages, setSelectedMessages] = useState([]);

const [newMessage,setNewMessage]=useState("");

const [showDeleteModal,setShowDeleteModal] =useState(false);

const [showBulkDeleteModal, setShowBulkDeleteModal] = useState(false);

const [bulkDeleteForEveryone, setBulkDeleteForEveryone] = useState(false);

const [showEmojiPicker,setShowEmojiPicker] =useState(false);

const [showAttachmentMenu,setShowAttachmentMenu] =useState(false);

const [selectedImage,setSelectedImage] =useState(null);

const [selectedVideo,setSelectedVideo] = useState(null);

const [viewImage,setViewImage] = useState(null);

const [viewVideo, setViewVideo] = useState(null);

const [showCamera,setShowCamera] =useState(false);

const [capturedImage,setCapturedImage] =useState(null);

const [selectedDocument,setSelectedDocument] =useState(null);

const documentInputRef = useRef(null);

const localStreamRef = useRef(null);

const peerConnectionRef = useRef(null);

const remoteAudioRef = useRef(null);

const [isRecording,setIsRecording] =useState(false);

const [audioBlob,setAudioBlob] = useState(null);

const [audioPreview,setAudioPreview] =useState(null);

const audioFileInputRef = useRef(null);

const videoRef = useRef(null);

const canvasRef = useRef(null);

const [selectedAudioFile, setSelectedAudioFile] =useState(null);

const mediaRecorderRef = useRef(null);

const audioChunksRef = useRef([]);

const fileInputRef = useRef(null);

const attachmentRef = useRef(null);

const messagesEndRef = useRef(null);

const chatContainerRef = useRef(null);

const micButtonRef = useRef(null);

const getDateLabel = (dateString) => {

    const date = new Date(dateString);

    const today = new Date();

    const yesterday = new Date();
    yesterday.setDate(today.getDate() - 1);

    if (
        date.toDateString() ===
        today.toDateString()
    ) {
        return "Today";
    }

    if (
        date.toDateString() ===
        yesterday.toDateString()
    ) {
        return "Yesterday";
    }

    const diffDays = Math.floor(
        (today - date) /
        (1000 * 60 * 60 * 24)
    );

    if (diffDays < 7) {

        return date.toLocaleDateString(
            "en-US",
            {
                weekday: "long"
            }
        );

    }

    return date.toLocaleDateString(
        "en-GB"
    );
};
const checkPremium = async () => {

    try {

        const subscription =
            await subscriptionAPI.getMySubscription();

        if (subscription?.isActive) {

            setIsPremium(true);

            await loadConversations();

        } else {

            setIsPremium(false);

           setShowUpgradePopup(true);

        }

    } catch (err) {

        setIsPremium(false);

        if (receiverId) {

            setShowUpgradePopup(true);

        }

    } finally {

        setCheckingPremium(false);

    }

};

const formatLastSeen = (lastSeen) => {

    if (!lastSeen) {

        return "Offline";

    }

    const date = new Date(lastSeen);

    const now = new Date();

    const isToday =
        date.toDateString() ===
        now.toDateString();

    if (isToday) {

        return `Last seen today at ${date.toLocaleTimeString([], {
            hour: "2-digit",
            minute: "2-digit"
        })}`;

    }

    const yesterday = new Date();

    yesterday.setDate(now.getDate() - 1);

    if (
        date.toDateString() ===
        yesterday.toDateString()
    ) {

        return `Last seen yesterday at ${date.toLocaleTimeString([], {
            hour: "2-digit",
            minute: "2-digit"
        })}`;

    }

    return `Last seen ${date.toLocaleDateString()} ${date.toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit"
    })}`;

};
useEffect(() => {

    const load = async () => {

        await checkPremium();

    };

    load();

}, []);
useEffect(() => {

    const user = JSON.parse(
        localStorage.getItem("user")
    );

    connectNotifications(

    user.profile.userId,

(message)=>{

    console.log("LIVE MESSAGE =", message);

    if (!selected) {

        return;

    }

    if (
        Number(message.sender?.id) !== Number(selected.otherUserId) &&
        Number(message.receiver?.id) !== Number(selected.otherUserId)
    ) {

        return;

    }

    setMessages(prev => {

        const exists = prev.some(m => m.id === message.id);

        if (exists) {

            return prev;

        }

        return [...prev, message];

    });
loadConversations();

window.dispatchEvent(
    new Event("dashboardUpdated")
);

},

    (data)=>{

        if(

            data.action==="TYPING" &&

            selected &&

            data.sender===selected.otherUsername

        ){

            setIsTyping(true);

            clearTimeout(typingTimeout.current);

            typingTimeout.current=setTimeout(()=>{

                setIsTyping(false);

            },2000);

        }

        if(

            data.action==="STOP_TYPING" &&

            selected &&

            data.sender===selected.otherUsername

        ){

            setIsTyping(false);

        }

    },

    (status)=>{

        console.log("STATUS =",status);

        setConversations(prev=>

            prev.map(chat=>{

                if(chat.otherUsername!==status.email){

                    return chat;

                }

                return{

                    ...chat,

                    isOnline:status.online,

                    lastSeen:status.lastSeen

                };

            })

        );

        setSelected(prev=>{

            if(!prev){

                return prev;

            }

            if(prev.otherUsername!==status.email){

                return prev;

            }

            return{

                ...prev,

                isOnline:status.online,

                lastSeen:status.lastSeen

            };

        });

    }

    );

    return () => {

        disconnectNotifications();

    };

}, [selected]);

useEffect(()=>{

connectCallSocket(

(data)=>{

// ================= CALL =================

if(

data.type==="VOICE_CALL" ||

data.type==="VIDEO_CALL"

){

setIncomingCall(data);

}

// ================= TYPING =================

if(

data.action==="TYPING"

){

setIsTyping(true);

clearTimeout(

typingTimeout.current

);

typingTimeout.current=

setTimeout(()=>{

setIsTyping(false);

},2000);

}

if(

data.action==="STOP_TYPING"

){

setIsTyping(false);

}

}

);

return()=>{

disconnectCallSocket();

};

},[]);

// useEffect(() => {
//
//     if (!selected) return;
//
//     const latest = conversations.find(
//
//         c => c.conversationId === selected.conversationId
//
//     );
//
//     if (!latest) return;
//
//     if (
//
//         latest.isOnline !== selected.isOnline ||
//
//         latest.lastSeen !== selected.lastSeen
//
//     ) {
//
//         setSelected(latest);
//
//     }
//
// }, [conversations]);

const loadConversations = async () => {

    try {

        const res = await getConversations();

        const data = res.data || [];

        setConversations(data);

        if (receiverId) {

            if (
                selected &&
                String(selected.otherUserId) === String(receiverId)
            ) {
                return;
            }

            const chat = data.find(
                c => String(c.otherUserId) === String(receiverId)
            );

            if (chat) {

                setSelected(chat);

                await loadChat(chat);

                return;
            }

            const userRes = await getChatUser(receiverId);

            setSelected({
                conversationId: 0,
                otherUserId: userRes.data.otherUserId,
                otherUsername: userRes.data.otherUsername,
                isOnline: userRes.data.isOnline,
                lastSeen: userRes.data.lastSeen,
                lastMessage: ""
            });

            setMessages([]);

            return;
        }

        // Existing logic
        if (data.length > 0) {

            if (selected) {

                const updated = data.find(
                    c => c.conversationId === selected.conversationId
                );

                if (updated) {

                    setSelected(updated);

                }

            } else {

                setSelected(data[0]);

                await loadChat(data[0]);

            }

        }

    } catch (err) {

        console.log(err);

    }

};

const loadChat = async (chat) => {

    // ✅ Conversation अजून तयार झालेली नाही
    if (!chat?.conversationId || chat.conversationId === 0) {

        setMessages([]);

        return;

    }

    try {

        await markSeen(chat.conversationId);

        const res = await getMessages(

            Number(chat.otherUserId),

            0,

            10000

        );

        console.log("CHAT RESPONSE =", res);

        console.log("CONTENT =", res.data.content);

        const content = [...(res.data.content || [])];

        content.sort(

            (a, b) =>

                new Date(a.timestamp || a.createdAt) -

                new Date(b.timestamp || b.createdAt)

        );

        setMessages(content);

        setMessages(content);

        setTimeout(() => {

            if (chatContainerRef.current) {

                chatContainerRef.current.scrollTop =
                    chatContainerRef.current.scrollHeight;

            }

        }, 1000);

        setTimeout(() => {

            if (chatContainerRef.current) {

                chatContainerRef.current.scrollTop =
                    chatContainerRef.current.scrollHeight;

            }

        }, 0);

    }

    catch (err) {

        console.log(err);

    }

};
const user =
JSON.parse(localStorage.getItem("user"));

const currentUserId =
user.profile.userId;

const sortedMessages = messages;



useEffect(()=>{

if(showCamera){

startCamera();

}

},[showCamera]);

useEffect(() => {

    const handleClickOutside = (event) => {

        if (
            attachmentRef.current &&
            !attachmentRef.current.contains(event.target)
        ) {
            setShowAttachmentMenu(false);
        }

        if (
            isRecording &&
            !micButtonRef.current?.contains(event.target)
        ) {
            setIsRecording(false);
        }

    };

    document.addEventListener(
        "click",
        handleClickOutside
    );

    return () => {

        document.removeEventListener(
            "click",
            handleClickOutside
        );

    };

}, [isRecording]);
const capturePhoto = ()=>{

const canvas = canvasRef.current;
const video = videoRef.current;

if(!canvas || !video) return;

canvas.width = video.videoWidth;
canvas.height = video.videoHeight;

const ctx = canvas.getContext("2d");

ctx.drawImage(
video,
0,
0,
canvas.width,
canvas.height
);

const imageUrl =
canvas.toDataURL("image/png");

setSelectedImage({

preview:imageUrl,

file:dataURLtoFile(
imageUrl,
"camera-photo.png"
)

});

if(video.srcObject){

video.srcObject
.getTracks()
.forEach(track=>track.stop());

}

setShowCamera(false);

};
const dataURLtoFile = (
dataurl,
filename
)=>{

const arr =
dataurl.split(',');

const mime =
arr[0].match(/:(.*?);/)[1];

const bstr =
atob(arr[1]);

let n =
bstr.length;

const u8arr =
new Uint8Array(n);

while(n--){

u8arr[n] =
bstr.charCodeAt(n);

}

return new File(
[u8arr],
filename,
{type:mime}
);

};
const startAudioCall = ()=>{

    sendCallRequest(
        Number(selected.otherUserId),
        "AUDIO"
    );

    setCallType("audio");

    setShowCallModal(true);

};

const startVideoCall = ()=>{

    sendCallRequest(
        Number(selected.otherUserId),
        "VIDEO"
    );

    setCallType("video");

    setShowCallModal(true);

};
const handleDeleteForMe = async () => {

    await deleteForMe(
        selectedMessage.messageId
    );

    setShowDeleteModal(false);

    await loadChat(selected);
};
const handleDeleteForEveryone = async () => {

    await deleteForEveryone(
        selectedMessage.messageId
    );

    setShowDeleteModal(false);

    await loadChat(selected);
};
const handlePin = async () => {

    try {

        if (selectedMessage?.pinned) {

            await unpinMessage(
                selectedMessage.messageId
            );

        } else {

            await pinMessage(
                selectedMessage.messageId
            );

        }

        setContextMenu(null);

        await loadChat(selected);

    } catch (err) {

        console.log(err);

    }

};
const handleStar = async () => {

    if (!selectedMessage) return;

    try {

        if (selectedMessage.starred) {

            await unstarMessage(selectedMessage.messageId);

        } else {

            await starMessage(selectedMessage.messageId);

        }

        setMessages(prev =>
            prev.map(msg =>
                msg.messageId === selectedMessage.messageId
                    ? {
                          ...msg,
                          starred: !msg.starred
                      }
                    : msg
            )
        );

        setContextMenu(null);

    } catch (err) {

        console.log(err);

    }

};
const handleSelectMessage = (message) => {

    if (!selectionMode) return;

    setSelectedMessages(prev => {

        const exists = prev.includes(message.messageId);

        if (exists) {

            return prev.filter(
                id => id !== message.messageId
            );

        }

        return [
            ...prev,
            message.messageId
        ];

    });

};
// ================= COPY =================

const handleBulkCopy = async () => {

    const text = messages

        .filter(msg =>
            selectedMessages.includes(msg.messageId)
        )

        .map(msg => {

            if (msg.deletedForEveryone) {
                return "";
            }

            // IMAGE
            if (msg.mediaType === "IMAGE") {

                return `🖼 Image
http://localhost:9090${msg.mediaUrl}`;

            }

            // DOCUMENT
            if (msg.mediaType === "DOCUMENT") {

                return `📄 Document
http://localhost:9090${msg.mediaUrl}`;

            }

            // AUDIO
            if (msg.mediaType === "AUDIO") {

                return `🎵 Audio
http://localhost:9090${msg.mediaUrl}`;

            }

            // VOICE
            if (msg.mediaType === "VOICE") {

                return `🎤 Voice
http://localhost:9090${msg.mediaUrl}`;

            }

            // NORMAL TEXT
            return msg.content || "";

        })

        .filter(Boolean)

        .join("\n\n");

    await navigator.clipboard.writeText(text);

    alert("Messages copied successfully.");

    setSelectionMode(false);

    setSelectedMessages([]);

};
// ================= STAR =================

const handleBulkStar = async () => {

    try {

        const allStarred = messages
            .filter(msg =>
                selectedMessages.includes(msg.messageId)
            )
            .every(msg => msg.starred);

        for (const id of selectedMessages) {

            if (allStarred) {

                await unstarMessage(id);

            } else {

                await starMessage(id);

            }

        }

        setSelectionMode(false);
        setSelectedMessages([]);

        await loadChat(selected);

    } catch (err) {

        console.log(err);

    }

};
// ================= PIN =================

const handleBulkPin = async () => {

    try {

        const allPinned = messages
            .filter(msg =>
                selectedMessages.includes(msg.messageId)
            )
            .every(msg => msg.pinned);

        for (const id of selectedMessages) {

            if (allPinned) {

                await unpinMessage(id);

            } else {

                await pinMessage(id);

            }

        }

        setSelectionMode(false);
        setSelectedMessages([]);

        await loadChat(selected);

    } catch (err) {

        console.log(err);

    }

};

// ================= DELETE =================

const handleBulkDelete = async () => {

    try {

        for (const id of selectedMessages) {

            if (bulkDeleteForEveryone) {

                await deleteForEveryone(id);

            } else {

                await deleteForMe(id);

            }

        }

        setShowBulkDeleteModal(false);

        setSelectionMode(false);

        setSelectedMessages([]);

        await loadChat(selected);

    }
    catch (err) {

        console.log(err);

    }

};
// ================= FORWARD =================

const handleBulkForward = async () => {

    if (!showForwardModal) {

        setShowForwardModal(true);

        return;

    }

    if (!selectedForwardUser) {

        return;

    }

    try {

        for (const id of selectedMessages) {

            await forwardMessage(

                id,

                Number(
                    selectedForwardUser.otherUserId
                )

            );

        }

        setShowForwardModal(false);

        setSelectedForwardUser(null);

        setSelectionMode(false);

        setSelectedMessages([]);

        await loadConversations();

    }
    catch(err){

        console.log(err);

    }

};
const startRecording = async() => {

    try{

        const stream =
        await navigator.mediaDevices.getUserMedia({
            audio:true
        });

        const mediaRecorder =
        new MediaRecorder(stream);

        mediaRecorderRef.current =
        mediaRecorder;

        audioChunksRef.current = [];

        mediaRecorder.ondataavailable =
        (event)=>{

            audioChunksRef.current.push(
                event.data
            );

        };

        mediaRecorder.onstop = ()=>{

           const blob =
           new Blob(
               audioChunksRef.current,
               {
                   type:"audio/webm"
               }
           );

           setAudioBlob(blob);

           setAudioPreview(
               URL.createObjectURL(blob)
           );

        };

        mediaRecorder.start();

        setIsRecording(true);

    }
    catch(err){

        console.log(err);

    }

};
const stopRecording = ()=>{

    if(
        mediaRecorderRef.current
    ){

        mediaRecorderRef.current.stop();

    }

    setIsRecording(false);

};
const startCamera = async()=>{

try{

const stream =
await navigator.mediaDevices.getUserMedia({

video:true

});

if(videoRef.current){

videoRef.current.srcObject =
stream;

}

}
catch(err){

console.log(err);

}

};

const handleSend = async () => {
if (!isPremium) {
    setShowUpgradePopup(true);
    return;
}
    if (!newMessage.trim() || !selected) {
        return;
    }

    try {

        const response = await sendMessage({

            receiverId: Number(selected.otherUserId),

            content: newMessage,

            replyToMessageId: replyingTo?.messageId || null

        });

        console.log("SEND RESPONSE =", response);

        setNewMessage("");

        setReplyingTo(null);

        setShowEmojiPicker(false);

        // Reload conversation list
        await loadConversations();
        const latest = await getConversations();

        const chats = latest.data || [];

        setConversations(chats);

        const createdChat = chats.find(
            c => Number(c.otherUserId) === Number(selected.otherUserId)
        );

        if (createdChat) {

            setSelected(createdChat);

            await loadChat(createdChat);
            await loadChat(selected);
window.dispatchEvent(
    new Event("dashboardUpdated")
);

        }

    }
    catch (err) {

        console.log("SEND ERROR =", err);

    }

};
const menuItemClass = `
w-full
text-left
px-5
py-3
hover:bg-gray-100
transition-all
flex
items-center
gap-3
text-[16px]
`;
if (checkingPremium) {

    return null;

}
if (!isPremium && receiverId) {
    return (
        <>
            {showUpgradePopup && (
                <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-[999999]">
                    <div className="bg-white rounded-3xl p-8 w-[420px] text-center">

                        <div className="text-6xl mb-4">
                            👑
                        </div>

                        <h2 className="text-2xl font-bold mb-3">
                            Premium Required
                        </h2>

                        <p className="text-gray-600 mb-6">
                            Chat is available only for Premium members.
                        </p>

                        <div className="flex justify-center gap-3">

                            <button
                                onClick={() => navigate("/home")}
                                className="px-5 py-2 rounded-xl bg-gray-200"
                            >
                                Home
                            </button>

                            <button
                                onClick={() => navigate("/upgrade")}
                                className="px-5 py-2 rounded-xl bg-pink-600 text-white"
                            >
                                Upgrade Premium
                            </button>

                        </div>

                    </div>
                </div>
            )}
        </>
    );
}
return(

<div className="min-h-screen bg-gray-100 flex flex-col">



<div className="flex-1 p-4">

<div className="bg-white rounded-3xl shadow-xl overflow-hidden flex h-[85vh]">

{/* LEFT */}

<div className="w-[350px] border-r flex flex-col">

<div className="p-5">

<h1 className="text-4xl font-bold mb-5">

Messages

</h1>

<div className="relative">

<Search className="absolute left-4 top-4 h-5 w-5 text-gray-400"/>

<input

placeholder="Search..."

className="w-full border rounded-xl pl-12 py-3"

/>

</div>

</div>

<div className="overflow-y-auto flex-1">

{

conversations.map((chat)=>(

<button

key={chat.conversationId}

onClick={()=>{

setSelected(chat);

loadChat(chat);

}}

className={`

w-full
p-4
border-b
flex
gap-4

${

selected?.conversationId===chat.conversationId

?

"bg-pink-50"

:

""

}

`}

>

<div className="

w-14
h-14
rounded-full
bg-pink-200
flex
items-center
justify-center

">

{

(chat.otherUsername ||

chat.otherUserName ||

"U")

.charAt(0)

}

</div>

<div className="text-left flex-1">

<h3>

{

chat.otherUsername ||

chat.otherUserName

}

</h3>

<p className="text-gray-500">

{

chat.lastMessage ||

"No messages"

}

</p>

</div>

</button>

))

}

</div>

</div>


{/* RIGHT */}

<div className="flex-1 flex flex-col bg-[#f8f5f2]">

{

(selected || selected?.otherUserId) ?

(

<>

<div className="

bg-white
p-5
border-b
flex
items-center
gap-4

">

<div className="

w-14
h-14
rounded-full
bg-pink-200
flex
items-center
justify-center
font-bold
text-xl
shrink-0

">

{

(

selected.otherUsername ||

selected.otherUserName ||

"U"

)

.charAt(0)

.toUpperCase()

}

</div>


<div>

<h2 className="font-bold text-xl">
{selected?.otherUsername || selected?.otherUserName}
</h2>

{console.log("SELECTED USER =", selected)}

{



isTyping ? (

<p className="text-green-600 text-sm animate-pulse">

Typing...

</p>

) : selected?.isOnline ? (

<p className="text-green-600 text-sm font-medium">

🟢 Online

</p>

) : (

<p className="text-gray-500 text-sm">

{formatLastSeen(selected?.lastSeen)}

</p>

)



}


</div>
<div className="ml-auto flex gap-3">

<button
onClick={startAudioCall}
className="
p-2
rounded-full
hover:bg-gray-100
"
>
<Phone size={22}/>
</button>

<button
onClick={startVideoCall}
className="
p-2
rounded-full
hover:bg-gray-100
"
>
<Video size={22}/>
</button>

</div>

</div>



<div
    ref={chatContainerRef}
    className="flex-1 overflow-y-auto p-6 flex flex-col gap-4"
>

{

sortedMessages.map((message,index)=>{


if (
    message.deletedForUsers
        ?.split(",")
        .includes(
            String(currentUserId)
        )
) {
    return null;
}

const currentDate =
getDateLabel(
message.timestamp ||
message.createdAt
);

const previousDate =
index > 0
?
getDateLabel(
sortedMessages[index-1].timestamp ||
sortedMessages[index-1].createdAt
)
:
null;

const showDate =
currentDate !== previousDate;

return (
<React.Fragment key={message.messageId}>

{
showDate &&
(
<div
className="
mx-auto
my-4
bg-gray-200
text-gray-700
text-xs
px-4
py-1
rounded-full
w-fit
"
>
{currentDate}
</div>
)
}
<div
className={`
flex
items-center
gap-3

${
message.senderId===currentUserId
?
"justify-end"
:
"justify-start"
}
`}
>

{
selectionMode && (

<div
onClick={() =>
handleSelectMessage(message)
}
className="
w-7
h-7
border-2
border-gray-400
rounded-md
flex
items-center
justify-center
cursor-pointer
bg-white
shrink-0
"
>

{
selectedMessages.includes(message.messageId)
?
<span className="text-green-600 font-bold">
✓
</span>
:
null
}

</div>

)
}

<div

onClick={() => {

    if (selectionMode) {

        handleSelectMessage(message);

    }

}}

onContextMenu={(e)=>{

    e.preventDefault();



    setSelectedMessage(message);

    setContextMenu({

        x:e.clientX,

        y:e.clientY

    });

}}
className={`
relative
max-w-[60%]
border-2
px-4
py-3
rounded-2xl

${
message.senderId ===
currentUserId
?
"bg-pink-600 text-white"
:
"bg-white"
}
`}
>

    {
    message.pinned && (
        <div
            className="
            absolute
            -top-2
            -right-2
            bg-white
            rounded-full
            shadow
            w-5
            h-5
            flex
            items-center
            justify-center
            text-[11px]
            "
        >
            📌
        </div>
    )
    }
    {
    message.starred && (
        <span
            className="
            absolute
            -top-2
            left-2
            text-yellow-500 drop-shadow-sm
            text-sm
            "
        >
            ⭐
        </span>
    )
    }

{
message.deletedForEveryone ? (

    <p className="italic text-gray-500">
        This message was deleted
    </p>

) : (

    <>
        {
        message.mediaType === "IMAGE" ? (

            <img
                src={`http://localhost:9090${message.mediaUrl}`}
                alt="chat"
                onClick={() =>
                    setViewImage(
                        `http://localhost:9090${message.mediaUrl}`
                    )
                }
                className="
                max-w-[250px]
                max-h-[300px]
                rounded-xl
                object-cover
                cursor-pointer
                hover:scale-105
                transition
                "
            />

        ) : message.mediaType === "VIDEO" ? (

            <video
                controls
                onClick={() =>
                    setViewVideo(
                        `http://localhost:9090${message.mediaUrl}`
                    )
                }
                className="
                max-w-[300px]
                max-h-[350px]
                rounded-xl
                cursor-pointer
                hover:scale-105
                transition
                "
                src={`http://localhost:9090${message.mediaUrl}`}
            />

        ) : message.mediaType === "VOICE" ? (

            <audio
                controls
                className="w-[250px]"
                src={`http://localhost:9090${message.mediaUrl}`}
            />

        ) : message.mediaType === "AUDIO" ? (

            <a
                href={`http://localhost:9090${message.mediaUrl}`}
                target="_blank"
                rel="noreferrer"
                className="
                flex
                items-center
                gap-3
                bg-white
                text-black
                rounded-xl
                p-3
                w-[320px]
                "
            >

                <div className="text-5xl">
                    🎵
                </div>

                <div className="flex-1 overflow-hidden">

                    <p className="font-semibold truncate">
                        {message.mediaUrl?.split("/").pop()}
                    </p>

                    <p className="text-xs text-gray-500">
                        Audio File
                    </p>

                </div>

            </a>

        ) : message.mediaType === "DOCUMENT" ? (

            <a
                href={`http://localhost:9090${message.mediaUrl}`}
                target="_blank"
                rel="noreferrer"
                className="
                flex
                items-center
                gap-3
                bg-white
                text-black
                rounded-xl
                p-3
                w-[300px]
                "
            >

                <div className="text-5xl">
                    📄
                </div>

                <div className="flex-1 overflow-hidden">

                    <p className="font-semibold truncate">
                        {message.mediaUrl?.split("/").pop()}
                    </p>

                    <p className="text-xs text-gray-500">
                        Document
                    </p>

                </div>

            </a>

        ) : (

            <>
                {
                message.replyToContent && (

                    <div
                        className="
                        bg-black/10
                        border-l-4
                        border-green-500
                        rounded-md
                        px-3
                        py-2
                        mb-2
                        text-sm
                        "
                    >

                        <p className="font-semibold text-green-600">
                            Reply
                        </p>

                        <p className="truncate">
                            {message.replyToContent}
                        </p>

                    </div>

                )
                }

                <p>{message.content}</p>

            </>

        )
        }
    </>

)
}

<div className="

flex
justify-end
items-center
gap-1
text-xs
mt-1
opacity-70

">

<span>

{

message.timestamp ||
message.createdAt
?
new Date(
message.timestamp ||
message.createdAt
).toLocaleTimeString(

[],

{

hour:"2-digit",

minute:"2-digit"

}

)

:

""

}

</span>

{
message.senderId === currentUserId && (

<span>

{

message.status==="SEEN"

?

<span className="text-blue-400">

✓✓

</span>

:

message.status==="DELIVERED"

?

"✓✓"

:

"✓"

}

</span>

)

}

</div>

</div>

</div>

</React.Fragment>
);
})

}

</div>


<div className="relative bg-white border-t p-4 flex gap-3">
    <button
    onClick={()=>
    setShowEmojiPicker(
    !showEmojiPicker
    )
    }
    className="
    text-3xl
    px-2
    "
    >
    😊
    </button>

   <button
   onClick={(e)=>{

       e.stopPropagation();

       setShowAttachmentMenu(
           !showAttachmentMenu
       );

   }}
   className="
   text-3xl
   px-2
   "
   >
   ➕
   </button>
{
showEmojiPicker && (

<div
className="
absolute
bottom-20
left-5
z-50
"
>

<EmojiPicker
onEmojiClick={(emojiData)=>{

setNewMessage(
prev =>
prev +
emojiData.emoji
);



}}
/>

</div>

)
}
{
showAttachmentMenu && (

<div
ref={attachmentRef}
className="
absolute
bottom-20
left-16
bg-white
shadow-xl
rounded-2xl
border
p-2
z-50
w-56
"
>

<button
onClick={()=>{
    documentInputRef.current?.click();
}}
className="
w-full
text-left
p-3
hover:bg-gray-100
rounded-xl
"
>
📄 Document
</button>

<button
onClick={()=>{
    fileInputRef.current?.click();
}}
className="
w-full
text-left
p-3
hover:bg-gray-100
rounded-xl
"
>
🖼️ Photos & Videos
</button>

<button
onClick={() => {

    audioFileInputRef.current?.click();

}}
className="
w-full
text-left
p-3
hover:bg-gray-100
rounded-xl
"
>
🎵 Audio
</button>

<button
onClick={()=>{
    setShowCamera(true);
    setShowAttachmentMenu(false);
}}
className="
w-full
text-left
p-3
hover:bg-gray-100
rounded-xl
"
>
📷 Camera
</button>

</div>

)
}
<input
type="file"
accept="image/*,video/*"
ref={fileInputRef}
style={{display:"none"}}
onChange={(e)=>{

const file = e.target.files[0];

if(!file){
    return;
}

if(file.type.startsWith("video/")){

    setSelectedVideo({
        file,
        preview: URL.createObjectURL(file)
    });

}
else{

    setSelectedImage({
        file,
        preview: URL.createObjectURL(file)
    });

}

setShowAttachmentMenu(false);

}}
/>
<input
type="file"
ref={documentInputRef}
style={{display:"none"}}
accept=".pdf,.doc,.docx,.txt"
onChange={(e)=>{

    const file = e.target.files[0];

    if(!file){
        return;
    }

    setSelectedDocument(file);

    setShowAttachmentMenu(false);

}}
/>
<input
type="file"
accept="audio/*"
ref={audioFileInputRef}
style={{ display: "none" }}
onChange={(e) => {

    const file = e.target.files?.[0];

    if (!file) return;

    setSelectedAudioFile(file);

    setShowAttachmentMenu(false);

}}
/>
{
selectedImage && (

<div
className="
absolute
bottom-24
left-0
right-0
bg-white
border-t
p-4
shadow-lg
z-40
"
>

<div className="flex items-center gap-4">

<img
src={selectedImage.preview}
alt="preview"
className="
w-24
h-24
object-cover
rounded-xl
border
"
/>

<div className="flex gap-2">

<button
onClick={()=>{
    setSelectedImage(null);
}}
className="
px-4
py-2
bg-gray-200
rounded-lg
"
>
❌ Cancel
</button>

<button
onClick={async()=>{

try{

    if (!isPremium) {
        setShowUpgradePopup(true);
        return;
    }

    const uploadRes = await uploadImage(
        selectedImage.file
    );

console.log(
    "UPLOAD RESPONSE",
    uploadRes
);
await fetch(

"/api/chat/send-media",

{

method:"POST",

headers:{

"Content-Type":"application/json",

Authorization:
`Bearer ${localStorage.getItem("token")}`

},

body:JSON.stringify({

receiverId:Number(
selected.otherUserId
),

mediaUrl:uploadRes.url,

mediaType:"IMAGE"

})

}

);
setSelectedImage(null);

loadChat(selected);

}
catch(err){

console.log(err);

}

}}
className="
px-4
py-2
bg-pink-600
text-white
rounded-lg
"
>
📤 Send Image
</button>

</div>



</div>

</div>

)
}
{
selectedVideo && (

<div
className="
absolute
bottom-24
left-0
right-0
bg-white
border-t
p-4
shadow-lg
z-40
"
>

<div className="flex items-center gap-4">

<video
controls
src={selectedVideo.preview}
className="
w-40
h-28
rounded-xl
border
"
/>

<div className="flex gap-2">

<button
onClick={()=>{
setSelectedVideo(null);
}}
className="
px-4
py-2
bg-gray-200
rounded-lg
"
>
❌ Cancel
</button>

<button
onClick={async()=>{

try{

    if (!isPremium) {
        setShowUpgradePopup(true);
        return;
    }

    const uploadRes =
    await uploadVideo(
        selectedVideo.file
    );

console.log(
"VIDEO RESPONSE =",
uploadRes
);

await fetch(
"/api/chat/send-media",
{
method:"POST",

headers:{
"Content-Type":"application/json",
Authorization:
`Bearer ${localStorage.getItem("token")}`
},

body:JSON.stringify({

receiverId:Number(
selected.otherUserId
),

mediaUrl:uploadRes.url,

mediaType:"VIDEO"

})

}
);

setSelectedVideo(null);

await loadChat(selected);

}
catch(err){

console.log(
"VIDEO ERROR =",
err
);

}

}}
className="
px-4
py-2
bg-pink-600
text-white
rounded-lg
"
>
📤 Send Video
</button>

</div>

</div>

</div>

)
}


{
selectedDocument && (

<div
className="
absolute
bottom-24
left-0
right-0
bg-white
border-t
p-4
shadow-lg
z-40
"
>

<div className="flex items-center gap-4">

<div className="text-5xl">
📄
</div>

<div>
<p className="font-semibold">
{selectedDocument.name}
</p>

<p className="text-sm text-gray-500">
{(
selectedDocument.size /
1024
).toFixed(1)} KB
</p>
</div>

<button
onClick={()=>{
setSelectedDocument(null);
}}
className="
px-4
py-2
bg-gray-200
rounded-lg
"
>
❌ Cancel
</button>

<button
onClick={async () => {

    console.log("DOCUMENT SEND CLICKED");

    try {
if (!isPremium) {
    setShowUpgradePopup(true);
    return;
}
        const uploadRes = await uploadDocument(selectedDocument);

        await fetch(
            "/api/chat/send-media",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${localStorage.getItem("token")}`
                },
                body: JSON.stringify({
                    receiverId: Number(selected.otherUserId),
                    mediaUrl: uploadRes.url,
                    mediaType: "DOCUMENT"
                })
            }
        );

        setSelectedDocument(null);

        await loadChat(selected);

    } catch (err) {

        console.log(err);

    }

}}
className="
px-4
py-2
bg-pink-600
text-white
rounded-lg
"
>
📤 Send Document
</button>

</div>

</div>

)
}
{
selectedAudioFile && (

<div
className="
absolute
bottom-24
left-0
right-0
bg-white
border-t
p-4
shadow-lg
z-40
"
>

<div className="flex items-center gap-4">

<div className="text-5xl">
🎵
</div>

<div>

<p className="font-semibold">
{selectedAudioFile.name}
</p>

<p className="text-sm text-gray-500">
{(
selectedAudioFile.size /
1024 /
1024
).toFixed(2)} MB
</p>

</div>

<button
onClick={()=>{
setSelectedAudioFile(null);
}}
className="
px-4
py-2
bg-gray-200
rounded-lg
"
>
❌ Cancel
</button>

<button
onClick={async () => {

    try{
if (!isPremium) {
    setShowUpgradePopup(true);
    return;
}
        console.log("AUDIO FILE =", selectedAudioFile);

        const uploadResponse =
        await uploadAudio(
            selectedAudioFile
        );

        console.log(
            "UPLOAD RESPONSE =",
            uploadResponse
        );

        const audioUrl =
        uploadResponse.url;

        await fetch(
        "/api/chat/send-media",
        {
            method:"POST",

            headers:{
                "Content-Type":"application/json",
                Authorization:
                `Bearer ${localStorage.getItem("token")}`
            },

            body:JSON.stringify({

                receiverId:Number(
                    selected.otherUserId
                ),

                mediaUrl:audioUrl,

                mediaType:"AUDIO"

            })
        }
        );

        setSelectedAudioFile(null);

        await loadChat(selected);

    }
    catch(err){

        console.log(
            "AUDIO FILE ERROR =",
            err
        );

    }

}}
className="
px-4
py-2
bg-pink-600
text-white
rounded-lg
"
>
📤 Send Audio
</button>

</div>

</div>

)
}

{
showCamera && (

<div
className="
fixed
inset-0
bg-black/50
z-[9999]
flex
items-center
justify-center
"
>

<div
className="
bg-white
rounded-3xl
overflow-hidden
w-[700px]
max-w-[95vw]
"
>

<video
ref={videoRef}
autoPlay
playsInline
className="
w-full
h-[400px]
object-cover
"
/>

<canvas
ref={canvasRef}
style={{display:"none"}}
/>

<div
className="
p-4
flex
justify-center
gap-4
"
>

<button
onClick={()=>{
if(videoRef.current?.srcObject){

videoRef.current.srcObject
.getTracks()
.forEach(track=>track.stop());

}

setShowCamera(false);

}}
className="
px-6
py-3
bg-red-500
text-white
rounded-xl
"
>
❌ Cancel
</button>

<button
onClick={capturePhoto}
className="
px-6
py-3
bg-pink-600
text-white
rounded-xl
"
>
📸 Capture
</button>

</div>

</div>

</div>

)
}
{
capturedImage && (

<div>

<img
src={capturedImage}
alt=""
className="
w-24
h-24
"
/>

<button>
📤 Send Photo
</button>

</div>

)
}

{
audioPreview && (

<div
className="
absolute
bottom-24
left-0
right-0
bg-white
border-t
p-4
shadow-lg
z-40
"
>

<div className="flex items-center gap-4">

<audio
controls
src={audioPreview}
/>

<div className="flex gap-2">



<button
onClick={async () => {

    try{
if (!isPremium) {
    setShowUpgradePopup(true);
    return;
}
        const file = new File(
            [audioBlob],
            "voice.webm",
            {
                type:"audio/webm"
            }
        );

        const uploadResponse =
        await uploadAudio(file);

        const audioUrl =
        uploadResponse.url;

        await fetch(
        "/api/chat/send-media",
        {
            method:"POST",

            headers:{
                "Content-Type":"application/json",
                Authorization:
                `Bearer ${localStorage.getItem("token")}`
            },

            body:JSON.stringify({

                receiverId:Number(
                    selected.otherUserId
                ),

                mediaUrl:audioUrl,

                mediaType:"VOICE"

            })
        }
        );

        setAudioBlob(null);

        setAudioPreview(null);

       await loadChat(selected);

    }
    catch(err){

        console.log(err);

    }

}}
className="
px-4
py-2
bg-pink-600
text-white
rounded-lg
"
>
📤 Send Audio
</button>

</div>

</div>

</div>

)
}
{
replyingTo && (

<div className="reply-preview">

    <div className="reply-top">

        Replying to

        <button
            onClick={() =>
                setReplyingTo(null)
            }
        >
            ✕
        </button>

    </div>

    <div>

        {
            replyingTo.content
        }

    </div>

</div>

)
}
<input

value={newMessage}

onChange={(e)=>{

setNewMessage(

e.target.value

);

if(selected){

sendTyping(
    selected.otherUsername
);

}

clearTimeout(

typingTimeout.current

);

typingTimeout.current=

setTimeout(()=>{

if(selected){

stopTyping(
    selected.otherUsername
);

}

},2000);

}}
placeholder="Type message..."

className="flex-1 border rounded-full px-5 py-3"

/>
{
isRecording && (

<div
className="
absolute
bottom-20
right-5
bg-red-500
text-white
px-4
py-2
rounded-full
"
>

🔴 Recording...

</div>

)
}
<button
    onClick={handleSend}
    className="bg-pink-600 text-white px-6 rounded-full"
>
    {isPremium ? "Send" : "👑 Upgrade"}
</button>
<button
ref={micButtonRef}
onClick={(e) => {

    e.stopPropagation();

    if(isRecording){

        stopRecording();

    }
    else{

        startRecording();

    }

}}
className="
bg-gray-200
p-3
rounded-full
"
>
<Mic size={20}/>
</button>

</div>
{

showUpgradePopup && (



<div className="fixed inset-0 bg-black/50 flex items-center justify-center z-[999999]">



    <div className="bg-white rounded-3xl p-8 w-[420px] text-center">



        <div className="text-6xl mb-4">

            👑

        </div>



        <h2 className="text-2xl font-bold mb-3">

            Premium Required

        </h2>



        <p className="text-gray-600 mb-6">

            Sending messages is available only for Premium members.

            Upgrade your plan to continue chatting.

        </p>



      <div className="flex justify-center gap-3">

          <button
              onClick={() => {
                  setShowUpgradePopup(false);
                  navigate("/home");
              }}
              className="px-5 py-2 rounded-xl bg-gray-200"
          >
              Home
          </button>

          <button
              onClick={() => navigate("/upgrade")}
              className="px-5 py-2 rounded-xl bg-pink-600 text-white"
          >
              Upgrade Premium
          </button>

      </div>


    </div>



</div>



)

}

{
showCallModal && (

<div className="
fixed inset-0
bg-black/60
z-[9999]
flex
items-center
justify-center
">

<div className="
bg-white
rounded-3xl
p-8
w-[350px]
text-center
">

<div className="text-6xl mb-4">

{
callType === "audio"
?
"📞"
:
"📹"
}

</div>

<h2 className="text-2xl font-bold">
Calling...
</h2>
<p className="mt-3 text-gray-600 text-lg">
    {
        selected?.otherUsername ||
        selected?.otherUserName ||
        selected?.email ||
        "Unknown User"
    }
</p>

<button
onClick={()=>{
setShowCallModal(false);
}}
className="
mt-8
bg-red-500
text-white
px-8
py-3
rounded-full
"
>
End Call
</button>

</div>

</div>

)
}
{
incomingCall && (

<div className="
fixed inset-0
bg-black/60
z-[9999]
flex
items-center
justify-center
">

<div className="
bg-white
rounded-3xl
p-8
w-[350px]
text-center
">

<div className="text-6xl mb-4">

{
incomingCall.type === "VIDEO"
?
"📹"
:
"📞"
}

</div>

<h2 className="text-2xl font-bold">
Incoming Call
</h2>

<p className="mt-2">
{incomingCall.caller}
</p>

<div className="
flex
justify-center
gap-4
mt-8
">

<button
onClick={()=>{
setIncomingCall(null);
}}
className="
bg-red-500
text-white
px-6
py-3
rounded-full
"
>
Decline
</button>

<button
onClick={async()=>{

    try{
if (!isPremium) {
    setShowUpgradePopup(true);
    return;
}
        const stream =
        await navigator.mediaDevices.getUserMedia({

            audio:true

        });

        localStreamRef.current =
        stream;
        const peerConnection =
        new RTCPeerConnection({

            iceServers:[
                {
                    urls:"stun:stun.l.google.com:19302"
                }
            ]

        });

        peerConnectionRef.current =
        peerConnection;

        stream.getTracks().forEach(track=>{

            peerConnection.addTrack(
                track,
                stream
            );

        });
    const offer =
    await peerConnection.createOffer();

    await peerConnection.setLocalDescription(
        offer
    );
    alert("BEFORE SIGNAL");
    sendSignal({

        type:"offer",

        targetUserId:
            incomingCall.callerId,

        offer

    });

    alert("AFTER SIGNAL");

    console.log(
        "OFFER CREATED",
        offer
    );

    alert(
        "OFFER CREATED"
    );

        alert("PEER CONNECTION CREATED");

        alert("MIC CONNECTED");

    }
    catch(err){

        console.log(err);

    }

}}
className="
bg-green-500
text-white
px-6
py-3
rounded-full
"
>
Accept
</button>

</div>

</div>

</div>

)
}
{
contextMenu && selectedMessage && (

<div
className="
fixed
inset-0
z-[99999]
"
onClick={()=>{
setContextMenu(null);
}}
>

<div
className="
absolute
bg-white
rounded-2xl
shadow-[0_8px_30px_rgba(0,0,0,0.18)]
overflow-hidden
w-[300px]
"
style={{
left:
Math.min(
contextMenu.x,
window.innerWidth - 320
),

top:
Math.min(
contextMenu.y,
window.innerHeight - 420
)
}}
onClick={(e)=>{
e.stopPropagation();
}}
>

{/* <div */}
{/* className=" */}
{/* flex */}
{/* items-center */}
{/* justify-between */}
{/* px-4 */}
{/* py-3 */}
{/* border-b */}
{/* text-3xl */}
{/* " */}
{/* > */}
{/* 👍 ❤️ 😂 😮 😢 🙏 ➕ */}
{/* </div> */}

<div className="py-2">

<button
onClick={() => {

    setReplyingTo(selectedMessage);

    setContextMenu(null);

}}
className={menuItemClass}
>
↩ Reply
</button>

<button
onClick={handlePin}
className={menuItemClass}
>

{
selectedMessage?.pinned
?
"📌 Unpin"
:
"📌 Pin"
}

</button>

<div
    onClick={handleStar}
    className="
    px-4
    py-3
    hover:bg-gray-100
    cursor-pointer
    flex
    items-center
    gap-2
    "
>

<span>⭐</span>

<span>
{
selectedMessage?.starred
?
"Unstar"
:
"Star"
}
</span>

</div>

<button
onClick={() => {

    setSelectionMode(true);

    setSelectedMessages([
        selectedMessage.messageId
    ]);

    setContextMenu(null);

}}
className={menuItemClass}
>

☑ Select

</button>

<hr/>

<button
onClick={() => {

    setShowDeleteModal(true);

    setContextMenu(null);

}}
className="
w-full
text-left
px-5
py-4
text-red-600
hover:bg-red-50
"
>
🗑 Delete
</button>

</div>

</div>

</div>

)
}
{
selectionMode && (

<div
className="
fixed
bottom-0
left-0
right-0
bg-white
border-t
shadow-2xl
px-6
py-4
flex
items-center
justify-between
z-[99999]
"
>

<div
className="
flex
items-center
gap-5
"
>

<button
onClick={() => {

    setSelectionMode(false);

    setSelectedMessages([]);

}}
className="
text-3xl
hover:text-red-500
"
>
✕

</button>

<p
className="
font-bold
text-lg
"
>

{selectedMessages.length} Selected

</p>

</div>

<div
className="
flex
items-center
gap-6
text-2xl
"
>

<button
onClick={handleBulkCopy}
title="Copy"
className="
hover:scale-110
transition
"
>
📋
</button>

<button
onClick={handleBulkStar}
title="Star"
className="
hover:scale-110
transition
"
>
⭐
</button>

<button
onClick={handleBulkPin}
title="Pin"
className="
hover:scale-110
transition
"
>
📌
</button>

<button
onClick={() => {

    setBulkDeleteForEveryone(false);

    setShowBulkDeleteModal(true);

}}
title="Delete"
className="
hover:scale-110
transition
text-red-500
"
>
🗑️
</button>

<button
onClick={handleBulkForward}
title="Forward"
className="
hover:scale-110
transition
"
>
↪
</button>

</div>

</div>

)
}
{
showForwardModal && (

<div
className="
fixed
inset-0
bg-black/50
flex
items-center
justify-center
z-[99999]
"
>

<div
className="
bg-white
rounded-3xl
w-[430px]
max-h-[650px]
overflow-hidden
shadow-2xl
"
>

<div
className="
p-5
border-b
"
>

<h2
className="
text-2xl
font-bold
"
>
Forward Message
</h2>

<input

type="text"

placeholder="Search..."

value={forwardSearch}

onChange={(e)=>
setForwardSearch(
e.target.value
)
}

className="
mt-4
w-full
border
rounded-xl
px-4
py-3
outline-none
"

/>

</div>

<div
className="
max-h-[350px]
overflow-y-auto
"
>

{

conversations

.filter(chat=>{

const name =

(

chat.otherUsername ||

chat.otherUserName ||

""

)

.toLowerCase();

return name.includes(

forwardSearch.toLowerCase()

);

})

.map(chat=>(

<div

key={chat.conversationId}

onClick={()=>{

setSelectedForwardUser(chat);

}}

className={`

flex
items-center
gap-4
p-4
cursor-pointer
hover:bg-gray-100

${
selectedForwardUser
?.conversationId===chat.conversationId

?

"bg-pink-100"

:

""

}

`}

>

<div
className="
w-12
h-12
rounded-full
bg-pink-200
flex
items-center
justify-center
font-bold
"
>

{

(

chat.otherUsername ||

chat.otherUserName ||

"U"

)

.charAt(0)

}

</div>

<div>

<p
className="
font-semibold
"
>

{

chat.otherUsername ||

chat.otherUserName

}

</p>

</div>

</div>

))

}

</div>

<div
className="
border-t
p-4
flex
justify-end
gap-3
"
>

<button

onClick={()=>{

setShowForwardModal(false);

setSelectedForwardUser(null);

}}

className="
px-5
py-2
rounded-lg
bg-gray-200
"
>

Cancel

</button>

<button

onClick={handleBulkForward}

disabled={!selectedForwardUser}

className="
px-5
py-2
rounded-lg
bg-pink-600
text-white
disabled:opacity-50
"
>

Forward

</button>

</div>

</div>

</div>

)
}
{
showBulkDeleteModal && (

<div className="fixed inset-0 bg-black/40 flex items-center justify-center z-[99999]">

    <div className="bg-white rounded-xl p-6 w-[360px]">

        <h2 className="text-lg font-semibold mb-4">

            Delete selected messages?

        </h2>

        <label className="flex items-center gap-2 mb-3">

            <input

                type="checkbox"

                checked={bulkDeleteForEveryone}

                onChange={(e)=>
                    setBulkDeleteForEveryone(
                        e.target.checked
                    )
                }

            />

            Delete for Everyone

        </label>

        <div className="flex justify-end gap-3 mt-6">

            <button

                onClick={()=>
                    setShowBulkDeleteModal(false)
                }

                className="px-4 py-2 rounded"

            >

                Cancel

            </button>

            <button

                onClick={handleBulkDelete}

                className="bg-red-500 text-white px-4 py-2 rounded"

            >

                Delete

            </button>

        </div>

    </div>

</div>

)
}
{
showDeleteModal && (

<div
className="
fixed
inset-0
bg-black/50
z-[999999]
flex
items-center
justify-center
"
>

<div
className="
bg-white
rounded-3xl
w-[350px]
overflow-hidden
"
>

<div className="p-5 text-center">

<h3 className="font-bold text-xl">
Delete Message?
</h3>

</div>

<button
onClick={handleDeleteForMe}
className="
w-full
p-4
hover:bg-gray-100
"
>
Delete For Me
</button>

{
selectedMessage?.senderId === currentUserId
&&
(
<button
onClick={handleDeleteForEveryone}
className="
w-full
p-4
text-red-600
hover:bg-red-50
"
>
Delete For Everyone
</button>
)
}

<button
onClick={()=>
setShowDeleteModal(false)
}
className="
w-full
p-4
border-t
"
>
Cancel
</button>

</div>

</div>

)
}
{
viewVideo && (

<div
className="
fixed
inset-0
bg-black/95
z-[99999]
flex
items-center
justify-center
"
onClick={()=>{
setViewVideo(null);
}}
>

<button
onClick={()=>{
setViewVideo(null);
}}
className="
absolute
top-5
right-5
text-white
text-4xl
z-10
"
>
✕
</button>

<video
controls
autoPlay
className="
max-w-[95vw]
max-h-[95vh]
rounded-xl
"
src={viewVideo}
/>

</div>

)
}
{

viewImage && (

<div
className="
fixed
inset-0
bg-black/95
z-[99999]
flex
items-center
justify-center
"
onClick={()=>{
setViewImage(null);
}}
>

<button
onClick={()=>{
setViewImage(null);
}}
className="
absolute
top-5
right-5
text-white
text-4xl
"
>
✕
</button>

<img
src={viewImage}
alt="preview"
className="
max-w-[95vw]
max-h-[95vh]
object-contain
rounded-xl
"
/>

</div>

)
}
</>

)

:

(

<div className="flex-1 flex items-center justify-center">

Select Conversation

</div>

)

}

</div>

</div>

</div>

</div>

);

};

export default Messages;
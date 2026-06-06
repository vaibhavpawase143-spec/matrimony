import { useEffect,useState } from "react";
import { Search } from "lucide-react";

import Navbar from "@/components/Navbar";

import {

getConversations,
getMessages,
sendMessage

} from "@/services/chatApi";

const Messages=()=>{

const [conversations,setConversations]=useState([]);

const [selected,setSelected]=useState(null);

const [messages,setMessages]=useState([]);

const [newMessage,setNewMessage]=useState("");

useEffect(()=>{

loadConversations();

},[]);

useEffect(()=>{

if(!selected){

return;

}

const interval=setInterval(()=>{

loadChat(selected);

},2000);

return ()=>{

clearInterval(interval);

};

},[selected]);

const loadConversations=async()=>{

try{

const res=await getConversations();

const data=res.data || [];

setConversations(data);

if(data.length>0){

setSelected(data[0]);

loadChat(data[0]);

}

}

catch(err){

console.log(err);

}

};

const loadChat=async(chat)=>{

try{

const res=await getMessages(

Number(chat.otherUserId),

0,

100

);

setMessages(

res.data.content || []

);

}

catch(err){

console.log(err);

}

};

const handleSend=async()=>{

if(

!newMessage.trim()

||

!selected

){

return;

}

try{

await sendMessage({

receiverId:Number(

selected.otherUserId

),

content:newMessage,

replyToMessageId:null

});

setNewMessage("");

loadChat(selected);

}

catch(err){

console.log(err);

}

};

return(

<div className="min-h-screen bg-gray-100 flex flex-col">

<Navbar/>

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

selected ?

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

{

selected.otherUsername ||

selected.otherUserName

}

</h2>

<p className="text-sm text-green-600">

● Online

</p>

</div>

</div>


<div className="flex-1 overflow-y-auto p-6 flex flex-col gap-4">

{

messages.map((message)=>(

<div

key={message.messageId}

className={`

flex

${

message.senderId===

JSON.parse(

localStorage.getItem("user")

).id

?

"justify-end"

:

"justify-start"

}

`}

>

<div

className={`

max-w-[60%]
px-4
py-3
rounded-2xl

${

message.senderId===

JSON.parse(

localStorage.getItem("user")

).id

?

"bg-pink-600 text-white"

:

"bg-white"

}

`}

>

<p>

{message.content}

</p>

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

message.timestamp

?

new Date(

message.timestamp

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

message.senderId===

JSON.parse(

localStorage.getItem(

"user"

)

).id

&&

(

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

))

}

</div>


<div className="bg-white border-t p-4 flex gap-3">

<input

value={newMessage}

onChange={(e)=>{

setNewMessage(

e.target.value

);

}}

placeholder="Type message..."

className="flex-1 border rounded-full px-5 py-3"

/>

<button

onClick={handleSend}

className="

bg-pink-600
text-white
px-6
rounded-full

"

>

Send

</button>

</div>

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
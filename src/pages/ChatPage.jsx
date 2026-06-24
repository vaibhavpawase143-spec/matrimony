import { useEffect, useRef, useState } from "react";
import { useParams } from "react-router-dom";
import {
getMessages,
sendMessage,
getConversations
} from "../services/chatApi";

function ChatPage() {

    const {

        conversationId,

        receiverId

    } = useParams();

    console.log(

        "receiverId",

        receiverId

    );

    console.log(

        "conversationId",

        conversationId

    );

    const [messages, setMessages] = useState([]);

    const [text, setText] = useState("");

    const bottomRef = useRef(null);

    const [chatUser,setChatUser]=useState(null);

    const [userInfo, setUserInfo] = useState(null);

    const markSeen = async()=>{

    try{

    await fetch(

    `http://localhost:9090/api/chat/seen/${conversationId}`,

    {

    method:"PUT",

    headers:{

    Authorization:

    `Bearer ${
    localStorage.getItem("token")
    }`

    }

    }

    );

    }

    catch(err){

    console.log(err);

    }

    };

    const loadMessages = async () => {

        try {

            const res = await getMessages(

                Number(receiverId),

                0,

                100

            );

            console.log(

                "FULL RESPONSE",

                res.data

            );

            console.log(

            "MESSAGES",

            res.data.content

            );

            setMessages(

            res.data.content || []

            );

        }

        catch (error) {

            console.log(

                "LOAD ERROR",

                error

            );

            setMessages([]);

        }

    };
const loadUser = async()=>{

try{

const res = await getConversations();

const user = res.data.find(

c => String(c.otherUserId) === receiverId

);

setChatUser(user);
setUserInfo(user);
console.log("USER DATA", user);
}

catch(err){

console.log(err);

}

};

    useEffect(()=>{

    loadMessages();

    loadUser();

    markSeen();

    },[receiverId]);

    useEffect(() => {

//         const interval = setInterval(() => {
//
//             loadMessages();
//             loadUser();
//             markSeen();
//
//
//         }, 2000);

        return () => {

            clearInterval(interval);

        };

    }, [receiverId]);

//     useEffect(() => {
//
//         bottomRef.current?.scrollIntoView({
//
//             behavior: "smooth"
//
//         });
//
//     }, [messages]);

    const handleSend = async () => {

        if (!text.trim()) {

            return;

        }

        try {

            await sendMessage({

                receiverId: Number(receiverId),

                content: text,

                replyToMessageId:
                replyingTo?.messageId
                || null

            });

           setText("");

           loadMessages();
           markSeen();

        }

        catch (error) {

            console.log(

                "SEND ERROR",

                error

            );

        }

    };

    return (

        <div
            style={{
                padding: "20px",
                maxWidth: "1000px",
                margin: "0 auto"
            }}
        >

            <div

            style={{

            background:"white",

            padding:"20px",

            borderRadius:"20px 20px 0 0",

            display:"flex",

            alignItems:"center",

            gap:"15px",

            border:"1px solid #ddd",

            borderBottom:"none"

            }}

            >

            <div

            style={{

            width:"60px",

            height:"60px",

            borderRadius:"50%",

            background:"#fbcfe8",

            display:"flex",

            alignItems:"center",

            justifyContent:"center",

            fontSize:"24px",

            fontWeight:"bold"

            }}

            >

            {

            (

            chatUser?.otherUsername ||

            chatUser?.otherUserName ||

            "U"

            )

            .charAt(0)

            .toUpperCase()

            }

            </div>

            <div>

            <h3

            style={{

            margin:0

            }}

            >

            {

            chatUser?.otherUsername ||

            chatUser?.otherUserName ||

            "User"

            }

            </h3>

            {
                userInfo?.isOnline
                    ? (
                        <p style={{color:"green"}}>
                            ● Online
                        </p>
                    )
                    : (
                        <p style={{color:"#666"}}>
                            Offline
                        </p>
                    )
            }

            </div>

            </div>

            <div

                style={{

                    border: "1px solid #ddd",

                    borderRadius: "12px",

                    padding: "20px",

                    height: "70vh",

                    overflowY: "auto",

                    display: "flex",

                    flexDirection: "column",

                    background:"#f8f5f2",

                    marginBottom: "20px"

                }}

            >

                {

                    messages.map((message) => (

                    <div

                    key={message.messageId}

                    style={{

                    display:"flex",

                    justifyContent:

                    message.senderId === currentUserId

                    ?

                    "flex-end"

                    :

                    "flex-start",

                    marginBottom:"12px"

                    }}

                    >

                    <div

                    style={{

                    background:

                    message.senderId === currentUserId

                    ?

                    "#9333ea"

                    :

                    "#e5e7eb",

                    color:

                    message.senderId === currentUserId

                    ?

                    "white"

                    :

                    "black",

                    padding:"12px",

                    borderRadius:"15px",

                    maxWidth:"60%",

                    wordBreak:"break-word"

                    }}

                    >

                   <div>

                  <div>

                  <p>

                  {message.content}

                  </p>

                  <div

                  style={{

                  fontSize:"11px",

                  marginTop:"5px",

                  textAlign:"right",

                  opacity:0.7

                  }}

                  >

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

                  </div>

                  </div>

                   {

                   message.senderId ===

                   JSON.parse(

                   localStorage.getItem("user")

                   ).id

                   &&

                   (

                   <div

                   style={{

                   fontSize:"12px",

                   marginTop:"4px",

                   textAlign:"right",

                   opacity:0.8

                   }}

                   >

                   {

                   message.status==="SEEN"

                   ?

                   <span style={{color:"#60a5fa"}}>

                   ✓✓

                   </span>

                   :

                   message.status==="DELIVERED"

                   ?

                   "✓✓"

                   :

                   "✓"

                   }

                   </div>

                   )

                   }

                   </div>

                    </div>

                    </div>

                    ))
                }

                <div ref={bottomRef}></div>

            </div>

            <div

                style={{

                    display: "flex",

                    gap: "10px"

                }}

            >

                <input

                    value={text}

                    onChange={(e) => {

                        setText(

                            e.target.value

                        );

                    }}

                    placeholder="Type message..."

                    style={{

                        flex: 1,

                        padding: "14px",

                        borderRadius: "10px",

                        border: "1px solid #ccc"

                    }}

                />

                <button

                    onClick={handleSend}

                    style={{

                        padding: "14px 25px",

                        border: "none",

                        borderRadius: "10px",

                        background:"#db2777",

                        color: "white",

                        cursor: "pointer"

                    }}

                >

                    Send

                </button>

            </div>

        </div>

    );

}

export default ChatPage;
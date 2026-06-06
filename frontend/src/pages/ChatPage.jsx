import { useEffect, useRef, useState } from "react";
import { useParams } from "react-router-dom";
import { getMessages, sendMessage } from "../services/chatApi";

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

    useEffect(()=>{

    loadMessages();

    markSeen();

    },[receiverId]);

    useEffect(() => {

        const interval = setInterval(() => {

            loadMessages();

        }, 2000);

        return () => {

            clearInterval(interval);

        };

    }, [receiverId]);

    useEffect(() => {

        bottomRef.current?.scrollIntoView({

            behavior: "smooth"

        });

    }, [messages]);

    const handleSend = async () => {

        if (!text.trim()) {

            return;

        }

        try {

            await sendMessage({

                receiverId: Number(receiverId),

                content: text,

                replyToMessageId: null

            });

            setText("");

            loadMessages();

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

            <h2
                style={{
                    marginBottom: "20px"
                }}
            >

                Chat

            </h2>

            <div

                style={{

                    border: "1px solid #ddd",

                    borderRadius: "12px",

                    padding: "20px",

                    height: "70vh",

                    overflowY: "auto",

                    display: "flex",

                    flexDirection: "column",

                    background: "#fafafa",

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

                    message.senderId===

                    JSON.parse(

                    localStorage.getItem("user")

                    ).id

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

                    message.senderId===

                    JSON.parse(

                    localStorage.getItem("user")

                    ).id

                    ?

                    "#9333ea"

                    :

                    "#e5e7eb",

                    color:

                    message.senderId===

                    JSON.parse(

                    localStorage.getItem("user")

                    ).id

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

                        background: "#9333ea",

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
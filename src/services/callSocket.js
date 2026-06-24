

import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

let client = null;

export const connectCallSocket = (
    onCallReceived
) => {

    const token =
    localStorage.getItem("token");

    console.log(
        "CALL TOKEN =",
        token
    );

    const socket = new SockJS(
        `http://localhost:9090/ws?token=${token}`
    );

    client = new Client({

        webSocketFactory: () => socket,

        reconnectDelay: 5000,

        debug: (msg) => {

            console.log(
                "CALL STOMP:",
                msg
            );

        },
        debug: (msg) => {

            console.log(
                "CALL STOMP:",
                msg
            );

        },

        onConnect: () => {

            console.log("CALL SOCKET CONNECTED");

            // ================= CALLS =================

            client.subscribe(

                "/user/queue/calls",

                (message) => {

                    console.log(
                        "CALL MESSAGE RAW =",
                        message.body
                    );

                    const data = JSON.parse(message.body);

                    console.log(
                        "CALL RECEIVED",
                        data
                    );

                    onCallReceived(data);

                }

            );

            // ================= TYPING =================

            client.subscribe(

                "/user/queue/typing",

                (message) => {

                    const data = JSON.parse(message.body);

                    console.log(
                        "TYPING EVENT =",
                        data
                    );

                    // Next step la Messages.jsx la callback deu
                }

            );

            console.log(
                "SUBSCRIBED TO CALLS & TYPING"
            );

        },

        onStompError: (frame) => {

            console.log(
                "CALL STOMP ERROR =",
                frame
            );

        },

        onWebSocketError: (err) => {

            console.log(
                "CALL WS ERROR =",
                err
            );

        }

    });

    client.activate();

};

export const sendCallRequest = (
    receiverId,
    type
) => {

    console.log("CALL BUTTON CLICKED");

    console.log(
        "CLIENT CONNECTED =",
        client?.connected
    );

    console.log(
        "SENDING TO",
        receiverId
    );

    console.log(
        "TYPE =",
        type
    );

    if(
        !client ||
        !client.connected
    ){

        console.log(
            "CLIENT NOT CONNECTED"
        );

        return;
    }


    client.publish({

        destination:"/app/call",

        body:JSON.stringify({

            receiverId,

            type

        })

    });

    console.log(
        "PUBLISH DONE"
    );

};

export const disconnectCallSocket = () => {

    console.trace("SOCKET DISCONNECTED");

    if(client){

        client.deactivate();

        client = null;

    }

};
export const sendSignal = (data) => {

    console.log(
        "CLIENT CONNECTED ?",
        client?.connected
    );

    if (!client || !client.connected) {

        alert("SOCKET NOT CONNECTED");

        console.log(client);

        return;
    }

    client.publish({
        destination: "/app/signal",
        body: JSON.stringify(data)
    });

    console.log("SIGNAL PUBLISHED");
};
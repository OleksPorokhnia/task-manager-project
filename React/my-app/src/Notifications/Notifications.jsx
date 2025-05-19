import { useReducer, useRef, useEffect, useState} from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { useParams } from "react-router";


function Notifications(){

    const clientRef = useRef(null);
    const {id} = useParams();

    const [notification, setNotification] = useState();

     function conntecWebSocket(){
            const client = new Client({
                webSocketFactory: () => new SockJS(`http://localhost:8081/ws-notification`),
                connectHeaders: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
                debug: (str) => {
                    console.log(str);
                }
            });

            client.onConnect = () => {
                client.subscribe(`/topic/notification/${id}`, (message) => {
                    const recievedMessage = JSON.parse(message.body);
                    setNotification(message.body);
                })
            }
    
            client.activate();
    
            clientRef.current = client;
        }
    
        useEffect(() => {
            conntecWebSocket();
    
            return() => {
                clientRef.current?.deactivate();
            }
        })

        console.log("Recieved notification ", notification);
    return (
        <>
            {id}
            {notification}
        </>
    )
}

export default Notifications;
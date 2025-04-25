import { Fragment, useEffect, useState } from "react";
import {Client, StompConfig} from "@stomp/stompjs";
import SockJS from "sockjs-client";


function Greetings(){
    const [input, setInput] = useState("");
    const [message, setMessage] = useState([]);

    useEffect(() => {
        //const connect = new SockJS('http://localhost:8080/portfolio');
        const stopmClient = new Client({
            webSocketFactory: () => new SockJS('http://localhost:8080/portfolio'),
            debug: (str) => {   
                console.log(str);
            },
            onConnect: () => {
                console.log("Connected");

                stopmClient.subscribe("/topic/greetings", (message) => {
                    if(message.body){
                        setMessage(prev => [...prev, message.body]);
                    }
                })
            }
        });

        stopmClient.activate();

        return() => {
            stopmClient.deactivate();
        };
    }, []);

    const sendMessage = () => {
        const connect = SockJS("http://localhost:8080/portfolio");
        const stopmClient = new Client({
            webSocketFactory: () => connect,
        });

        stopmClient.onConnect = () => {
            stopmClient.publish({
                destination: "/app/greet",
                body: input
            });
            stopmClient.deactivate();
        }
        stopmClient.activate();
    }


    return (
        <Fragment>
            <h2>WebSocket Messages</h2>
            <input
                type="text"
                value={input}
                onChange={(e) => setInput(e.target.value)}
            />
            <button onClick={sendMessage}>Send</button>

            <ul>
                {message.map((msg, index) => (
                <li key={index}>{msg}</li>
                ))}
            </ul>
        </Fragment>
    )
}

export default Greetings;
import { Fragment, useEffect, useState , useRef} from "react";
import apiClient from "./api";
import { useParams } from "react-router";
import SockJS from "sockjs-client";
import {Client, StompConfig} from "@stomp/stompjs";


function UserSearching({project}){

    const[query, setQuery] = useState("");
    const[users, setUsers] = useState([]);

    const [checkedUsers, setCheckedUsers] = useState([]);

    var filteredUsers = users;

    const clientRef = useRef(null);

    const {id} = useParams();

    function connectWebSocket(){
        const client = new Client({
                    webSocketFactory: () => new SockJS(`http://localhost:8080/portfolio?token=${localStorage.getItem("token")}`),
                    connectHeaders: {
                        Authorization: `Bearer ${localStorage.getItem("token")}`,
                    },
                    debug: (str) => {
                        console.log(str);
                    }
                });

                client.onConnect = () => {
                    stompClient.subscribe(`/topic/project/${id}/update`, (message) => {
                        const newProj = JSON.parse(message.body);
                        setUsers(newProj.users);
                    })
                }
        
                client.activate();
        
                clientRef.current = client;
    }

    useEffect(() => {
        const delayBounced = setTimeout(() => {
            if(query.length >= 2){
                apiClient.get(`http://localhost:8080/api/user/search?username=${query}`,
                    {
                        headers: {
                            "Authorization" : `Bearer ${localStorage.getItem("token")}`
                        }
                    }
                )
                .then((res) => {
                    setUsers(res.data);
                    console.log(res);
                })
                .catch((err) => console.log("Error"));
            }else{
                setUsers([]);
            }
        }, 300);
    

        return () => clearTimeout(delayBounced);
    }, [query])

    useEffect(() => {
        connectWebSocket();

        return() => {
            clientRef.current?.deactivate();
        }
    }, [])



    function handleProjectUpdate(e){
        e.preventDefault();
        const finalProj = {
            ...project,
            users: checkedUsers
        }
        if(clientRef.current && clientRef.current.connected){
            clientRef.current.publish({
                destination: `/app/project/${id}/update`,
                body: JSON.stringify(finalProj),
            });
        }
    }

    function usersAddition(username){
        if(username == localStorage.getItem("username")){
            return;
        }

        setCheckedUsers((prev) => 
            prev.includes(username)
                ? prev.filter((name) => name != username)
                : [...prev, username]   
            )
    }
    console.log(project);

    return(
        <Fragment>
            <div>
                <input type="text" placeholder="Enter nickname" onChange={(e) => setQuery(e.target.value)}></input>
                <ul>
                    {
                        users
                        .filter(user => {
                            return !project.users.some(projectUser => projectUser.name === user.username)
                        })
                        .map((user) => (
                                <li style={{listStyle: "none"}} key={user.id}>
                                    <button onClick={() => usersAddition(user.username)}>{user.username != localStorage.getItem("username") ? user.username : user.username + " (you)"}</button>
                                </li>
                        ))
                    }
                </ul>
                    {
                        <div>
                            <div>
                                Now added users: 
                            </div>
                            <ul>
                                {
                                    checkedUsers.map((user) => (
                                        <li style={{listStyle: "none"}}>
                                            {user}
                                            <button onClick={() => usersAddition(user)}>Delete</button>
                                        </li>
                                    ))
                                }
                            </ul>
                        </div>
                    }

                    <button onClick={handleProjectUpdate}>Add all selected users</button>
            </div>
        </Fragment>
    )
}

export default UserSearching;
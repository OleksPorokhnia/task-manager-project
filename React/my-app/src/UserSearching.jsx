import { Fragment, useEffect, useState , useRef} from "react";
import apiClient from "./api";
import { useParams } from "react-router";
import SockJS from "sockjs-client";
import {Client, StompConfig} from "@stomp/stompjs";
import "./styles/userSearchingStyles.css"


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
        console.log("Project", project.users);
        const users = project.users.map(user => user.name);
        console.log("Users usernames", users)
        const finalProj = {
            ...project,
            users: [...users, ...checkedUsers]
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
                <div className="input-group">
                    <div className="form-floating">
                        <input type="text" className=" form-control" id="usernameForm" placeholder="Enter nickname" onChange={(e) => setQuery(e.target.value)}></input>
                        <label for="usernameForm">Enter username</label>
                    </div>
                </div>
                <div className="mt-2 ms-1" style={{maxHeight: 250}}>
                    {
                        users
                        .filter(user => {
                            return !project.users.some(projectUser => projectUser.name === user.username)
                        })
                        .map((user) => (
                                <div style={{listStyle: "none"}} key={user.id}>
                                    <div className="users mb-1" onClick={() => usersAddition(user.username)}>{user.username != localStorage.getItem("username") ? user.username : user.username + " (you)"}</div>
                                    <div className="border-bottom border-2 wh-100"></div>
                                </div>
                        ))
                    }
                </div>
                    {
                        <div className="mt-3">
                            <div className="mb-2">
                                Now added users: 
                            </div>
                            <div>
                                {
                                    checkedUsers.map((user) => (
                                        <div className="d-flex justify-content-between align-content-center" style={{listStyle: "none"}}>
                                            <div className="ms-2 mt-2">{user}</div>
                                            <button className="btn btn-danger me-3" onClick={() => usersAddition(user)}>Delete</button>
                                        </div>
                                    ))
                                }
                            </div>
                        </div>
                    }
                    {checkedUsers.length !== 0 && 
                        <button className="btn btn-success mt-4 w-100" onClick={handleProjectUpdate}>Add all selected users</button>
                    }
            </div>
        </Fragment>
    )
}

export default UserSearching;
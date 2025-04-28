import { Fragment, useEffect, useRef, useState } from "react";
import { useNavigate, useOutletContext, useParams } from "react-router";
import { marked } from 'marked'
import apiClient from "./api";
import SockJS from "sockjs-client";
import {Client, StompConfig} from "@stomp/stompjs";

function TaskModal({project, realTasks}){
    const {id, taskId} = useParams();
    const navigate = useNavigate();
    const [edited, setEdited] = useState();

    const clientRef = useRef(null);

    const task = realTasks.find(t => t.id === parseInt(taskId));

    const [editedTask, setEditedTask] = useState(task);

    console.log(task);


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
                client.subscribe("/user/queue/errors", (errors) => {
                    setError(errors.body);
                })
            };
    
            client.activate();

            clientRef.current = client;
    }

    useEffect(() => {
        connectWebSocket();

        return () => {
            clientRef.current?.deactivate();
        }
    })

    const editTask = (e) => {
        e.preventDefault();
        setEdited(!edited)
    };

    async function updateTask(e){
        e.preventDefault();
        const finalTask = {
            ...editedTask,
            creatorUsername: localStorage.getItem("username")
        }
        if(clientRef.current && clientRef.current.connected){
            clientRef.current.publish({
                destination: `/app/project/${id}/task/${taskId}/update`,
                body: JSON.stringify(finalTask),
            });
        }
    }

    const handleChange = (e) => {
        e.preventDefault();
        const {name, value} = e.target;
        console.log("Status: " + name + " Value: " + value);
        setEditedTask(prevTask => ({
            ...prevTask,
            [name]: value
        }))
        console.log(editedTask);
    }

    if(!task){
        return (<div>Loading...</div>)
    }

    console.log(task.creatorUsername);

    return(
        <Fragment>
            {edited === true ? (
            <form className="d-flex flex-column w-100" onSubmit={updateTask}>
                <div className="input-group mb-3 border-1 rounded">
                    <div className="fw-bold mb-1">
                        Title
                    </div>
                    <div className="flex-nowrap input-group mb-3 border-1 rounded">
                        <i className="bi bi-pencil-square input-group-text"></i>
                        <input className="w-100 form-control" type="text" name="title" value={editedTask.title} placeholder="Enter the title here..." onChange={handleChange}></input>
                    </div>
                </div>
                <div className="input-group mb-3">
                    <div className="fw-bold mb-1">
                        Discription
                    </div>
                    <div className="input-group mb-3">
                        <div className="input-group-prepend">
                            <span className="input-group-text"><i class="bi bi-pencil"></i></span>
                        </div>
                            <textarea className="form-control" name="description" value={editedTask.description} placeholder="Enter description here..." onChange={handleChange} style={{height: 200,resize: "none"}}></textarea>
                    </div>
                </div>
                <div className="input-group mb-3 d-flex flex-column">
                    <div className="mb-1 fw-bold">
                        Deadline
                    </div>
                    <input className="border-1 rounded w-100 ps-3" type ="date" value={editedTask.deadline} name="deadline" style={{height: 40}} onChange={handleChange}></input>
                </div>
                <div className="mb-3 d-flex justify-content-around mb-4">
                    <div className="d-flex flex-column">
                        <div className="fw-bold"> 
                            Status
                        </div>
                        <div className="dropdown">
                            {editedTask.status === "TODO" &&
                            <button className="btn btn-secondary dropdown-toggle mb-1" data-bs-toggle="dropdown" aria-expanded="false" type="button">
                                {editedTask.status}
                            </button>
                            }
                            {editedTask.status === "IN_PROGRESS" &&
                            <button className="btn btn-warning dropdown-toggle mb-1" data-bs-toggle="dropdown" aria-expanded="false" type="button">
                                {editedTask.status}
                            </button>
                            }
                            {editedTask.status === "DONE" &&
                            <button className="btn btn-success dropdown-toggle mb-1" data-bs-toggle="dropdown" aria-expanded="false" type="button">
                                {editedTask.status}
                            </button>
                            }
                           {editedTask.status == null &&
                            <button className="btn btn-secondary dropdown-toggle mb-1" data-bs-toggle="dropdown" aria-expanded="false" type="button">
                                TO DO
                            </button>
                            }
                            <ul className="dropdown-menu">
                                <li className="ms-3"><button className="dropdown-item" type="button" name="status" value="TODO" onClick={handleChange}>TO DO</button></li>
                                <li className="ms-3"><button className="dropdown-item" type="button" name="status" value="IN_PROGRESS" onClick={handleChange}>IN PROGRESS</button></li>
                                <li className="ms-3"><button className="dropdown-item" type="button" name="status" value="DONE" onClick={handleChange}>DONE</button></li>
                            </ul>
                        </div>
                    </div>
                    <div className="d-flex flex-column">
                        <div className="fw-bold mb-1">
                            Priority(1-10)
                        </div>
                        <div className="d-flex input-group flex-nowrap border-1 rounded">
                            <i className="bi bi-pencil-square input-group-text"></i>
                            <input className="w-100 form-control" type="number" value={editedTask.priority} min="1" max="10" name="priority" placeholder="Enter status here..." onChange={handleChange}></input>
                        </div>
                    </div>
                    <div>
                        <div className="fw-bold"> 
                             Contributor
                        </div>
                        <div className="dropdown">
                            <button className="btn btn-success dropdown-toggle mb-1" data-bs-toggle="dropdown" aria-expanded="false" type="button">
                                {editedTask.user}
                            </button>
                            <ul className="dropdown-menu">
                                {
                                    project.users.map(user => (
                                        <li> <li className="ms-3"><button className="dropdown-item" type="button" name="user" value={user.name} onClick={handleChange}>{user.name}</button></li></li>
                                    ))
                                }
                            </ul>
                        </div>
                    </div>
                </div>
                <button type="submit" className="btn btn-success">Edit</button>
            </form>
            ) : (
                <form className="d-flex flex-column w-100" onSubmit={editTask}>
                <div className="input-group mb-3 d-flex flex-column  border-bottom border-1">
                    <div className="fw-bold mb-1">
                        Description
                    </div>
                    <div className="mb-3">
                        <div dangerouslySetInnerHTML={{__html: marked(task.description)}}></div>
                    </div>
                </div>
                <div className="input-group mb-3 d-flex justify-content-between">
                    <div className="mb-1 fw-bold">
                        Deadline
                    </div>
                    <div>{task.deadline}</div>
                </div>
                <div className="mb-3 d-flex justify-content-between mb-4">
                    <div className="d-flex flex-column">
                        <div className="fw-bold"> 
                            Status
                        </div>
                        {task.status === "TODO" &&
                            <div className="btn btn-secondary mb-1">
                                {task.status}
                            </div>
                        }
                        {task.status === "IN PROGRESS" &&
                            <div className="btn btn-warning mb-1">
                                {task.status}
                            </div>
                        }
                        {task.status === "DONE" &&
                            <div className="btn btn-success mb-1">
                                {task.status}
                            </div>
                        }
                        {task.status == null &&
                            <div className="btn btn-secondary mb-1">
                                TO DO
                            </div>
                        }
                    </div>
                    <div className="d-flex flex-column">
                        <div className="fw-bold mb-1">
                            Priority(1-10)
                        </div>
                        <div className="d-flex input-group flex-nowrap border-1 rounded">
                            <i className="bi bi-pencil-square input-group-text"></i>
                            <span className="w-100 form-control d-flex justify-content-center" type="number" min="1" max="10" name="priority" placeholder="Enter status here..." onChange={handleChange}>
                                {task.priority}
                            </span>
                        </div>
                    </div>
                    <div>
                        <div className="fw-bold"> 
                             Contributor
                        </div>
                        <div className="">
                            {task.user != null ? task.user : "NONE"}
                        </div>
                    </div>
                </div>
                {(localStorage.getItem("username") == task.creatorUsername) ? <button type="submit" className="btn btn-success">Edit</button> : <div></div>}
            </form>
            )}
        </Fragment>
    )
}

export default TaskModal;
import { Fragment, useEffect, useRef, useState } from "react";
import { useLocation, useNavigate, useOutletContext, useParams } from "react-router";
import apiClient from "./api";
import SockJS from "sockjs-client";
import {Client, StompConfig} from "@stomp/stompjs";

function AddTask({project}){
    const navigate = useNavigate();
    const [task, setTask] = useState({});
    const {id} = useParams();
    const [error, setError] = useState([]);

    const [status, setStatus] = useState();

    const clientRef = useRef(null);

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
    }, [])

    async function addNewTask(e){
        e.preventDefault();
        console.log("Errors: " + error)

        const finalTask = {
            ...task,
            creatorUsername: localStorage.getItem("username")
        }
        if(clientRef.current && clientRef.current.connected){
            clientRef.current.publish({
                destination: `/app/project/${id}/task/add`,
                body: JSON.stringify(finalTask),
            })
        }
    }

    const close = () => {
        navigate(-1)
    }

    const handleChange = (e) => {
        const {name, value} = e.target;
        setTask(prevTask => ({
            ...prevTask,
            [name]: value
        }))
        console.log(task);
    }

    return (
        <Fragment>
            
            <form className="d-flex flex-column w-100" onSubmit={addNewTask}>
                <div className="input-group mb-3 border-1 rounded">
                    <div className="fw-bold mb-1">
                        Title
                    </div>
                    <div className="flex-nowrap input-group mb-3 border-1 rounded">
                        <i className="bi bi-pencil-square input-group-text"></i>
                        <input className="w-100 form-control" type="text" name="title" placeholder="Enter the title here..." onChange={handleChange}></input>
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
                            <textarea className="form-control" name="description" placeholder="Enter description here..." onChange={handleChange} style={{height: 200,resize: "none"}}></textarea>
                    </div>
                </div>
                <div className="input-group mb-3 d-flex flex-column">
                    <div className="mb-1 fw-bold">
                        Deadline
                    </div>
                    <input className="border-1 rounded w-100 ps-3" type ="date" name="deadline" style={{height: 40}} onChange={handleChange}></input>
                </div>
                <div className="d-flex justify-content-between mb-4">
                    <div className="d-flex flex-column">
                        <div className="fw-bold"> 
                            Status
                        </div>
                        <div className="dropdown" name="status" onClick={handleChange}>
                            {task.status === "TODO" &&
                            <button className="btn btn-secondary dropdown-toggle mb-1" data-bs-toggle="dropdown" aria-expanded="false" type="button">
                                TODO
                            </button>
                            }
                            {task.status === "IN PROGRESS" &&
                            <button className="btn btn-warning dropdown-toggle mb-1" data-bs-toggle="dropdown" aria-expanded="false" type="button">
                                IN PROGRESS
                            </button>
                            }
                            {task.status === "DONE" &&
                            <button className="btn btn-success dropdown-toggle mb-1" data-bs-toggle="dropdown" aria-expanded="false" type="button">
                                DONE
                            </button>
                            }
                           {task.status == null &&
                            <button className="btn btn-secondary dropdown-toggle mb-1" data-bs-toggle="dropdown" aria-expanded="false" type="button">
                                TODO
                            </button>
                            }
                            <ul className="dropdown-menu">
                                <li className="ms-3"><button className="dropdown-item" type="button" name="status" value="TODO" onClick={handleChange}>TO DO</button></li>
                                <li className="ms-3"><button className="dropdown-item" type="button" name="status" value="IN PROGRESS" onClick={handleChange}>IN PROGRESS</button></li>
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
                            <input className="w-100 form-control" type="number" min="1" max="10" name="priority" placeholder="Enter status here..." onChange={handleChange}></input>
                        </div>
                    </div>
                    <div>
                        <div className="fw-bold"> 
                             Contributor
                        </div>
                        <div className="dropdown">
                            <button className="btn btn-success dropdown-toggle mb-1" data-bs-toggle="dropdown" aria-expanded="false" type="button">
                                {task.userUsername}
                            </button>
                            <ul className="dropdown-menu">
                                {
                                    project.users.map(user => (
                                        <li> <li className="ms-3"><button className="dropdown-item" type="button" name="userUsername" value={user.name} onClick={handleChange}>{user.name}</button></li></li>
                                    ))
                                }
                            </ul>
                        </div>
                    </div>
                </div>
                <button type="submit" className="btn btn-success">Add task</button>
            </form>
        </Fragment>
    )
}

export default AddTask;
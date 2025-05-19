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

    const [success, setSuccess] = useState("");

    const clientRef = useRef(null);

    const naviaget = useNavigate();

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
                const parsedErrors = JSON.parse(errors.body);
                if (Array.isArray(parsedErrors)) {
                    setError(parsedErrors); 
                } else {
                    setError([parsedErrors]);
                }
            })

            client.subscribe("/user/queue/success", (message) => {
                const success = JSON.parse(message.body);
                setSuccess(success);

            })

            client.publish({
                destination: "/app/client-ready",
                body: JSON.stringify({ status: "ready" }),
            });
        };

        client.activate();

        clientRef.current = client;
    }


    useEffect(() => {
        connectWebSocket();

        return () => {
            clientRef.current?.deactivate();
        }
    }, [error])

    async function addNewTask(e){
        e.preventDefault();
        setError([]);
        setSuccess("");
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
        console.log("Errors", error);
        console.log(success.message);
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
            {
                success && 
                <div className="alert alert-success">
                    {success.message}
                </div>
            }
            <form className="d-flex flex-column w-100" onSubmit={addNewTask}>
                <div className="input-group mb-3 has-validation border-1 rounded">
                    <div className="fw-bold mb-1">
                        Title
                    </div>
                    <div className="input-group mb-3 border-1">
                        <i className={`bi bi-pencil-square input-group-text`}></i>
                        <div className={`form-floating ${error.filter(err => (
                                        err.field === 'title')) ? 'is-invalid' : '' }`}>
                            <input className={`w-100 form-control ${error.some(err => (
                                        err.field === 'title')) ? 'is-invalid' : '' }`} type="text" name="title" id="floatingTitle" placeholder="Enter the title here..." onChange={handleChange}></input>
                            <label for="floatingTitle">Enter title</label>
                        </div>
                        {
                                (error.length > 0 && 
                                    error.filter(err => (
                                        err.field === 'title'))
                                        .map((err, index) => (
                                            <div key={index} className="invalid-feedback" style={{marginLeft: 40}}>
                                                {err.message}
                                            </div>
                                        ))
                                )
                            }
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
                        <div className="form-floating">
                            <textarea className={`w-100 form-control ${error.some(err => (
                                        err.field === 'description')) ? 'is-invalid' : '' }`} id="floatingDescription" name="description" placeholder="Enter description here..." onChange={handleChange} style={{height: 200,resize: "none"}}></textarea>
                            <label for="floatingDescription">Enter description</label>
                            {
                                (error.length > 0 && 
                                    error.filter(err => (
                                        err.field === 'description'))
                                        .map((err, index) => (
                                            <div key={index} className="invalid-feedback">
                                                {err.message}
                                            </div>
                                        ))
                                )
                            }
                        </div>
                    </div>
                </div>
                <div className="input-group mb-3 d-flex flex-column">
                    <div className="mb-1 fw-bold">
                        Deadline
                    </div>
                    <div className="input-group">
                        <div className="form-floating">
                            <input className={`w-100 form-control rounded ps-3 pb-4 ${error.some(err => (
                                        err.field === 'deadline')) ? 'is-invalid' : '' }`} type ="date" id="floatingDeadline" name="deadline" style={{height: 40}} onChange={handleChange}></input>
                            {
                                (error.length > 0 && 
                                    error.filter(err => (
                                        err.field === 'deadline'))
                                        .map((err, index) => (
                                            <div key={index} className="invalid-feedback">
                                                {err.message}
                                            </div>
                                        ))
                                )
                            }
                        </div>
                    </div>
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
                                <li className="ms-3"><button className="dropdown-item" type="button" name="status" value="TODO" onClick={handleChange}>TODO</button></li>
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
                                {task.userUsername == null && "Add new"}
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
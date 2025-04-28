import { Fragment, useState} from "react";
import { Outlet, useNavigate, useParams, useLocation, Link } from "react-router"
import apiClient from "./api";
import { useEffect } from "react";
import Header from "./NavigationComponents/Header";
import BurgerMenu from "./NavigationComponents/BurgerMenu";
import {Modal} from 'react-bootstrap'
import AddTask from "./AddTAsk";
import { marked } from 'marked'
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { all } from "axios";
import TaskModal from "./taskModal";



function ProjectView() {
    const {id, taskId} = useParams();
    const [proj, setProj] = useState({});
    const [tasks, setTasks] = useState([]);
    const navigate = useNavigate();

    const [user, setUser] = useState();

    const [task, setTask] = useState({});

    const [checkedTask, setCheckedTask] = useState([]);

    const location = useLocation();

    //const isTaskOpen = location.pathname.includes("/task");

    const showModal = location.pathname.endsWith("/task");

    const showTaskModal = location.pathname.includes(`/task/`);

    const handleClose = () => {
        navigate(`/project/${id}`);
    };

    useEffect(() => {
        const stompClient = new Client({
            webSocketFactory: () => new SockJS(`http://localhost:8080/portfolio?token=${localStorage.getItem("token")}`),
            connectHeaders: {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
            debug: (str) => {
                console.log(str);
            },
            onConnect: () => {
                console.log("Connected");

                stompClient.subscribe(`/topic/project/${id}/tasks`, (message) => {
                    const allTasks = JSON.parse(message.body);
                    console.log("All tasks: " + allTasks)
                    setTasks(allTasks);
                });

                stompClient.subscribe(`/topic/project/${id}/task/add`, (message) => {
                    const newTask = JSON.parse(message.body);
                    setTasks(prevTasks => [...prevTasks, newTask]);
                });

                stompClient.subscribe(`/topic/project/${id}/task/update`, (message) => {
                    const newTask = JSON.parse(message.body);
                    console.log("New task " + newTask)
                    setTasks(prevItems => 
                        prevItems.map((item) => (item.id === newTask.id ? newTask : item))
                    )
                })

                stompClient.subscribe(`/topic/project/${id}/task/delete`, (message) => {
                    const deletedIds = JSON.parse(message.body);
                    console.log(deletedIds)
                    setTasks(prevItems => 
                        prevItems.filter(task => !deletedIds.includes(task.id))
                    )
                })

                stompClient.subscribe("/user/queue/current", (message) => {
                    setUser(message.body);
                    localStorage.setItem("username", message.body)
                })
                stompClient.subscribe(`/topic/project/${id}/update`, (message) => {
                    const newProj = JSON.parse(message.body);
                    setProj(newProj);
                })
                stompClient.publish({
                    destination: `/app/project/${id}/tasks`,
                    body: ""
                })
                stompClient.publish({
                    destination: "/app/project/getCurrentUser",
                    body: ""
                })
            }
        });

        stompClient.activate();

        return ()=>{
            stompClient.deactivate();
        }
    }, [id]);

    console.log("Current user: " + user);

    useEffect(() => {
        console.log("Current tasks state:", tasks);
    }, [tasks]);

    useEffect(() => {
        apiClient.get(`/project/${id}`, {
            headers: {
                "Authorization" : `Bearer ${localStorage.getItem("token")}`
            }
        }).then(resp => {
            setProj(resp.data);
            console.log("Project title " + resp.data.title);
        })
    }, [])

    const getTask = (taskId) => {
        const task = tasks.find(t => t.id === parseInt(taskId));
        setTask(task);
        navigate(`/project/${id}/task/${taskId}`);
    }


    const addToList = (taskId) => {
        setCheckedTask((prev) => 
            prev.includes(taskId)
                ? prev.filter((id) => id != taskId)
                : [...prev, taskId]        
    );
    console.log("All cheked tasks" + checkedTask);
    };

    return(
        <Fragment>
            <Header />
            <div className="d-flex">
                <BurgerMenu project={proj} checkedTasks={checkedTask}/>
                <div className="ms-5 w-100">
                    <table className="table">
                        <thead>
                            <tr>
                                <th scope="col"></th>
                                <th scope="col">Title</th>
                                <th scope="col">Priority</th>
                                <th scope="col">Task status</th>
                                <th scope="col">Creator</th>
                            </tr>
                        </thead>
                        <tbody>
                            {Array.isArray(tasks) && tasks.map(task => (
                                <tr key={task.id} onClick={(e) => {
                                        if (e.target.tagName === "INPUT") return;
                                        getTask(task.id)}}>
                                    <th><input className={`${(task.status === "DONE") ? "bg-light text-muted" : ""}`} disabled={(task.status === "DONE" || task.creatorUsername != localStorage.getItem("username"))} type="checkbox" checked={checkedTask.includes(task.id)} onChange={(e) => {
                                        addToList(task.id)}}></input>
                                    </th>
                                    <td className={task.status === "DONE" ? "bg-light text-muted" : ""}>{task.title}</td>
                                    <td className={task.status === "DONE" ? "bg-light text-muted" : ""}>{task.priority > 0 ? task.priority : 0}</td>
                                    <td className={task.status === "DONE" ? "bg-light text-muted" : ""}>{task.status}</td>
                                    <td className={task.status === "DONE" ? "bg-light text-muted" : ""}>{task.creatorUsername != null ? task.creatorUsername : "None"}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>

                    <Modal show={(showModal)} onHide={handleClose} centered>
                            <Modal.Header closeButton>
                                <Modal.Title>
                                    Create new Task
                                </Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                                <AddTask />
                            </Modal.Body>
                    </Modal>

                    <Modal show={showTaskModal} onHide={handleClose} centered>
                            <Modal.Header closeButton>
                                <Modal.Title>
                                    {task != null ? <div dangerouslySetInnerHTML={{__html: marked("# " + task.title)}}></div> : <div></div>}
                                </Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                                <TaskModal project = {proj} realTasks={tasks}/>
                            </Modal.Body>
                    </Modal>
                </div>
            </div>
        </Fragment>
    )
}

export default ProjectView
import { Fragment , useEffect, useState, useRef} from "react"
import {useNavigate, Outlet, useParams, useOutletContext } from "react-router";
import AddTask from "../AddTAsk";
import {Modal} from 'react-bootstrap'
import "./navigationStyles/burgerMenu.css"
import apiClient from "../api";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import UserSearching from "../UserSearching";

function BurgerMenu({checkedTasks, project}){

    const [isOpen, setIsOpen] = useState();
    const [modalOpen, setModalOpen] = useState();
    const [userSearchOpen, setUserSearchOpen] = useState();
    const {id} = useParams();

    const navigate = useNavigate();

    const clientRef = useRef(null);

    function conntecWebSocket(){
        const client = new Client({
            webSocketFactory: () => new SockJS(`http://localhost:8080/portfolio?token=${localStorage.getItem("token")}`),
            connectHeaders: {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
            debug: (str) => {
                console.log(str);
            }
        });

        client.activate();

        clientRef.current = client;
    }

    useEffect(() => {
        conntecWebSocket();

        return() => {
            clientRef.current?.deactivate();
        }
    })

    async function deleteTasks(e){

        const answer = confirm("Do you want to delete all this tasks?")

        if(answer){
            if(clientRef.current && clientRef.current.connected){
                clientRef.current.publish({
                    destination: `/app/project/${id}/task/delete`,
                    body: JSON.stringify(checkedTasks),
                });
            }
        }
    }

    const toggleSidebar = () => setIsOpen(!isOpen);

    // const addTask = () => {
    //     navigate(`/project/${id}/task`, { replace: true});
    // }

    const showModal =() => setModalOpen(true);

    const handleClose = () => setModalOpen(false);

    const showUserSearch =()=> setUserSearchOpen(true);

    const closeUserSearch = () => setUserSearchOpen(false);

    return (
        <Fragment>
                <div className={`d-flex ${isOpen ? 'sidebar-expanded' : 'sidebar-collapsed'} border-end border-2`}>
                <div className="border-end border-2 vh-100">
                <div className="sidebar bg-light p-2">
                    <button className="btn btn-light mb-3" onClick={toggleSidebar}>
                        <i className="bi bi-list fs-4"></i>
                    </button>

                {isOpen ? (
                <div className="d-flex flex-column">
                    <div className="border-bottom border-2 mb-2 pb-4 w-100">
                        <div className="align-content-center">
                            {project.title.toUpperCase()}
                        </div>
                    </div>
                    <button onClick={showModal} className="btn btn-outline-secondary burger-button mb-2 w-100 d-flex mb-4 mt-4"><i className="bi bi-plus-circle me-3"/> Add a new task</button>
                    <button onClick={deleteTasks} className="btn btn-outline-secondary burger-button mb-2 w-100 d-flex mb-4"><i className="bi bi-dash-circle me-3"/>Remove task</button>
                    <button className="btn btn-outline-secondary burger-button mb-2 w-100 d-flex mb-4" disabled><i className="bi bi-box-seam me-3"/>Create new task group</button>
                    <button onClick={showUserSearch} className="btn btn-outline-secondary burger-button mb-2 w-100 d-flex text-center mb-4"><i className="bi bi-people me-3"/>Add a new project contributor</button>
                </div>
                ) : (
                <div className="d-flex flex-column">
                    <button onClick={showModal} className="btn burger-button btn-outline-secondary mb-4"><i className="bi bi-plus-circle"></i></button>
                    <button onClick={deleteTasks} className="btn btn-outline-secondary burger-button mb-4"><i className="bi bi-dash-circle"></i></button>
                    <button className="btn btn-outline-secondary burger-button mb-4" disabled><i className="bi bi-box-seam"></i></button>
                    <button onClick={showUserSearch} className="btn btn-outline-secondary burger-button mb-4"><i className="bi bi-people"></i></button>
                </div>
                )}
            </div>
            </div>
            </div>
            {
                modalOpen &&
                <Modal show={showModal} onHide={handleClose} centered>
                            <Modal.Header closeButton>
                                <Modal.Title>
                                    Create new Task
                                </Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                                <AddTask project ={project}/>
                            </Modal.Body>
                    </Modal>
            }
            {
                userSearchOpen &&
                <Modal show={showUserSearch} onHide={closeUserSearch} centered>
                            <Modal.Header closeButton>
                                <Modal.Title>
                                    Enter User Nickname
                                </Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                                <UserSearching project ={project}/>
                            </Modal.Body>
                    </Modal>
            }

        </Fragment>
    )
}

export default BurgerMenu
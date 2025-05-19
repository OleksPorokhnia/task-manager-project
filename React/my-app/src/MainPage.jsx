import { Fragment, useEffect, useState } from "react"
import apiClient from "./api"
import { Link } from "react-router";
import Header from "./NavigationComponents/Header";
import "./styles/projectStyle.css"

function MainPage(){
    const [project, setProject] = useState({});
    const [projects, setProjects] = useState([]);

    useEffect(() => {
        apiClient.get(`/project/user/${localStorage.getItem("username")}`, {
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("token")}`
            }
        })
        .then(resp => {
            setProjects(resp.data);
            console.log(resp.data);
        })
    }, [])
    async function handleAddProject(){
        const finalProject = {
            ...project,
            users: [localStorage.getItem("username")]
        }
        try{
            const response = await apiClient.post("/project/add", finalProject,{
                headers: {
                    "Authorization": `Bearer ${localStorage.getItem("token")}`
                }
        });
            setProjects(prevProj => [response.data, ...prevProj]);
            setProject({});
        }catch(e){
            console.log(e.response.data)
        }
    }

    const handleUpdate = (e) => {
        const {name, value} = e.target;
        setProject(prevProject => ({
            ...prevProject,
            [name] : value
        }))
        console.log(project);
    }

    return (
        <Fragment>
            <div>
                <Header />
                <div className="d-flex">
                    <div className="ms-4 d-flex">
                        <div className="mt-4 me-3">
                            <div className="input-group">
                                <div className="form-floating">
                                    <input className="mb-3 form-control" id="projectTitle" placeholder="Enter project title" name="title" onChange={handleUpdate}></input>
                                    <label for="projectTitle">Enter project title</label>
                                </div>
                            </div>
                            <button className="btn btn-success w-100" style={{height: 45}} onClick={handleAddProject} value="Create new project">New project</button>
                        </div>
                        <div className="border-end border-2 vh-100"></div>
                    </div>
                    {localStorage.getItem("username") && projects.length > 0 && 
                        <div className="ms-5 me-4 mt-4 w-100">
                            <div className="border-top border-2 wh-100 mb-2"></div>
                            {
                                projects.map(proj => (
                                    <div className="mb-2 fs-5" key={proj.id}>
                                        <Link className="project d-block mb-3" style={{textDecoration: "none"}} to={`/project/${proj.id}`} key={proj.id}>{proj.title}</Link>
                                        <div className="border-bottom border-2 wh-100"></div>
                                    </div>
                                ))
                            }
                        </div>
                    }
                    { localStorage.getItem("username") == null && 
                        <div className="d-flex justify-content-center align-items-center" style={{height: '100vh', width: '100%'}}>
                                <div className="text-center" style={{fontSize: 40, color: "gray", maxWidth: 600}}>
                                    Please register or log in to an account to create projects or to work in already invited projects
                                </div>
                        </div>
                    }
                    {localStorage.getItem("username") && projects.length == 0 &&
                        <div className="d-flex justify-content-center align-items-center" style={{height: '100vh', width: '100%'}}>
                            <div className="text-center" style={{fontSize: 40, color: "gray", maxWidth: 600}}>
                                You don't have projects now, please create new or wait invitations
                            </div>
                    </div>
                    }
                </div>
            </div>
        </Fragment>
    )
}

export default MainPage
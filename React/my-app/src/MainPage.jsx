import { Fragment, useEffect, useState } from "react"
import apiClient from "./api"
import { Link } from "react-router";

function MainPage(){
    const [project, setProject] = useState({});
    const [projects, setProjects] = useState([]);

    useEffect(() => {
        apiClient.get("/project/", {
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
        try{
            const response = await apiClient.post("/project/add", project);
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
                <Link to={`auth/login`}>Login page</Link>
                <Link to={`auth/registration`}>Registration page</Link>
                <form>
                    <input name="title" onChange={handleUpdate}></input>
                </form>
                <button onClick={handleAddProject} value="Create new project">New project</button>

                <div>
                    {
                        projects.map(proj => (
                            <div>
                                <Link to={`/project/${proj.id}`} key={proj.id}>{proj.title}</Link>
                            </div>
                        ))
                    }
                </div>
            </div>
        </Fragment>
    )
}

export default MainPage
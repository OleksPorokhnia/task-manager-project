import { Fragment, useState} from "react";
import { useParams } from "react-router"
import apiClient from "./api";
import { useEffect } from "react";

function ProjectView() {
    const {id} = useParams();
    const [proj, setProj] = useState({});
    const [tasks, setTasks] = useState([]);

    useEffect(() => {
        apiClient.get(`/project/${id}`)
        .then((resp) => {
            console.log(resp.data);
            setProj(resp.data);
            setTasks(resp.data.tasks);
        }).catch(e => {
            console.log(e);
        })
    }, [])

    const getProj = () => {
        
    }

    return(
        <Fragment>
            <div>{proj.title}</div>
            {
                tasks.isArray && tasks.map(task => (
                    <div>
                        {task.title}
                    </div>
                ))
            }
        </Fragment>
    )
}

export default ProjectView
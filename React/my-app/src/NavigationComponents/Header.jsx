import { Fragment } from "react"
import { Link } from "react-router"
import logo from "../assets/TaskBearerBear.png"
import logoText from "../assets/TaskBearerTextLogo.png"

function Header(){

    function handleLogout(){
        const answear = confirm("Do you want to logout?")
        if(answear){
            localStorage.removeItem("token");
            localStorage.removeItem("username");
        }
    }


    return (
        <Fragment>
            <div className="d-flex justify-content-between border-bottom border-2">
                <div className="d-flex justify-content-start mt-2" style={{marginLeft:70}}>
                    <Link to={"/"}>
                         <img className="" src={logo} style={{width: 90, height: 90}}></img>
                    </Link>
                    <Link to={"/"}>
                        <img className="ms-3" src={logoText} style={{height: 80}}></img>
                    </Link>
                </div>
                {localStorage.getItem("username") == null && 
                <div className="d-flex justify-content-end mt-4 mb-3 pb-4">
                    <Link className="fs-5 mt-2 text-decoration-none rounded-3" style={{marginRight: 100}} to={`/auth/registration`}>Sign Up</Link>
                    <Link to={`/auth/login`} className="me-5 btn btn-primary" style={{width: 150}}>
                        <span className="text-reset text-decoration-none fs-5" to={`/auth/login`}>Login</span>
                    </Link>
                </div>
                }   
                { localStorage.getItem("username") && 
                <div className="d-flex justify-content-end mt-4 mb-3 pb-4">
                    <Link className="fs-5 mt-2 text-decoration-none rounded-3" style={{marginRight: 100}} onClick={handleLogout}>Logout</Link>
                </div>
                }   
            </div>
        </Fragment>
    )
}

export default Header
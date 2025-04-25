import { Fragment } from "react"
import { Link } from "react-router"

function Header(){
    return (
        <Fragment>
            <div className="d-flex justify-content-end mt-4 border-bottom border-2 mb-3 pb-4">
                <Link className="fs-5 mt-2 text-decoration-none rounded-3" style={{marginRight: 100}} to={`/auth/registration`}>Sign Up</Link>
                <Link to={`/auth/login`} className="me-5 btn btn-primary" style={{width: 150}}>
                    <span className="text-reset text-decoration-none fs-5" to={`/auth/login`}>Login</span>
                </Link>
            </div>
        </Fragment>
    )
}

export default Header
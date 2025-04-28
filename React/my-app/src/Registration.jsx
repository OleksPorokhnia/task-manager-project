import { useState, useEffect } from 'react'
import './App.css'
import axios, { all } from "axios";
import Cookies from 'js-cookie'
import apiClient from './api';
import { Link, useNavigate } from 'react-router';


function Registration() {
  const [user, setUser] = useState({});
  const [error, setError] = useState([]);

  const navigate = useNavigate();
  
  async function handleRegister(){
    try{
      const response = await apiClient.post("/auth/register", user);
      localStorage.removeItem("token");
      localStorage.setItem("token", response.data.token);
      navigate("/");
    }catch(error){
        console.log("Errors during registration ", error.response.data)
        setError(error.response.data);
    }
  };

  const handleUpdate = (e) => {
      const {name, value} = e.target;
      setUser(prevUser => ({
        ...prevUser,
        [name] : value
      }))
      console.log(user)
  }

  return (
    <>
         <div className='d-flex justify-content-center align-items-center vh-100 flex-column' >
            <div className='mb-5' style={{fontSize: 80, color: "#2E22DC"}}>Sign Up</div>
            <div className='form-floating mb-4'>
              <input className={`form-control ${(error.nickname || error.message) ? 'is-invalid' : ''}`} style={{width: 300}} id='floatingUsername' type="text" placeholder='Username' name="nickname" onChange={handleUpdate} />
              <label for="floatingUsername">Username</label>
              {(error.nickname || error.message) && 
                  <div className='invalid-feedback'>
                    {error.nickname || error.message}
                  </div>
                }
            </div>
            <div className='form-floating mb-4'>
              <input className={`form-control ${(error.email || error.message) ? 'is-invalid' : ''}`} style={{width: 300}} type="email" id="floatingEmail" placeholder="Email" name="email" onChange={handleUpdate} />
              <label for="floatingUsername">Email</label>
              {(error.email || error.message) && 
                  <div className='invalid-feedback'>
                    {error.email || error.message}
                  </div>
                }
            </div>
            <div className='form-floating mb-5'>
              <input className={`form-control ${error.password ? 'is-invalid' : ''}`} style={{width: 300}} id="floatingPassword" type="password" placeholder="Password" name="password" onChange={handleUpdate} />
              <label for="floatingPassword">Password</label>
              {(error.password) && 
                  <div className='invalid-feedback'>
                    {error.password}
                  </div>
                }
            </div>
            <div>
              <div className='d-flex flex-column fs-5 mb-2'>
                Already registered? 
                <Link style={{textDecoration: "none", color: "#645BE5"}} to={`/auth/login`}>Log In</Link>
              </div>
              <button className='btn fs-4' style={{backgroundColor: "#645BE5", color: "#FFFFFF", height: 50, width: 300}} onClick={handleRegister}>Sign Up</button>
            </div>
        </div>
    </>
  )
}

export default Registration
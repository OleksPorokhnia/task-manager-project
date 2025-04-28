import { useState, useEffect } from 'react'
import './App.css'
import axios, { all } from "axios";
import Cookies from 'js-cookie'
import { use } from 'react';
import apiClient from './api';
import { Link, useNavigate } from 'react-router';


function Login() {
  const [user, setUser] = useState({});
  const [errors, setErrors] = useState([]);

  const navigate = useNavigate();
  
  async function handleLogin (){
      try{
        const response = await apiClient.post('auth/login', user);
        console.log("Login successful! Response data:", response);
        localStorage.setItem("token", response.data.token);
        setErrors([]);
        navigate("/");
      } catch (error) {
        console.error("Error during login:", error.response.data);
        setErrors(error.response.data);
      }

  };

  console.log(errors.message);

  async function handleLogout(){
    localStorage.removeItem("token");
    localStorage.removeItem("username");
  }


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
        <div className='d-flex flex-column align-items-center justify-content-center vh-100'>
            <div className='mb-5' style={{fontSize: 80, color: "#2E22DC"}}>Log In</div>
            <div className='has-validation mb-4'>
              <div className='form-floating'>
                <input className={`form-control ${(errors.email || errors.message) ? 'is-invalid' : ''}`} style={{width: 300}} required type="email" id="floatingEmail" placeholder="Email" name="email" onChange={handleUpdate} />
                <label for="floatingUsername">Email</label>
                {(errors.message || errors.email) && 
                  <div className='invalid-feedback'>
                    {errors.message || errors.email}
                  </div>
                }
              </div>
            </div>
            <div className='mb-4'>
              <div className='form-floating'>
                <input className={`form-control ${(errors.password || errors.message) ? 'is-invalid' : ''}`} style={{width: 300}} id="floatingPassword" type="password" placeholder="Password" name="password" onChange={handleUpdate} />
                <label for="floatingPassword">Password</label>
                {(errors.message || errors.password) && 
                  <div className='invalid-feedback'>
                    {errors.message || errors.password}
                  </div>
                }
              </div>
            </div>
            <div>
              <div className='d-flex flex-column fs-5 mb-2'>
                New user? 
                <Link style={{textDecoration: "none", color: "#645BE5"}} to={`/auth/registration`}>Sign Up</Link>
              </div>
              <button className='btn fs-4' style={{backgroundColor: "#645BE5", color: "#FFFFFF", height: 50, width: 300}} onClick={handleLogin}>Log In</button>
            </div>
        </div>
    </>
  )
}

export default Login
import { useState, useEffect } from 'react'
import './App.css'
import axios, { all } from "axios";
import Cookies from 'js-cookie'
import { use } from 'react';
import apiClient from './api';
import { useNavigate } from 'react-router';


function Login() {
  const [user, setUser] = useState({});

  const navigate = useNavigate();
  async function fetchCsrfToken(){
    try{
      const resp = await axios.get("/auth/csrf", 
        {
          withCridentials: true
        }
      )
      console.log("Log from function: " + resp.data);
      return resp.data;
    }catch(error){
        console.log("Error");
    }
  } 
  async function handleLogin (){
      // await fetchCsrfToken();
      // const csrf = Cookies.get("XSRF-TOKEN");
      // if (!csrfToken) {
      //   console.error("No CSRF token available. Aborting login.");
      //   return;
      // }
      // console.log("Log from handleLoging: " + csrfToken);
      // const loggingResponse = await axios.post("http://localhost:8080/api/auth/login", 
      //   user, {
      //     headers:{
      //         'Content-Type': 'application/json',
      //         'X-XSRF-TOKEN': csrfToken
      //     },
      //     withCredentials: true
      // }
      // )

      try{
        // await apiClient.get("auth/csrf");
        const response = await apiClient.post('auth/login', user);
        // const me = await apiClient.get("auth/me");
        // console.log("current user " + me.data.username);
        //   localStorage.setItem("username", me.data.username);
        console.log("Login successful! Response data:", response);
        localStorage.setItem("token", response.data.token);
        navigate("/");
      } catch (error) {
        console.error("Error during login:", error.response.data);
      }

  };

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
        <div>
            <h2>Login</h2>
            <input type="email" placeholder="Email" name="email" onChange={handleUpdate} />
            <input type="password" placeholder="Пароль" name="password" onChange={handleUpdate} />
            <button onClick={handleLogin}>Login</button>
            <br/><br/>
            <button onClick={handleLogout}>Logout</button>
        </div>
    </>
  )
}

export default Login
import { useState, useEffect } from 'react'
import './App.css'
import axios, { all } from "axios";
import Cookies from 'js-cookie'
import apiClient from './api';


function Registration() {
  const [user, setUser] = useState({});
  const [error, setError] = useState([]);
  
  async function handleRegister(){
    //const csrfToken = Cookies.get('XSRF-TOKEN');
    //const payload = { ...user, _csrf: csrfToken };

    //console.log(csrfToken);
    const response = await apiClient.post("/auth/register", user);
    console.log(response.promise)
    localStorage.removeItem("token");
    localStorage.setItem("token", response.data.token);
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
         <div>
            <h2>Регистрация</h2>
            <input type="text" placeholder="Логин" name="nickname" onChange={handleUpdate} />
            <input type="email" placeholder="Email" name="email" onChange={handleUpdate} />
            <input type="password" placeholder="Пароль" name="password" onChange={handleUpdate} />
            <button onClick={handleRegister}>Зарегистрироваться</button>
        </div>
    </>
  )
}

export default Registration
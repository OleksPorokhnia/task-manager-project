import { useState, useEffect } from 'react'
import './App.css'
import axios, { all } from "axios";
import Cookies from 'js-cookie'


function Registration() {
  const [user, setUser] = useState({});
  const [error, setError] = useState([]);
  const handleRegister = () => {
    const csrfToken = Cookies.get('XSRF-TOKEN');
    const payload = { ...user, _csrf: csrfToken };

    console.log(csrfToken);
    axios.post("auth/register", 
      user, {
        withCredentials: true,
      headers:{
        'Content-Type': 'application/json',
        "X-XSRF-TOKEN": csrfToken
      },
    }
    ).then(resp => {
      console.log("Regisrtation sucessfull!")
    }).catch(err => {
        console.log(err.response.data.message)
    })
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
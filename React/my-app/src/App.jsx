import { useState, useEffect } from 'react'
import './App.css'
import axios, { all } from "axios";


function App() {
  const [employee, setEmployee] = useState([]);

  useEffect(()=> {

    axios.get("http://localhost:8080/FirstRESTProject/api/employees")
        .then(resp => {
        console.log("User", resp.data);
        setEmployee(resp.data);
        console.log("User data: ", employee)
    }).catch(error =>{
        console.log(error.data)
    })
  }, []);
  return (
    <>
    {
      employee.map(emp => (
        <div>Department: {emp.department}</div>
      ))
    }
    </>
  )
}

export default App

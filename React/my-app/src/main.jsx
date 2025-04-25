import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import {
  createBrowserRouter,
  RouterProvider
} from 'react-router-dom'
import Registration from './Registration.jsx'
import Login from './Login.jsx'
import MainPage from './MainPage.jsx'
import ProjectView from './ProjectView.jsx'
import TaskModal from './taskModal.jsx'
import AddTask from './AddTAsk.jsx'
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import 'bootstrap-icons/font/bootstrap-icons.css';
import Greetings from './STOMPExample/Greetings.jsx'
 

const route = createBrowserRouter([
  {
    path: "/",
    element: <MainPage />
  },
  {
    path: "/auth/registration",
    element: <Registration />
  },
  {
    path: "/auth/login",
    element: <Login />
  },
  {
    path: "/smptest",
    element: <Greetings />
  },
  {
    path: "/project/:id",
    element: <ProjectView />,
    children: [
      {
        path: "task/:taskId",
        element: <TaskModal />
      },
      {
        path: "task",
        element: <AddTask />
      }
    ]
  }
])

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={route} />
  </StrictMode>,
)

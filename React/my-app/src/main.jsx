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
    path: "/project/:id",
    element: <ProjectView />
  }
])

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={route} />
  </StrictMode>,
)

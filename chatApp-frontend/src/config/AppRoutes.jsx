import React from 'react'
import { Route, Routes } from 'react-router-dom'
import App from '../App'
import ChatPage from '../pages/ChatPage'
import LoginPage from '../pages/LoginPage'
import Register from '../pages/Register'
import ProtectedRoute from './ProtectedRoute'

const AppRoutes = () => {
  return (
    <Routes>
        <Route path='/' element={<ProtectedRoute>
          <App />
         </ProtectedRoute>} />

        <Route path='/chat' element={<ProtectedRoute>
          <ChatPage />
        </ProtectedRoute>} />

        <Route path='/login' element={<LoginPage />} />
        <Route path='/register' element={<Register />} />
        <Route path='*' element={<h1>404 Not Found</h1>} />
    </Routes>
  )
}

export default AppRoutes
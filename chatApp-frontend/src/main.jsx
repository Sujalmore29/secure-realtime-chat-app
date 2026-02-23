import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { BrowserRouter } from 'react-router-dom'
import AppRoutes from './config/AppRoutes.jsx'
import { Toaster } from 'react-hot-toast'
import { ChatProvider } from './Context/ChatContext.jsx'

createRoot(document.getElementById('root')).render(
  <BrowserRouter>
  <Toaster position="top-center" reverseOrder={false} />
    <ChatProvider>
      <AppRoutes />
    </ChatProvider>
  </BrowserRouter>
)

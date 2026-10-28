import React from 'react'
import toast from 'react-hot-toast'
import JoinCreateChat from './components/JoinCreateChat'
import ProtectedRoute from './config/ProtectedRoute'

const App = () => {
  return (
    <div>
      <ProtectedRoute>
        <JoinCreateChat />
      </ProtectedRoute>
    </div>
  )
}

export default App
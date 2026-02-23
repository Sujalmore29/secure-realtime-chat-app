import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom';

const ProfileMenu = () => {

    const [open, setOpen] = useState(false);
    const navigate = useNavigate();

    const logout = () => {
        localStorage.removeItem("token");
        toast.success("Logged out successfully");
        navigate("/login");
    };


  return (
    <div className='relative'>
        { /* Avatar */}
        <button onClick={() => setOpen(!open)}
        className='w-10 h-10 rounded-full bg-indigo-600 text-white font-bold flex items-center justify-center'>
            U
        </button>

        { /* Dropdown */ }
        {open && (
            <div className='absolute right-0 mt-2 w-40 bg-white dark:bg-gray-800 rounded-xl shadow-lg overflow-hidden'>
                <button onClick={() => navigate("/rooms")}
                className='w-full text-left px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-700'>
                    Rooms
                </button>

                <button onClick={logout}
                className='w-full text-left px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-700'>
                    Logout
                </button>
            </div>
        )}
    </div>
  )
}

export default ProfileMenu
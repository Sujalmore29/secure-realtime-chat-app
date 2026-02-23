import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom';
import { loginUser } from '../services/AuthService';
import toast from 'react-hot-toast';
import chatIcon from "../assets/chat.png";

const LoginPage = () => {

    const [form, setform] = useState({username: "",password: ""});
    const navigate = useNavigate();

    const handleChange = (e) => {
        setform({ ...form, [e.target.name]: e.target.value});
    }

    const login = async () => {
         
        if(!form.username || !form.password) {
            toast.error("Please fill all fields");
            return;
        }
        try{
            const token = await loginUser(form);
            localStorage.setItem("token",token);
            toast.success("Login Successfull");
            navigate("/");
        }catch {
            toast.error("Invalid credentials");
        }
    };

  return (
    <div className='min-h-screen flex items-center justify-center bg-linear-to-br from-indigo-500'>
        <div className='bg-white dark:bg-gray-900 p-8 rounded-2xl shadow-xl w-full max-w-md'>
            {/* Title */}
            <h1 className='text-3xl font-bold text-center text-gray-800 dark:text-white'>
                <div className='mb-2'>
                    <img src={chatIcon} className='w-16  mx-auto' />
                </div>
                 ChatSphere
            </h1>
            <p className="text-center text-gray-500 dark:text-gray-400 mt-2">
                Sign in to continue chatting
        </p>

        {/* form */}
        <div className='mt-6 space-y-4'>
            <input 
            type='text'
            name='username'
            placeholder='Username'
            onChange={handleChange}
            className='w-full px-4 py-3 rounded-full border dark:border-gray-700 dark:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-indigo-500' />

            <input 
            type='password'
            name='password'
            placeholder='Password'
            onChange={handleChange}
            className='w-full px-4 py-3 rounded-full border dark:border-gray-700 dark:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-indigo-500' />
            
            <button onClick={login}
            className='w-full bg-indigo-600 hover:bg-indigo-700 text-white py-3 rounded-2xl font-semibold transition'>
                Login
            </button>

            <p className='text-center text-sm text-gray-600 dark:text-gray-400 mt-2'>
                Not Registered yet?{" "}
                <Link to={"/register"}
                className='text-indigo-600 hover:underline font-semibold'>
                    Create an account
                </Link>
            </p>
        </div>
        </div>
    </div>
  )
}

export default LoginPage
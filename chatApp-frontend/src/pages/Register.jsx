import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom';
import { registerUser } from '../services/AuthService';
import toast from 'react-hot-toast';

const Register = () => {

    const [form, setform] = useState({username: "",password: ""});
    const navigate = useNavigate();

    const handleChange = (e) => {
        setform({ ...form, [e.target.name]: e.target.value});
    }

    const register = async() => {
        if (!form.username || !form.password) {
            toast.error("Please fill all fields");
            return;
        }
        try{
            await registerUser(form);
            toast.success("Registered Successfully");
            navigate("/login");
        }catch {
            toast.error("User already exists, login instead");
        }
    };

  return (
    <div className='min-h-screen flex items-center justify-center bg-linear-to-br from-indigo-500'>
        <div className='bg-white dark:bg-gray-900 p-8 rounded-2xl shadow-xl w-full max-w-md'>
            <h1 className='text-3xl font-bold text-center text-gray-800 dark:text-white'>
                🚀 Create Account
            </h1>
            <p className='text-center text-gray-500 dark:text-gray-400 mt-2'>
                Join the conversation
            </p>

            <div className='mt-6 space-y-4'>
                <input
                    type='text'
                    name='username'
                    placeholder='Choose a username'
                    onChange={handleChange}
                    className='w-full px-4 py-3 rounded-2xl border dark:border-gray-700 dark:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-purple-500' />

                <input
                    type='password'
                    name='password'
                    placeholder='Create a password'
                    onChange={handleChange}
                    className='w-full px-4 py-3 rounded-2xl border dark:border-gray-700 dark:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-purple-500' />

                <button onClick={register}
                className='w-full bg-purple-600 hover:bg-purple-700 text-white py-3 rounded-xl font-semibold transition'>
                    Register
                </button>
            </div>

            <p className='text-center text-sm text-gray-600 dark:text-gray-400 mt-6'>
                Already have an account?{" "}
                <Link to="/login"
                className='text-purple-600 hover:underline font-semibold'>
                    Login
                </Link>
            </p>
        </div>
    </div>
  )
}

export default Register;
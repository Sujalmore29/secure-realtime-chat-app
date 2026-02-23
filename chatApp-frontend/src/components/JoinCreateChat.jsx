import React, { useState } from 'react'
import chatIcon from "../assets/chat.png";
import toast from 'react-hot-toast';
import { createRooms, joinChats } from '../services/RoomService';
import useChatContext from '../Context/ChatContext';
import { useNavigate } from 'react-router-dom';
const JoinCreateChat = () => {
    
    const [detail, setdetail] = useState({
        roomId: "",
        username: ""
    });

    const {roomId,username,connect,setRoomId,setCurrentUser,setConnect} = useChatContext();

    const navigate = useNavigate();

    function handleFormInputChange(event){
        setdetail({
            ...detail,
            [event.target.name]:event.target.value
        });
    }

    function validateForm() {
        if(detail.username === "" || detail.roomId === ""){
            toast.error("Please fill all the fields");
            return false;
        }
        return true;
    }

    async function joinChat() {
        if(validateForm()){
            try{
                const token = localStorage.getItem("token")
                if(token != null){
                const room = await joinChats(detail.roomId);
            toast.success("Joined room successfully");
            setCurrentUser(detail.username);
            setRoomId(room.roomId);
            setConnect(true);
            navigate("/chat");
            }else{
                toast.error("Please login again");
                navigate("/login");
            }
        }catch(error){
                if(error.status === 400){
                    toast.error(error.response.data);
                }else{
                toast.error("Error joining room");
                console.log(error);
                }
            } 
        }
    }

    async function createRoom() {
        if(validateForm()){
            try{
                const token = localStorage.getItem("token")
                if(token != null){
                const response = await createRooms(detail.roomId)
                toast.success("Room created successfully");
                setCurrentUser(detail.username);
                setRoomId(detail.roomId);
                setConnect(true);
                navigate("/chat");
                }else{
                    toast.error("Please login again");
                    navigate("/login");
                }
            }catch(error){
                if(error.status === 400){
                    toast.error("Room already exists");
                }else{
                    toast.error("Error creating room");
                }
            }
        }
    }

  return (
    <div className='min-h-screen flex items-center justify-center bg-linear-to-br from-indigo-500'>
        <div className='p-8 bg-gray-800 flex flex-col gap-5 max-w-md rounded-2xl w-full'>
            <div>
                <img src={chatIcon} className='w-16  mx-auto' />
            </div>
            <h1 className='text-2xl font-semibold text-center'>Join Room / Create Room</h1>

            { /* Name Input */ }
            <div className=''>
                <label htmlFor='' className='block font-medium mb-2'>
                    Your name
                </label>
                <input onChange={handleFormInputChange}
                value={detail.username}
                type ="text"
                id='name'
                name='username'
                placeholder='Enter your name'
                className='w-full dark:bg-gray-600 px-4 py-2 border dark:border-gray-600 rounded-full focus:outline-none focus:ring-2 focus:ring-blue-500'
                ></input>
            </div>

            { /* Room ID Input */ }
            <div className=''>
                <label htmlFor='' className='block font-medium mb-2'>
                    Room ID / New Room ID
                </label>
                <input 
                onChange={handleFormInputChange}
                name='roomId'
                value={detail.roomId}
                type ="text"
                id='name'
                placeholder='Enter Room ID'
                className='w-full dark:bg-gray-600 px-4 py-2 border dark:border-gray-600 rounded-full focus:outline-none focus:ring-2 focus:ring-blue-500'
                ></input>
            </div>

            { /* button */}
            <div className='flex justify-center gap-2'>
                <button onClick={joinChat}className=' bg-blue-600 hover:bg-blue-700 text-white font-semibold px-4 py-2 rounded-full focus:outline-none focus:ring-2 focus:ring-blue-500'>Join Room</button>
                <button onClick={createRoom} className=' bg-orange-500 hover:bg-orange-600 text-white font-semibold px-4 py-2 rounded-full focus:outline-none focus:ring-2 focus:ring-orange-500'>Create Room</button>
            </div>
        </div>
    </div>
  )
}

export default JoinCreateChat
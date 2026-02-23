import React, { useEffect, useRef, useState } from 'react'
import { MdAttachFile, MdSend } from 'react-icons/md'
import useChatContext from '../Context/ChatContext';
import { useNavigate } from 'react-router-dom';
import SockJS from 'sockjs-client';
import { baseURL } from '../config/AxiosHelper';
import { Client, Stomp } from '@stomp/stompjs';
import toast from 'react-hot-toast';
import { getMessages } from '../services/RoomService';
import { timeAgo } from '../config/timeAgoMsg';
import ProfileMenu from '../components/ProfileMenu';

const ChatPage = () => {

    const {roomId,
        currentUser,
        connect,setConnect,setCurrentUser,setRoomId
    } = useChatContext();

    const navigate = useNavigate();
    useEffect(() => {
        if(!connect){
            navigate("/");
        }
    }, [connect,roomId,currentUser])

    const [message, setMessage] = useState([]);
    const [input, setInput] = useState('');
    const inputRef=useRef(null);
    const chatBoxRef=useRef(null);
    const [stompClient, setStompClient] = useState(null);

    //Reload all previous messages
    useEffect(() => {
        async function loadMessages(){
            try{
                const messages = await getMessages(roomId);
                setMessage(messages);
            }catch(error){
                console.log("Error loading messages", error);
            }
        }
        if(connect){
            loadMessages();
        }
    }, []);

    //scroll down to the latest message
    useEffect(() => {

        if(chatBoxRef.current){
            chatBoxRef.current.scroll({
                top:chatBoxRef.current.scrollHeight,
                behavior:"smooth",
            })
        }
    }, [message]);

    useEffect(() => {
        const connectWebSocket = () => {
            const socket = new SockJS(`${baseURL}/chat`) // connection establishment endpoint
            
            const client = Stomp.over(socket); 
            // client bna dega
            client.connect({
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            }, () => {
                
                setStompClient(client);
                toast.success("Connected");
            
                client.subscribe(`/topic/room/${roomId}`,(message) => {
                    console.log("Message received from topic");
                    const newMessage = JSON.parse(message.body);
                    console.log(newMessage);
                    setMessage((prev) => [...prev,newMessage]); // prev message array or new message milkr nya array message create krdenge
                });
                
            },(error) => {
                console.error("STOMP error:",error);
            })
        }
        if(connect){
            connectWebSocket();
        }
    }, [roomId]);

    function handleLeaveRoom(){
        stompClient.disconnect();
        setConnect(false);
        setCurrentUser("");
        setRoomId("");
        navigate("/");
    }

    //send message handle

    const sendMessage = async() => {
        if(stompClient && connect && input.trim()){
            console.log(input);
            const message = {
                sender:currentUser,
                content:input,
                roomId:roomId
            }
            
            stompClient.send(`/app/sendMessage/${roomId}`, {}, JSON.stringify(message));
            setInput("");
        } else{
            toast.error("Unable to send message");
        }
    }

  return (
    <div>
        <header className='flex fixed justify-around  w-full items-center dark:bg-gray-900 p-2'>
        { /* Room Name Container */ }
        <div>
            <h1 className='text-xl font-semibold'>Room : <span>{roomId}</span></h1>
        </div>

        { /* Username Container */ }
        <div>
            <h1 className='text-xl font-semibold'>
                User : <span>{currentUser}</span>
            </h1>
        </div>

        { /* Button: Leave Room */ }
        <div>
            <button onClick={handleLeaveRoom} className='dark:bg-red-500 dark:hover:bg-red-700 px-3 py-2 rounded-full'>Leave Room</button>
        </div>
        </header>

        { /* Chat messages container */}
        <main ref={chatBoxRef} className='py-20 px-10 w-2/3 dark:bg-gray-700 h-screen mx-auto overflow-auto'>
            {message.map((message, index) => (
                <div key={index} className={`flex ${message.sender==currentUser?'justify-end':'justify-start'}`}>
                    <div className={`my-2 ${message.sender==currentUser?'bg-blue-400':'bg-purple-500'} p-2 max-w-xs rounded`}>
                        <div className='flex flex-row gap-2'>
                            <img className='h-10 w-10' src='src\assets\avataaars (1).png'></img>
                            <div className='flex flex-col gap-1'>
                                <p className='text-sm font-bold'>{message.sender}</p>
                                <p>{message.content}</p>
                                <p className='text-shadow-2xs 
                                text-xs text-gray-200'>{timeAgo(message.timeStamp)}</p>
                            </div>
                        </div>
                    </div>
                </div>
            ))}
        </main>

        { /* input message container */}
        <div className='fixed bottom-2 w-full h-16'>
            <div className='dark:bg-gray-800 h-9/12 flex items-center justify-between w-1/2 pr-10 gap-4 mx-auto rounded-full'>
                <input type="text" 
                value={input}
                onChange={(e) => {setInput(e.target.value)}}
                onKeyDown={(e) => {
                    if(e.key === "Enter"){
                        sendMessage();
                    }
                }}
                placeholder='Type your message here...' className='px-5 py-2 h-full w-full dark:bg-gray-900 border dark:border-gray-700 rounded-full focus:outline-none focus:ring-2 focus:ring-blue-500'></input>
                <div className='flex gap-2'>
                <button className='dark:bg-purple-600 px-3 py-2 flex rounded-full justify-center items-center hover:bg-purple-700'><MdAttachFile /></button>
                <button onClick={sendMessage} className='dark:bg-green-600 px-3 py-2 flex rounded-full justify-center items-center hover:bg-green-700'>
                    <MdSend size={24}/>
                </button>
                </div>
            </div>
        </div>
    </div>
  )
}

export default ChatPage
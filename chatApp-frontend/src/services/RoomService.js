import { httpClient } from "../config/AxiosHelper"
import axios from "axios";

const authConfig = () => ({
  headers: {
    Authorization: `Bearer ${localStorage.getItem("token")}`,
  },
});
export const createRooms = async (roomId, username) => {
    console.log({ roomId, username });
    
    const response = await httpClient.post("/api/v1/rooms", { roomId, username });
    return response.data;
};

export const joinChats = async(roomId) => {
    const response = await httpClient.get(`/api/v1/rooms/get/${roomId}`);
    return response.data;
}

export const getMessages = async(roomId,size=50,page=0) => {
    const response = await httpClient.get(`/api/v1/rooms/${roomId}/messages?size=${size}&page=${page}`);
    return response.data;
}

export const addRoomMember = async(roomId, username) =>{
  const response = await httpClient.post(`/api/v1/rooms/${roomId}/members/add-member`,{ username }, authConfig()
  );
  return response.data;
}

export const removeRoomMember = async(roomId, username) =>{
    const response = await httpClient.delete(`/api/v1/rooms/${roomId}/members/remove-member/${username}`, authConfig());
    return response.data;
}

export const getRoomMembers = async(roomId) =>{
    const response = await httpClient.get(`/api/v1/rooms/${roomId}/members`, authConfig());
    return response.data;
}

export const getRoomDetails = async(roomId) =>{
    const response = await httpClient.get(`/api/v1/rooms/${roomId}`, authConfig());
    return response.data;
}

export const getUser = async() => {
    const response = await httpClient.get(`/api/v1/rooms/getUser`);
    return response.data;
}
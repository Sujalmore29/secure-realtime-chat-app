import { httpClient } from "../config/AxiosHelper"

export const createRooms = async (roomDetail) => {
    const response = await httpClient.post("/api/v1/rooms",roomDetail, {
        headers: {
            "Content-Type":"text/plain",
        },
    });
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
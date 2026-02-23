import { httpClient } from "../config/AxiosHelper"

export const loginUser = async(data) => {
    const res = await httpClient.post("/auth/login",data);
    return res.data;
};

export const registerUser = async(data) => {
    return httpClient.post("/auth/register",data)
};
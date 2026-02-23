import axios from 'axios';
export const baseURL = "http://localhost:8080";
export const httpClient = axios.create({
    baseURL: baseURL,
});
httpClient.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");
    if(token){
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
})

httpClient.interceptors.response.use(
    res => res,
    err => {
        if(err.response?.status === 401) {
            localStorage.removeItem("token");
            window.location.href = "/login";
        }
        return Promise.reject(err);
    }
);
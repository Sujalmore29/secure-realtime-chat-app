# 🌐 ChatSphere  
### 🔐 Secure Real-Time Chat Application

ChatSphere is a full-stack real-time chat application built using **Spring Boot, WebSocket (STOMP), JWT Authentication, and React**.  
It demonstrates secure bidirectional communication between authenticated users using an event-driven architecture.

---

## 🚀 Overview

Traditional REST APIs are not suitable for instant messaging systems because they rely on request-response cycles.

ChatSphere solves this by implementing:

- Persistent WebSocket connections
- JWT-secured communication
- Room-based message broadcasting
- Real-time message updates
- Stateless authentication
- Secure WebSocket handshake validation

This project focuses on backend architecture, real-time systems, and security integration rather than just UI.

---

## 🧠 Problem It Solves

ChatSphere addresses:

- 🔄 Real-time communication between multiple users
- 🔐 Secure WebSocket authentication using JWT
- 🧵 Event-driven message broadcasting
- 💬 Room-based chat architecture
- 🛡 Preventing unauthorized room access
- 💾 Persistent message storage

---

## 🏗 Tech Stack

### 🔹 Backend
- Java 17+
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- WebSocket
- STOMP Protocol
- ChannelInterceptor
- REST APIs
- MongoDB

### 🔹 Frontend
- React
- Context API
- SockJS
- @stomp/stompjs
- Axios
- Tailwind CSS

---

## 🔐 Security Architecture

ChatSphere uses a layered security approach:

- JWT authentication for REST APIs
- Custom JWT filter for token validation
- Stateless session management
- WebSocket ChannelInterceptor for STOMP authentication
- Authorization header validation during WebSocket CONNECT
- Protected routes in React frontend

---

## 💬 Core Features

- ✅ User Registration
- ✅ User Login (JWT based)
- ✅ Protected Routes
- ✅ Create Chat Room
- ✅ Join Existing Room
- ✅ Real-Time Messaging
- ✅ Secure WebSocket Connection
- ✅ Relative Time Stamp
- ✅ Persistent Chat History
- ✅ Logout Functionality

---
<h2>📸 Application Screenshots</h2>

<h3>🔐 Login Page</h3>
<p align="center">
    <img src="/chatApp-frontend/public/screenshots/Login.jpg" width="900"/>
</p>

<h3>🔐 Register Page</h3>
<p align="center">
    <img src="/chatApp-frontend/public/screenshots/Register.jpg" width="900"/>
</p>

<h3>Join/Create Room</h3>
<p align="center">
    <img src="/chatApp-frontend/public/screenshots/Join_Create_Room.jpg" width="900"/>
</p>

<h3>Real Time Chat</h3>
<p align="center">
    <img src="/chatApp-frontend/public/screenshots/Real_Time_Chat.jpg" width="900"/>
</p>

---

## 🔄 WebSocket Flow

1. User logs in → receives JWT
2. Token stored in localStorage
3. STOMP client connects with Authorization header
4. Backend ChannelInterceptor validates JWT
5. User subscribes to `/topic/room/{roomId}`
6. Messages broadcasted to all connected users

---

## 📂 Project Structure

```
ChatSphere/
│
├── backend/
│   ├── src/main/java/com/msd/myChatApp/
│   │
│   │   ├── Config/
│   │   │   ├── CorsConfig.java
│   │   │   ├── SecurityConfig.java
│   │   │   ├── WebSocketAuthInterceptor.java
│   │   │   └── WebSocketConfig.java
│   │   │
│   │   ├── Controllers/
│   │   │   ├── AuthController.java
│   │   │   ├── ChatController.java
│   │   │   └── RoomController.java
│   │   │
│   │   ├── Entities/
│   │   │   ├── Message.java
│   │   │   ├── Room.java
│   │   │   └── Users.java
│   │   │
│   │   ├── Filter/
│   │   │   └── JwtFilter.java
│   │   │
│   │   ├── PayLoad/
│   │   │   └── MessageRequest.java
│   │   │
│   │   ├── Repositories/
│   │   │   ├── RoomRepository.java
│   │   │   └── UserRepository.java
│   │   │
│   │   ├── Services/
│   │   │   ├── RoomServices.java
│   │   │   ├── UserDetailsServiceImpl.java
│   │   │   └── UserServices.java
│   │   │
│   │   ├── Utils/
│   │   │   └── JwtUtils.java
│   │   │
│   │   └── ChatAppBackendApplication.java
│   │
│   └── pom.xml
│
├── frontend/
│   ├── public/
│   │
│   ├── src/
│   │   ├── assets/
│   │   │
│   │   ├── components/
│   │   │   ├── JoinCreateChat.jsx
│   │   │   └── ProfileMenu.jsx
│   │   │
│   │   ├── config/
│   │   │   ├── AppRoutes.jsx
│   │   │   ├── AxiosHelper.js
│   │   │   ├── ProtectedRoute.jsx
│   │   │   └── timeAgoMsg.js
│   │   │
│   │   ├── Context/
│   │   │   └── ChatContext.jsx
│   │   │
│   │   ├── pages/
│   │   │   ├── ChatPage.jsx
│   │   │   ├── LoginPage.jsx
│   │   │   └── Register.jsx
│   │   │
│   │   ├── services/
│   │   │   ├── AuthService.js
│   │   │   └── RoomService.js
│   │   │
│   │   ├── App.jsx
│   │   ├── App.css
│   │   ├── index.css
│   │   └── main.jsx
│   │
│   ├── index.html
│   ├── package.json
│   ├── vite.config.js
│   └── README.md
│
└── README.md
```

---

## ▶️ How To Run Locally

### 🔹 Backend

```bash
cd chat-app-backend
mvn clean install
mvn spring-boot:run
```

Runs at:
```
http://localhost:8080
```

---

### 🔹 Frontend

```bash
cd chatApp-frontend
npm install
npm run dev
```

Runs at:
```
http://localhost:5173
```

---

## 🧩 Key Learning Outcomes

- Real-time event-driven systems
- Secure WebSocket authentication
- Spring Security filter chain management
- JWT validation and token parsing
- Handling anonymous vs authenticated users
- Managing persistent WebSocket connections
- React state synchronization with live updates
- Debugging WebSocket handshake and 403 errors

---

## 📈 Future Enhancements

- 🟢 Online/Offline Status
- ✔ Seen / Delivered Indicators
- 📎 File Attachments
- 🚀 Redis for scalability

---

## 👨‍💻 Author

- Sujal More
- LinkedIn:<a href="www.linkedin.com/in/sujal-more-841575249">Sujal More</a>
- Email: moresujal2912@gmail.com

# 🌐 ChatSphere  
### 🔐 Secure Real-Time Chat Application

ChatSphere is a full-stack real-time chat application built using **Spring Boot, WebSocket (STOMP), JWT Authentication, and React**.  
It demonstrates secure bidirectional communication between authenticated users using an event-driven architecture.

---

## 🚀 Overview

Traditional REST APIs are not suitable for instant messaging systems because they rely on request-response cycles.

ChatSphere solves this by implementing:

- Persistent WebSocket connections for low-latency communication
- JWT-based authentication for REST APIs and STOMP connections
- Room-based message broadcasting and persistent chat history
- Private room membership with server-side authorization
- Owner-controlled member management (add/remove users)
- Real-time message updates
- Stateless authentication and protected frontend routes

The project focuses on backend architecture, real-time communication, and integration rather than just UI.
---

## 🧠 Problem It Solves

ChatSphere addresses:

- 🔄 Real-time communication between multiple users
- 🔐 Secure WebSocket authentication using JWT
- 🧵 Event-driven message broadcasting
- 💬 Room-based chat architecture
- 🛡 Preventing unauthorized room access
- 👥 Restricting rooms to their members
- 👑 Allowing room owners to manage membership
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

### 🔹 Data Storage
- MongoDB — user, room, membership, and message data

---

## 🧭 Architecture Overview

```text
                         ┌──────────────────────────┐
                         │       React Client       │
                         │ React + Context + Axios  │
                         └────────────┬─────────────┘
                                      │
                       REST API calls │ HTTP + JWT
                                      ▼
                         ┌──────────────────────────┐
                         │ Spring Security + JWT    │
                         │       JwtFilter          │
                         └────────────┬─────────────┘
                                      │
                                      ▼
                         ┌──────────────────────────┐
                         │ Room / Auth Controllers  │
                         │ Services + Repositories  │
                         └────────────┬─────────────┘
                                      │
                                      ▼
                         ┌──────────────────────────┐
                         │         MongoDB          │
                         │ Users / Rooms / Messages │
                         └──────────────────────────┘

       Real-time messaging path
       ────────────────────────
       React STOMP client
             │ SockJS connection to /chat
             │ STOMP CONNECT with JWT
             ▼
       WebSocketAuthInterceptor
             │ Validate token and set Principal
             │ Check membership on SUBSCRIBE and SEND
             ▼
       ChatController
             │ Validate room membership
             │ Save message
             ▼
       STOMP broker → /topic/room/{roomId} → room subscribers
```

### 🔄 Main Application Flow

1. A user registers or logs in and receives a JWT.
2. The React client sends the JWT with protected REST requests.
3. The user creates a room; the backend assigns the creator as its owner and initial member.
4. The owner can add registered users to the room or remove existing members.
5. A user requests room details and message history. The backend checks membership before returning private room data.
6. The STOMP client connects to the WebSocket endpoint and sends the JWT in the `CONNECT` frame.
7. The backend validates the JWT and associates the authenticated username with the STOMP session's `Principal`.
8. The backend checks room membership for room-specific `SUBSCRIBE` and `SEND` operations.
9. Authorized messages are persisted and broadcast to `/topic/room/{roomId}`.

---

## 🔐 Security Architecture

ChatSphere uses a layered security approach:

- JWT authentication for REST APIs
- Custom JWT filter for token validation
- Stateless session management
- WebSocket ChannelInterceptor for STOMP authentication
- Authorization header validation during STOMP `CONNECT`
- Server-side room membership checks for `SUBSCRIBE` and `SEND`
- Room history and membership APIs protected by membership checks
- Only the room owner can add or remove members
- Message sender identity derived from the authenticated `Principal`, not trusted from the client payload
- Protected routes in React frontend

> **Authentication vs. authorization:** A valid JWT identifies a user; it does not automatically grant access to every room. The backend separately checks whether that user is a member of the requested room.

---

## 💬 Core Features

- ✅ User Registration
- ✅ User Login (JWT based)
- ✅ Protected Routes
- ✅ Create Chat Room
- ✅ Join Existing Room (membership checked by backend)
- ✅ Private room membership
- ✅ Room owner assigned at room creation
- ✅ Owner can add registered users to a room
- ✅ Owner can remove room members
- ✅ Member list visible to authorized room members
- ✅ Real-Time Messaging
- ✅ JWT-authenticated STOMP connection
- ✅ Server-side authorization for room subscriptions and messages
- ✅ Message sender derived from authenticated identity
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

## 🔄 WebSocket and Private Room Flow

### Connection and messaging

1. User logs in and receives a JWT.
2. The frontend uses the JWT for protected REST calls and includes it in the STOMP `CONNECT` headers.
3. `WebSocketAuthInterceptor` validates the token and sets the authenticated user as the STOMP `Principal`.
4. When the client subscribes to `/topic/room/{roomId}`, the backend checks that the user belongs to that room.
5. When the client sends to `/app/sendMessage/{roomId}`, the backend checks membership again.
6. `ChatController` takes the sender from `Principal`, validates the message, persists it, and broadcasts it to `/topic/room/{roomId}`.
7. Authorized subscribers receive the new message in real time.

### Room membership flow

```text
User creates room
      │
      ▼
Backend sets creator as owner + initial member
      │
      ▼
Owner adds an existing registered user
      │
      ▼
Backend verifies requester is the room owner
      │
      ▼
Membership is saved in MongoDB
      │
      ▼
Added user can load history, subscribe, and send messages
```

- Non-members cannot read room history or subscribe to the room's message topic.
- Non-members cannot send messages to the room.
- Only the owner can add or remove members.
- The backend enforces these rules; hiding controls in React is not considered authorization.

> Membership revocation for users who already have an active WebSocket subscription should also close or revoke that subscription if immediate removal is required.

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
│   │   │   ├── MessageRequest.java
|   |   |   └── RoomDetails.java
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
|   |   |   ├── RoomMemebers.jsx
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

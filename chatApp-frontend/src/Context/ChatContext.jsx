import { createContext, useContext, useState } from "react";

const ChatContext = createContext();

export const ChatProvider = ({children}) => {
    const [roomId, setRoomId] = useState("");
    const [currentUser, setCurrentUser] = useState("");
    const [connect, setConnect] = useState(false);

    return (<ChatContext.Provider value={{roomId, setRoomId, currentUser, setCurrentUser,connect,setConnect}}>{children}</ChatContext.Provider>
    );
};

const useChatContext = () => useContext(ChatContext);
export default useChatContext;
import { useState } from "react";
import ChatSidebar from "../components/ChatSideBar";
import ChatBox from "../components/ChatBox";

const ChatPage = ({ currentUserId }) => {
  const [receiverId, setReceiverId] = useState(null);

  return (
    <div style={{ display: "flex", height: "100vh" }}>
      <ChatSidebar currentUserId={currentUserId} onSelectUser={setReceiverId} />
      {receiverId && (
        <ChatBox currentUserId={currentUserId} receiverId={receiverId} />
      )}
    </div>
  );
};

export default ChatPage;

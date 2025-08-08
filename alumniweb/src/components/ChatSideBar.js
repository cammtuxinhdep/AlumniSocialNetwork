import { ref, onValue } from "firebase/database";
import { db } from "../configs/firebaseConfig";
import { useEffect, useState } from "react";

const ChatSidebar = ({ currentUserId, onSelectUser }) => {
  const [chatUsers, setChatUsers] = useState([]);

  useEffect(() => {
    const chatRef = ref(db, 'chats');
    onValue(chatRef, (snapshot) => {
      const data = snapshot.val();
      if (!data) return;

      const users = new Set();
      Object.keys(data).forEach(chatId => {
        const [uid1, uid2] = chatId.split('_');
        if (uid1 === currentUserId) users.add(uid2);
        else if (uid2 === currentUserId) users.add(uid1);
      });

      // Giả định bạn đã có thông tin user trong hệ thống khác
      setChatUsers(Array.from(users));
    });
  }, [currentUserId]);

  return (
    <div style={{ width: "250px", background: "#111", color: "#fff", padding: "10px" }}>
      <h5>💬 Đã nhắn tin</h5>
      {chatUsers.map(uid => (
        <div key={uid} onClick={() => onSelectUser(uid)} style={{ padding: "8px", cursor: "pointer" }}>
          👤 {uid}
        </div>
      ))}
    </div>
  );
};

export default ChatSidebar;

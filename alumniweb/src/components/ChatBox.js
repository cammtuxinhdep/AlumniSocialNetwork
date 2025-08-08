import { ref, onValue, push } from "firebase/database";
import { db } from "../configs/firebaseConfig";
import { useEffect, useState } from "react";

const ChatBox = ({ currentUserId, receiverId }) => {
  const [messages, setMessages] = useState([]);
  const [newMsg, setNewMsg] = useState("");

  const chatId = [currentUserId, receiverId].sort().join('_');

  useEffect(() => {
    const msgRef = ref(db, `chats/${chatId}/messages`);
    onValue(msgRef, snapshot => {
      const msgs = snapshot.val();
      if (msgs) {
        setMessages(Object.values(msgs).sort((a, b) => a.timestamp - b.timestamp));
      } else {
        setMessages([]);
      }
    });
  }, [chatId]);

  const sendMessage = async () => {
    if (!newMsg.trim()) return;

    const msgRef = ref(db, `chats/${chatId}/messages`);
    await push(msgRef, {
      senderId: currentUserId,
      content: newMsg,
      timestamp: Date.now()
    });

    setNewMsg("");
  };

  return (
    <div style={{ flex: 1, padding: "20px", background: "#f5f5f5" }}>
      <div style={{ height: "400px", overflowY: "scroll", marginBottom: "10px" }}>
        {messages.map((msg, idx) => (
          <div key={idx} style={{
            textAlign: msg.senderId === currentUserId ? "right" : "left",
            marginBottom: "10px"
          }}>
            <span style={{
              background: msg.senderId === currentUserId ? "#d1e7dd" : "#fff3cd",
              padding: "8px 12px", borderRadius: "15px", display: "inline-block"
            }}>
              {msg.content}
            </span>
          </div>
        ))}
      </div>
      <div>
        <input
          type="text"
          value={newMsg}
          onChange={e => setNewMsg(e.target.value)}
          style={{ width: "80%", padding: "10px" }}
        />
        <button onClick={sendMessage} style={{ padding: "10px" }}>Gửi</button>
      </div>
    </div>
  );
};

export default ChatBox;

import React from "react";
import { Badge } from "react-bootstrap";

const emojiMap = {
    LIKE: "👍",
    HAHA: "😂",
    HEART: "❤️",
};

const ReactionStats = ({ stats }) => {
    return (
        <div className="d-flex gap-2 align-items-center">
            {Object.entries(stats).map(([type, count]) => (
                count > 0 && (
                    <Badge key={type} pill bg="light" text="dark">
                        {emojiMap[type]} {count}
                    </Badge>
                )
            ))}
        </div>
    );
};

export default ReactionStats;

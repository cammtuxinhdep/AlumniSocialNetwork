const ReactionStats = ({ reactions, emojis }) => {
  return (
    <div className="reaction-stats">
      {reactions.map(({ reactionType, count }) => (
        count > 0 && (
          <div key={reactionType} className="reaction-item">
            <span>{emojis[reactionType]}</span>
            <span className="count">{count}</span>
          </div>
        )
      ))}
    </div>
  );
};

export default ReactionStats;
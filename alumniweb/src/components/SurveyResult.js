import React, { useEffect, useState } from 'react';
import { authApis, endpoints } from "../configs/Apis";

const SurveyResult = ({ surveyId }) => {
    const [stats, setStats] = useState(null);

    useEffect(() => {
        const load = async () => {
            try {
                const res = await authApis().get(endpoints.surveyStats(surveyId));
                setStats(res.data);
            } catch (err) {
                console.error(err);
            }
        };

        load();
    }, [surveyId]);

    if (!stats) return <p>Đang tải kết quả...</p>;

    const total = stats.total || 1;
    const keys = Object.keys(stats).filter(k => k !== 'total');

    return (
        <div className="p-4 bg-gray-50 rounded">
            <h2 className="text-lg font-semibold mb-3">Kết quả khảo sát</h2>
            <ul className="space-y-3">
                {keys.map(key => (
                    <li key={key} className="border p-3 rounded">
                        <div className="flex justify-between mb-1">
                            <span>{key}</span>
                            <span>{stats[key]}  lượt</span>
                        </div>
                        <div className="h-2 bg-gray-200 rounded">
                            <div
                                className="h-2 bg-blue-600 rounded"
                                style={{ width: `${(stats[key] / total) * 100}%` }}
                            />
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default SurveyResult;

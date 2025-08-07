import { useEffect, useState, useContext, useRef } from "react";
import { Button, Form, Alert } from "react-bootstrap";
import { authApis, endpoints } from "../configs/Apis";
import SurveyItem from "./SurveyItem";
import MySpinner from "./layout/MySpinner";
import { MyUserContext } from "../configs/Context";
import cookie from 'react-cookies';
import { useNavigate } from "react-router-dom";

const SurveyList = () => {
    const [surveys, setSurveys] = useState([]);
    const [user] = useContext(MyUserContext);
    const [page, setPage] = useState(1);
    const [q, setQ] = useState("");
    const [loading, setLoading] = useState(false);
    const [hasMore, setHasMore] = useState(true);
    const debounceTimer = useRef(null);
    const navigate = useNavigate();

    const loadSurveys = async (search = q, pageNumber = page) => {
        let url = `${endpoints.surveys}?page=${pageNumber}`;
        if (search) url += `&kw=${search}`;

        setLoading(true);
            const res = await authApis().get(url);
            const data = res.data;

            if (!Array.isArray(data)) {
                console.warn("API trả về dữ liệu không hợp lệ:", data);
                setSurveys([]);
                setHasMore(false);
                return;
            }

            if (data.length === 0) {
                if (pageNumber === 1) setSurveys([]);
                setHasMore(false);
            } else {
                if (pageNumber === 1)
                    setSurveys(data);
                else
                    setSurveys(prev => [...prev, ...data]);

                setHasMore(data.length >= 6);
            }
            setSurveys([]);
            setHasMore(false);
        
            setLoading(false);
        
    };
    useEffect(() => {
        if (debounceTimer.current) clearTimeout(debounceTimer.current);

        debounceTimer.current = setTimeout(() => {
            setPage(1);           
            loadSurveys(q, 1);    
        }, 500);

        return () => clearTimeout(debounceTimer.current);
    }, [q]);

    useEffect(() => {
        if (page > 1) loadSurveys(q, page);
    }, [page]);

    useEffect(() => {
        const token = cookie.load("token");
        if (!token) navigate("/login");
        else loadSurveys(q, 1); // tải ban đầu
    }, []);

    const loadMore = () => {
        if (!hasMore || loading) return;
        setPage(prev => prev + 1);
    };

    return (
        <>
            <Form>
                <Form.Group className="mb-3 mt-2">
                    <Form.Control
                        value={q}
                        onChange={e => setQ(e.target.value)}
                        type="text"
                        placeholder="Tìm kiếm khảo sát..."
                    />
                </Form.Group>
            </Form>

            {surveys.length === 0 && !loading && (
                <Alert variant="info">Không có khảo sát nào để hiển thị.</Alert>
            )}

            {surveys.map(s => (
                <SurveyItem key={s.id} survey={s} onSurveyUpdate={() => {
                    setPage(1);
                    loadSurveys(q, 1);
                }} />
            ))}

            {loading && <MySpinner />}

            {!loading && hasMore && surveys.length > 0 && (
                <div className="text-center mt-2 mb-2">
                    <Button variant="primary" onClick={loadMore}>Xem thêm...</Button>
                </div>
            )}
        </>
    );
};

export default SurveyList;

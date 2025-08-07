import { useState, useEffect, useContext } from "react";
import { Alert, Button, Form, Col, Row } from "react-bootstrap";
import { authApis, endpoints } from "../configs/Apis";
import { MyUserContext } from "../configs/Context";
import SurveyForm from "./SurveyForm";
import SurveyItem from "./SurveyItem";
import { useNavigate } from "react-router-dom";
import { debounce } from "lodash";
import MySpinner from "./layout/MySpinner";

const SurveyList = () => {
  const [userState] = useContext(MyUserContext);
  const currentUser = userState?.currentUser;
  const navigate = useNavigate();

  const [surveys, setSurveys] = useState([]);
  const [loading, setLoading] = useState(false);
  const [q, setQ] = useState("");
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(true);
  const [error, setError] = useState("");

  const debouncedSetQ = debounce((value) => setQ(value), 300);

  const loadSurveys = async () => {
    if (!hasMore || loading) return;

    setLoading(true);
    try {
      const res = await authApis().get(q ? endpoints.surveySearch : endpoints.survey, {
        params: q ? { title: q } : { page },
      });
      const data = Array.isArray(res.data) ? res.data : [];

      if (data.length === 0) {
        if (page === 1) setSurveys([]);
        setHasMore(false);
      } else {
        setSurveys((prev) => (page === 1 ? data : [...prev, ...data]));
      }
      setError("");
    } catch (err) {
      setError("Lỗi tải danh sách khảo sát!");
      console.error("Lỗi tải khảo sát:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    setPage(1);
    setHasMore(true);
  }, [q]);

  useEffect(() => {
    loadSurveys();
  }, [page, q]);

  const handleSurveyCreated = (newSurvey) => {
    if (!newSurvey || !newSurvey.id) return;
    setSurveys((prev) => [newSurvey, ...prev]);
  };

  const handleVote = () => {
    setPage(1);
    setHasMore(true);
    loadSurveys();
  };

  return (
    <div className="container my-4">
      {error && <Alert variant="danger">{error}</Alert>}

      {currentUser?.id ? (
        <SurveyForm onSurveyCreated={handleSurveyCreated} />
      ) : (
        <Button
          variant="success"
          className="mb-4"
          onClick={() => navigate("/login")}
        >
          Đăng nhập để tạo khảo sát
        </Button>
      )}

      <Form className="mb-4">
        <Form.Control
          type="text"
          placeholder="Tìm kiếm khảo sát..."
          onChange={(e) => debouncedSetQ(e.target.value)}
        />
      </Form>

      {surveys.length === 0 && !loading && (
        <Alert variant="info">Không có khảo sát nào!</Alert>
      )}

      <Row>
        {surveys.map((survey) => (
          <Col key={survey.id} xs={12} md={6} lg={4} className="mb-4">
            <SurveyItem survey={survey} onVote={handleVote} />
          </Col>
        ))}
      </Row>

      {loading && <MySpinner />}

      {!loading && hasMore && (
        <div className="text-center mb-4">
          <Button
            variant="outline-primary"
            onClick={() => setPage((prev) => prev + 1)}
          >
            Xem thêm
          </Button>
        </div>
      )}
    </div>
  );
};

export default SurveyList;
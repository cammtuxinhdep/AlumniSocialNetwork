import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { authApis, endpoints } from "../configs/Apis";
import MySpinner from "./layout/MySpinner";
import cookie from "react-cookies";
import { Card, Button, Form, Alert } from "react-bootstrap";
import { formatTimeVi } from "../formatters/TimeFormatter"; // giống PostItem

const SurveyDetail = () => {
    const { id } = useParams();
    const [survey, setSurvey] = useState(null);
    const [loading, setLoading] = useState(true);
    const [selectedOption, setSelectedOption] = useState(null);
    const [voted, setVoted] = useState(false);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        const token = cookie.load("token");
        if (!token) {
            navigate("/login");
            return;
        }

        const loadSurvey = async () => {
            try {
                const res = await authApis().get(endpoints.surveyDetail(id));
                setSurvey(res.data);
            } catch (err) {
                console.error("Lỗi tải chi tiết khảo sát:", err.response?.data || err.message);
                navigate("/surveys");
            } finally {
                setLoading(false);
            }
        };

        loadSurvey();
    }, [id, navigate]);

    const handleVote = async () => {
        if (!selectedOption) {
            setError("Vui lòng chọn một lựa chọn để vote.");
            return;
        }

        setError("");

        try {
            await authApis().post(endpoints.surveyVote(id), {
                optionId: selectedOption,
            });

            const updatedOptions = survey.options.map(option =>
                option.id === selectedOption
                    ? { ...option, voteCount: option.voteCount + 1 }
                    : option
            );

            setSurvey({ ...survey, options: updatedOptions });
            setVoted(true);
        } catch (err) {
            setError("Có lỗi xảy ra khi gửi vote.");
            console.error("Vote error:", err.response?.data || err.message);
        }
    };

    if (loading) return <MySpinner />;
    if (!survey) return <p>Không tìm thấy khảo sát.</p>;

    return (
        <Card className="my-3 shadow-sm">
            <Card.Body>
                <Card.Title className="h4">{survey.title}</Card.Title>
                <Card.Text className="text-muted">{survey.description}</Card.Text>
                <small className="text-secondary">
                    Ngày tạo: {survey.createdAt ? formatTimeVi(survey.createdAt) : "Không xác định"}
                </small>

                <div className="mt-4">
                    <h5 className="mb-3">Các lựa chọn:</h5>

                    <Form>
                        {survey.options?.map(option => (
                            <Form.Check
                                type="radio"
                                id={`option-${option.id}`}
                                name="survey-options"
                                key={option.id}
                                label={
                                    <>
                                        {option.optionText}
                                        <span className="text-muted ms-2">(Số vote: {option.voteCount})</span>
                                    </>
                                }
                                value={option.id}
                                onChange={() => setSelectedOption(option.id)}
                                disabled={voted}
                                className="mb-2"
                            />
                        ))}
                    </Form>

                    {error && <Alert variant="danger" className="mt-3">{error}</Alert>}

                    {!voted ? (
                        <Button className="mt-3" variant="primary" onClick={handleVote}>
                            Gửi vote
                        </Button>
                    ) : (
                        <Alert variant="success" className="mt-3">
                            ✅ Cảm ơn bạn đã vote!
                        </Alert>
                    )}
                </div>
            </Card.Body>
        </Card>
    );
};

export default SurveyDetail;

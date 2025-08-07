import React, { useState } from 'react';
import { Card, Button, Row, Col } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import SurveyResult from './SurveyResult';
import { formatTimeVi } from '../formatters/TimeFormatter'; 

const SurveyItem = ({ survey }) => {
    const [showResult, setShowResult] = useState(false);

    return (
        <Card className="my-3 shadow-sm">
            <Card.Body>
                <Row className="align-items-start">
                    <Col>
                        <h5>
                            <Link to={`/surveys/${survey.id}`} className="text-decoration-none text-primary">
                                {survey.title}
                            </Link>
                        </h5>

                        {survey.description && (
                            <p className="text-muted">{survey.description}</p>
                        )}

                        <p className="text-secondary" style={{ fontSize: "0.875rem" }}>
                            Tạo lúc: {survey.createdAt ? formatTimeVi(survey.createdAt) : "Không xác định"}
                        </p>
                    </Col>

                    <Col xs="auto">
                        <Button
                            variant={showResult ? "secondary" : "success"}
                            onClick={() => setShowResult(!showResult)}
                        >
                            {showResult ? "Ẩn kết quả" : "Xem kết quả"}
                        </Button>
                    </Col>
                </Row>

                {showResult && (
                    <div className="mt-3 pt-3 border-top">
                        <SurveyResult surveyId={survey.id} />
                    </div>
                )}
            </Card.Body>
        </Card>
    );
};

export default SurveyItem;

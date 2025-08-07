import { useState, useContext } from "react";
import { Button, Form, Card, Alert, Spinner } from "react-bootstrap";
import { authApis, endpoints } from "../configs/Apis";
import { MyUserContext } from "../configs/Context";

const SurveyForm = ({ onSurveyCreated }) => {
  const [userState] = useContext(MyUserContext);
  const currentUser = userState?.currentUser;

  const [title, setTitle] = useState("");
  const [options, setOptions] = useState([""]);
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  const handleOptionChange = (index, value) => {
    const newOptions = [...options];
    newOptions[index] = value;
    setOptions(newOptions);
  };

  const addOptionField = () => {
    setOptions([...options, ""]);
  };

  const removeOptionField = (index) => {
    if (options.length > 1) {
      setOptions(options.filter((_, i) => i !== index));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!currentUser) {
      setError("Vui lòng đăng nhập để tạo khảo sát");
      return;
    }

    if (!title.trim()) {
      setError("Tiêu đề khảo sát không được để trống");
      return;
    }

    if (options.some((opt) => !opt.trim())) {
      setError("Các tùy chọn không được để trống");
      return;
    }

    try {
      setSubmitting(true);

      const payload = {
        title,
        surveyOptionSet: options.map((opt) => ({ optionText: opt.trim() })),
      };

      const res = await authApis().post(endpoints.survey, payload);
      onSurveyCreated(res.data);
      setTitle("");
      setOptions([""]);
      setError("");
    } catch (err) {
      setError("Lỗi khi tạo khảo sát!");
      console.error(err);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Card className="mb-3 shadow-sm">
      <Card.Body>
        {error && <Alert variant="danger">{error}</Alert>}
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Tiêu đề khảo sát</Form.Label>
            <Form.Control
              type="text"
              placeholder="Nhập tiêu đề khảo sát"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              disabled={submitting}
            />
          </Form.Group>

          <Form.Label>Tùy chọn</Form.Label>
          {options.map((option, index) => (
            <div key={index} className="d-flex mb-2">
              <Form.Control
                type="text"
                placeholder={`Tùy chọn ${index + 1}`}
                value={option}
                onChange={(e) => handleOptionChange(index, e.target.value)}
                disabled={submitting}
                className="me-2"
              />
              {options.length > 1 && (
                <Button
                  variant="danger"
                  onClick={() => removeOptionField(index)}
                  disabled={submitting}
                >
                  Xóa
                </Button>
              )}
            </div>
          ))}
          <Button
            variant="outline-primary"
            onClick={addOptionField}
            disabled={submitting}
            className="mb-3"
          >
            Thêm tùy chọn
          </Button>

          <div className="d-flex justify-content-end">
            <Button variant="primary" type="submit" disabled={submitting}>
              {submitting ? (
                <>
                  <Spinner
                    as="span"
                    animation="border"
                    size="sm"
                    className="me-1"
                  />
                  Đang tạo...
                </>
              ) : (
                "Tạo khảo sát"
              )}
            </Button>
          </div>
        </Form>
      </Card.Body>
    </Card>
  );
};

export default SurveyForm;
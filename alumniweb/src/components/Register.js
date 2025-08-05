import { useRef, useState } from "react";
import { Alert, Button, Col, Form, Row } from "react-bootstrap";
import Apis, { endpoints } from "../configs/Apis";
import { useNavigate } from "react-router-dom";
import MySpinner from "./layout/MySpinner";

const Register = () => {
    const info = [{
        "title": "Họ và tên đệm",
        "field": "lastName",
        "type": "text"
    }, {
        "title": "Tên",
        "field": "firstName",
        "type": "text"
    }, {
        "title": "Mã số sinh viên",
        "field": "studentId",
        "type": "text"
    }, {
        "title": "Email",
        "field": "email",
        "type": "email"
    }, {
        "title": "Tên đăng nhập",
        "field": "username",
        "type": "text"
    }, {
        "title": "Mật khẩu",
        "field": "password",
        "type": "password"
    }, {
        "title": "Xác nhận mật khẩu",
        "field": "confirm",
        "type": "password"
    }];

    const avatar = useRef();
    const [user, setUser] = useState({});
    const [msg, setMsg] = useState();
    const nav = useNavigate();
    const [loading, setLoading] = useState(false);

    const validate = () => {
        if (user.confirm !== user.password)
            setMsg("Mật khẩu không khớp!");
        return true;
    }

    const register = async (event) => {
        event.preventDefault();

        if (validate()) {
            try {
                setLoading(true);
                let formData = new FormData();
                for (let key in user)
                    if (key !== 'confirm')
                        formData.append(key, user[key]);

                if (avatar.current.files.length > 0) {
                    formData.append("avatar", avatar.current.files[0]);
                }

                console.info(formData);
                let res = await Apis.post(endpoints['register'], formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                });

                if (res.status === 201)
                    nav("/login");
            } catch (ex) {
                console.error(ex);
            } finally {
                setLoading(false);
            }
        }
    }

    return (
        <div className="form-container">
            <Form onSubmit={register} style={{ width: "71.5%", backgroundColor: "#add8e678", margin: "0 auto", padding: "2rem",
                    boxShadow: "5px 5px 5px #55555599", borderRadius: "1rem", fontWeight: "bold" }}>

                <h1 className="text-center mb-3" style={{ color: "#0e3a57" }}>ĐĂNG KÝ</h1>

                {msg && <Alert variant="danger">{msg}</Alert>}

                <Row>
                    <Col md={6}>
                        {info.slice(0, 4).map(i =>
                            <Form.Group key={i.field} className="mb-3" controlId={i.field}>
                                <Form.Label>{i.title}</Form.Label>
                                <Form.Control
                                    required value={user[i.field]} onChange={(e) => setUser({ ...user, [i.field]: e.target.value })} type={i.type} placeholder={`Nhập ${i.title.toLowerCase()}`} />
                            </Form.Group>)}
                    </Col>
                    <Col md={6}>
                        {info.slice(4).map(i =>
                            <Form.Group key={i.field} className="mb-3" controlId={i.field}>
                                <Form.Label>{i.title}</Form.Label>
                                <Form.Control required value={user[i.field]} onChange={(e) => setUser({ ...user, [i.field]: e.target.value })} type={i.type} placeholder={`Nhập ${i.title.toLowerCase()}`} />
                            </Form.Group>)}
                    </Col>
                </Row>

                <Form.Group className="mb-3" controlId="avatar">
                    <Form.Label>Ảnh đại diện</Form.Label>
                    <Form.Control type="file" ref={avatar} />
                </Form.Group>

                {loading ? <MySpinner /> :
                    <Form.Group className="mb-3" style={{ direction: "rtl" }}>
                        <Button type="submit" style={{ backgroundColor: "#0e3a57", color: "white", border: "none" }} className="mt-2">Đăng ký</Button>
                    </Form.Group>}
            </Form>
        </div>
    );
};

export default Register;
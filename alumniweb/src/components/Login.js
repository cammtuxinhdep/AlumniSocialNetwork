import { useContext, useState } from "react";
import { Alert, Button, Form } from "react-bootstrap";
import MySpinner from "./layout/MySpinner";
import { useNavigate } from "react-router-dom";
import Apis, { authApis, endpoints } from "../configs/Apis";
import cookie from 'react-cookies'
import { MyUserContext } from "../configs/Context";

const Login = () => {
    const [, dispatch] = useContext(MyUserContext);

    const info = [{
        "title": "Tên đăng nhập",
        "field": "username",
        "type": "text"
    }, {
        "title": "Mật khẩu",
        "field": "password",
        "type": "password"
    }];

    const [user, setUser] = useState({});
    const [msg, setMsg] = useState();
    const nav = useNavigate();
    const [loading, setLoading] = useState(false);

    const login = async (event) => {
        event.preventDefault();
        setMsg("");

        try {
            setLoading(true);

            let res = await Apis.post(endpoints['login'], {
                ...user
            });

            if (res.status === 200) {
                cookie.save('token', res.data.token);

                let u = await authApis().get(endpoints.profile());

                if (u.data.isLocked === false) {
                    cookie.save('user', u.data);

                    dispatch({
                        "type": "login",
                        "payload": u.data
                    });

                    nav("/");
                } else {
                    if (u.data.userRole === "ROLE_ALUMNI")
                        setMsg("Mã số sinh viên của bạn chưa được xác nhận, vui lòng chờ thêm!");
                    else
                        setMsg("Tài khoản của bạn đã bị khóa do không đổi mật khẩu đúng hạn! Vui lòng liên hệ admin để gia hạn thời gian đổi mật khẩu!");
                    cookie.remove('token');
                }
            } else
                return;
        } catch (ex) {
            console.error(ex);
            setMsg("Sai thông tin đăng nhập!");
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="form-container">
            <Form onSubmit={login} style={{
                width: "71.5%", backgroundColor: "#add8e678", margin: "0 auto", padding: "2rem",
                boxShadow: "5px 5px 5px #55555599", borderRadius: "1rem", fontWeight: "bold"
            }}>
                {msg && <Alert variant="danger">{msg}</Alert>}

                <h1 className="text-center mb-3" style={{ color: "#0e3a57" }}>ĐĂNG NHẬP</h1>

                {info.map(i =>
                    <Form.Group key={i.field} className="mb-3" controlId={i.field}>
                        <Form.Label>{i.title}</Form.Label>
                        <Form.Control
                            required value={user[i.field]} onChange={(e) => setUser({ ...user, [i.field]: e.target.value })} type={i.type} placeholder={`Nhập ${i.title.toLowerCase()}`} />
                    </Form.Group>)}

                {loading ? <MySpinner /> :
                    <Form.Group className="mb-3" style={{ direction: "rtl" }}>
                        <Button style={{ backgroundColor: "#0e3a57", color: "white", border: "none", marginLeft: ".5rem" }} className="mt-2">Đăng kí</Button>
                        <Button type="submit" style={{ backgroundColor: "#0e3a57", color: "white", border: "none" }} className="mt-2">Đăng nhập</Button>
                    </Form.Group>}
            </Form>
        </div>
    );
};
export default Login;

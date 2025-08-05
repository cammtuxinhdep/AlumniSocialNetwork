import { useContext, useState } from "react";
<<<<<<< HEAD
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
=======
  import { Button, Form, Alert } from "react-bootstrap";
  import Apis, {endpoints } from "../configs/Apis";
  import { MyUserContext } from "../configs/Context";
  import { useNavigate, useSearchParams } from "react-router-dom";
  import MySpinner from "./layout/MySpinner";

  const Login = () => {
    const [, dispatch] = useContext(MyUserContext);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const info = [
      { title: "Tên đăng nhập", field: "username", type: "text" },
      { title: "Mật khẩu", field: "password", type: "password" },
    ];
    const [user, setUser] = useState({});
    const nav = useNavigate();
    const [q] = useSearchParams();

    const login = async (e) => {
      e.preventDefault();
      if (loading) return;
      if (!user.username || !user.password) {
        setError("Vui lòng nhập tên đăng nhập và mật khẩu");
        return;
      }
      try {
        setLoading(true);
        setError(null);
        console.log("Dữ liệu gửi:", JSON.stringify(user, null, 2));
        let res = await Apis.post(endpoints["login"], { ...user });
        console.log("Token:", res.data.token);
        // Tạm thời comment phần gọi profile, sẽ mở lại sau
        // let u = await authApis().get(endpoints["profile"]);
        // console.log("Profile:", u.data);
        // dispatch({ type: "login", payload: u.data });
        dispatch({
          type: "login",
          payload: {
            token: res.data.token,
            user: {
              username: user.username,
              id: "temp-id", // Tạm thời, thay bằng dữ liệu từ /api/secure/profile sau
              firstName: "User" // Tạm thời
            }
          }
        });
        let next = q.get("next");
        console.log("Điều hướng tới:", next ? next : "/");
        nav(next ? next : "/");
      } catch (ex) {
        console.error("Lỗi:", ex);
        setError(ex.response?.data || "Đăng nhập thất bại. Vui lòng thử lại.");
      } finally {
        setLoading(false);
      }
    };
>>>>>>> ed38b1fd4eb7dd41710a6669cf79db05437a6fee

    const [user, setUser] = useState({});
    const [msg, setMsg] = useState();
    const nav = useNavigate();
    const [loading, setLoading] = useState(false);

    const login = async (event) => {
        event.preventDefault();

        try {
            setLoading(true);

            let res = await Apis.post(endpoints['login'], {
                ...user
            });

            console.info(res.data);
            cookie.save('token', res.data.token);

            let u = await authApis().get(endpoints['profile']);
            console.info(u.data);

            // Lưu thêm user để giữ phiên đăng nhập
            cookie.save('user', JSON.stringify(u));

            dispatch({
                "type": "login",
                "payload": u.data
            });

            nav("/");
        } catch (ex) {
            console.error(ex);
        } finally {
            setLoading(false);
        }
    }

    return (
<<<<<<< HEAD
        <div className="form-container">
            <Form onSubmit={login} style={{
                width: "71.5%", backgroundColor: "#add8e678", margin: "0 auto", padding: "2rem",
                boxShadow: "5px 5px 5px #55555599", borderRadius: "1rem", fontWeight: "bold"
            }}>

                <h1 className="text-center mb-3" style={{ color: "#0e3a57" }}>ĐĂNG NHẬP</h1>

                {msg && <Alert variant="danger">{msg}</Alert>}

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
=======
      <>
        <h1 className="text-center text-success mt-2">ĐĂNG NHẬP</h1>
        {error && <Alert variant="danger">{error}</Alert>}
        <Form onSubmit={login}>
          {info.map((i) => (
            <Form.Group key={i.field} className="mb-3" controlId={i.field}>
              <Form.Label>{i.title}</Form.Label>
              <Form.Control
                required
                value={user[i.field] || ""}
                onChange={(e) => setUser({ ...user, [i.field]: e.target.value })}
                type={i.type}
                placeholder={i.title}
              />
            </Form.Group>
          ))}
          {loading ? (
            <MySpinner />
          ) : (
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput2">
              <Button type="submit" variant="success" disabled={loading}>
                Đăng nhập
              </Button>
            </Form.Group>
          )}
        </Form>
      </>
>>>>>>> ed38b1fd4eb7dd41710a6669cf79db05437a6fee
    );
  };

  export default Login;
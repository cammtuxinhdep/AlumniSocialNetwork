import { Button, Container, Form } from "react-bootstrap";

const Login = () => {
    const info = [{
        "title": "Tên đăng nhập",
        "field": "username",
        "type": "text"
    }, {
        "title": "Mật khẩu",
        "field": "password",
        "type": "password"
    }];

    return (
        <><body
            style={{
                backgroundImage: `url('https://res.cloudinary.com/dlnru7sj1/image/upload/v1753011517/2112812-3840x2160-desktop-4k-minimalist-background-photo_bkbjvs.jpg')`,
                backgroundSize: 'cover',
            }}
        >
        </body>
                    <Container>
                <Form
                    style={{
                        width: '71.5%',
                        backgroundColor: '#add8e678',
                        margin: '6.5rem auto',
                        padding: '2rem',
                        boxShadow: '5px 5px 5px #55555599',
                        borderRadius: '1rem',
                        fontWeight: 'bold',
                    }}
                >
                    <h1 className="text-center mb-3">ĐĂNG NHẬP</h1>
                    {info.map((item, index) => (
                        <Form.Group key={index} className="mb-3">
                            <Form.Label htmlFor={item.field}>{item.title}:</Form.Label>
                            <Form.Control
                                type={item.type}
                                id={item.field}
                                placeholder={`Nhập ${item.title.toLowerCase()}`}
                                name={item.field}
                            />
                        </Form.Group>
                    ))}
                    <Button
                        type="submit"
                        style={{ backgroundColor: '#0e3a57', color: 'white', border:"none" }}
                        className="mt-2"
                    >
                        Đăng nhập
                    </Button>
                    <Button
                        style={{ backgroundColor: '#0e3a57', color: 'white', border:"none", marginLeft: "1rem" }}
                        className="mt-2"
                    >
                        Đăng kí
                    </Button>
                </Form>
            </Container>
        </>
    );
};

export default Login;
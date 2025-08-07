import { useContext, useState } from "react";
import { Container, Nav, Navbar, NavDropdown } from "react-bootstrap";
import { MyUserContext } from "../../configs/Context";
import ChangePasswordModal from "./ChangePasswordModal";

const Header = () => {
    const [user, dispatch] = useContext(MyUserContext);
    const [changePassword, setChangePassword] = useState(false);

    return (
        <>
            <Navbar expand="sm" className="p-3" style={{ backgroundColor: "#add8e678" }}>
                <Container>
                    <Navbar.Brand href="/">ALUMNI SOCIAL NETWORK</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    {user !== null &&
                        <Navbar.Collapse id="basic-navbar-nav">
                            <Nav className="me-auto">
                                <Nav.Link href="/">Trang chủ</Nav.Link>
                                <Nav.Link href="/posts">Đăng bài</Nav.Link>
                                <Nav.Link href="/notifications">Thông báo</Nav.Link>
                                <Nav.Link href="/surveys">Khảo sát</Nav.Link>
                                <NavDropdown title="Tài khoản" id="basic-nav-dropdown">
                                    <NavDropdown.Item href="/profile">Trang cá nhân</NavDropdown.Item>
                                    <NavDropdown.Item onClick={() => setChangePassword(true)}>Đổi mật khẩu</NavDropdown.Item>
                                    <NavDropdown.Divider />
                                    <NavDropdown.Item href="/login" onClick={() => dispatch({ "type": "logout" })}>Đăng xuất</NavDropdown.Item>
                                </NavDropdown>
                            </Nav>
                        </Navbar.Collapse>
                    }
                </Container>
            </Navbar >

            <ChangePasswordModal show={changePassword} handleClose={() => setChangePassword(false)} />
        </>
    );
}

export default Header;

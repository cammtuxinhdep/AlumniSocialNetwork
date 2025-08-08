import { useContext, useState } from "react";
import { Container, Nav, Navbar, NavDropdown } from "react-bootstrap";
import { MyUserContext } from "../../configs/Context";
import ChangePasswordModal from "./ChangePasswordModal";
import { Link } from "react-router-dom";

const Header = () => {
    const [user, dispatch] = useContext(MyUserContext);
    const [changePassword, setChangePassword] = useState(false);

    return (
        <>
            <Navbar expand="sm" className="p-3" style={{ backgroundColor: "#add8e678" }}>
                <Container>
                    <Link to="/" style={{ color: "black", textDecoration: 'none', fontSize: "20px" }}>ALUMNI SOCIAL NETWORK</Link>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    {user !== null &&
                        <Navbar.Collapse id="basic-navbar-nav">
                            <Nav className="me-auto">
                                <Link to="/" className="nav-link">Trang chủ</Link>
                                <Link to="/chat" className="nav-link">Chat</Link>

                                <NavDropdown title="Tài khoản" id="basic-nav-dropdown">
                                    <Link to="/profile" className="dropdown-item">Trang cá nhân</Link>
                                    <NavDropdown.Item onClick={() => setChangePassword(true)}>Đổi mật khẩu</NavDropdown.Item>
                                    <NavDropdown.Divider />
                                    <Link to="/login" className="dropdown-item" onClick={() => dispatch({ "type": "logout" })}>Đăng xuất</Link>
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

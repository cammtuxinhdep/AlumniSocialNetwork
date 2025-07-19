import { Container, Nav, Navbar, NavDropdown } from "react-bootstrap";

const Header = () => {
    return (
        <>
            <Navbar expand="lg" className="bg-body-tertiary">
                <Container>
                    <Navbar.Brand href="/">Alumni Network</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="me-auto">
                            <Nav.Link href="/">Trang chủ</Nav.Link>
                            <Nav.Link href="/posts">Đăng bài</Nav.Link>
                            <Nav.Link href="/notifications">Thông báo</Nav.Link>
                            <NavDropdown title="Tài khoản" id="basic-nav-dropdown">
                                <NavDropdown.Item href="/profile">Trang cá nhân</NavDropdown.Item>
                                <NavDropdown.Item href="/login">Đăng nhập</NavDropdown.Item>
                                <NavDropdown.Item href="/register">Đăng ký</NavDropdown.Item>
                                <NavDropdown.Divider />
                                <NavDropdown.Item href="/logout">Đăng xuất</NavDropdown.Item>
                            </NavDropdown>
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
        </>
    );
}

export default Header;

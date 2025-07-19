import { Container } from "react-bootstrap";

const Footer = () => {
    return (
        <footer style={{ backgroundColor: "#f8f9fa", padding: "10px 0", marginTop: "auto" }}>
            <Container className="text-center">
                <small className="text-muted">
                    &copy; 2025 Alumni Social Network. Developed by CTDev.
                </small>
            </Container>
        </footer>
    );
}

export default Footer;

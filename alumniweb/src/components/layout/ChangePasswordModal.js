import { Modal, Button, Form, Alert } from "react-bootstrap";
import { useEffect, useState } from "react";
import { authApis, endpoints } from "../../configs/Apis";
import MySpinner from "./MySpinner";

const ChangePasswordModal = ({ show, handleClose }) => {
    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [msg, setMsg] = useState("");
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (show) {
            setCurrentPassword("");
            setNewPassword("");
            setConfirmPassword("");
            setMsg("");
        }
    }, [show]);

    const changePassword = async (e) => {
        e.preventDefault();

        if (newPassword !== confirmPassword) {
            setMsg("Mật khẩu xác nhận không khớp!");
            return;
        }

        try {
            setLoading(true);
            let res = await authApis().post(endpoints['changePassword'], {
                oldPassword: currentPassword,
                newPassword: newPassword
            }, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (res.status === 200) {
                setMsg("Đổi mật khẩu thành công!");
                setTimeout(() => {
                    setMsg("");
                    handleClose();
                }, 1500);
            }
        } catch (err) {
            // Lấy lỗi từ server
            let errorMsg = err.response.data.message || "Đã xảy ra lỗi.";
            setMsg(errorMsg);
        } finally {
            setLoading(false);
        }
    };

    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton style={{ backgroundColor: "#d7ecf6" }}>
                <Modal.Title>Đổi mật khẩu</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                <Form onSubmit={changePassword}>
                    {msg && <Alert variant="danger">{msg}</Alert>}

                    <Form.Group className="mb-3">
                        <Form.Label>Mật khẩu hiện tại</Form.Label>
                        <Form.Control type="password" value={currentPassword} onChange={(e) => setCurrentPassword(e.target.value)} required />
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Mật khẩu mới</Form.Label>
                        <Form.Control type="password" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} required />
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Xác nhận mật khẩu mới</Form.Label>
                        <Form.Control type="password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} required />
                    </Form.Group>

                    {loading ? <MySpinner /> :
                        <div className="text-end">
                            <Button type="submit" style={{ backgroundColor: "#0e3a57", border: "none" }}>Xác nhận</Button>
                        </div>}
                </Form>
            </Modal.Body>
        </Modal>
    );
};

export default ChangePasswordModal;

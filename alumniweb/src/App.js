import { BrowserRouter, Route, Routes } from "react-router-dom";
import Header from "./components/layout/Header";
import Footer from "./components/layout/Footer";
import Home from "./components/Home";
import Register from "./components/Register";
import Login from "./components/Login";
import PostForm from "./components/PostForm";
import { Container } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";

import { MyUserContext } from "./configs/Context";
import { useReducer } from "react";
import MyUserReducer from "./components/reducers/MyUserReducer";

const App = () => {
  const [user, dispatch] = useReducer(MyUserReducer, null);

  return (
    <MyUserContext.Provider value={[user, dispatch]}>
      <BrowserRouter>
        <Header />

        <Container className="mt-5 mb-5">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/post/:postId" element={<PostForm />} />
            <Route path="/register" element={<Register />} />
            <Route path="/login" element={<Login />} />
          </Routes>
        </Container>

        <Footer />
      </BrowserRouter>
    </MyUserContext.Provider>
  );
};

export default App;

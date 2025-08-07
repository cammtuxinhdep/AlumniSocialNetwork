import { BrowserRouter, Route, Routes } from "react-router-dom";
import Header from "./components/layout/Header";
import Footer from "./components/layout/Footer";
import Home from "./components/Home";
import Register from "./components/Register";
import Login from "./components/Login";
import { Container } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";

import { MyUserContext } from "./configs/Context";
import { useReducer } from "react";
import MyUserReducer from "./components/reducers/MyUserReducer";
import cookie from 'react-cookies';
import Profile from "./components/Profile";
import PostList from "./components/PostList";
import UsernameProfile from "./components/UsernameProfile";

const App = () => {
  const [user, dispatch] = useReducer(MyUserReducer, cookie.load('user') || null);

  return (
    <MyUserContext.Provider value={[user, dispatch]}>
      <BrowserRouter>
        <Header />

        <main>
          <Container className="form-container">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/posts" element={<PostList />} />
              <Route path="/register" element={<Register />} />
              <Route path="/login" element={<Login />} />
              <Route path="/profile" element={<Profile />} />
              <Route path="/profile/:username" element={<UsernameProfile />} />
            </Routes>
          </Container>
        </main>

        <Footer />
      </BrowserRouter>
    </MyUserContext.Provider>
  );
};

export default App;

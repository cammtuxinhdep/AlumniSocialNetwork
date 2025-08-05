import cookie from 'react-cookies';

const MyUserReducer = (current, action) => {
    // current là đối tượng user
    switch (action.type) {
        case 'login':
            // lưu user và token vào cookie
            cookie.save('token', action.payload.token);
            cookie.save('user', action.payload.user);
            return action.payload.user;

        case 'logout':
            cookie.remove('token');
            cookie.remove('user');
            return null;

        default:
            return current;
    }
}

export default MyUserReducer;

import cookie from 'react-cookies';

const MyUserReducer = (current, action) => {
    // current là đối tượng user
    switch (action.type) {
        case 'login':
            return action.payload.user;

        case 'logout':
            cookie.remove('user');
            cookie.remove('token');
            return null;

        default:
            return current;
    }
}

export default MyUserReducer;

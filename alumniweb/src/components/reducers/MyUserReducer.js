import cookie from 'react-cookies'

const MyUserReducer = (current, action) => {
    switch (action.type) {
        case 'login':
            return action.payload.user;

        case 'logout':
            cookie.remove('user');
            cookie.remove('token');
            return null;
    }

    return current;
}

export default MyUserReducer;
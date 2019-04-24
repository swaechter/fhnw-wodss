import {FETCH_ALLOCATIONS_SUCCESS, SET_ADMIN_ERROR} from "../actions";

const initialState = () => {
    return '';
};

export function admin_error(state = initialState(), action) {
    switch (action.type) {
        case SET_ADMIN_ERROR:
            return action.error;
        case FETCH_ALLOCATIONS_SUCCESS:
            return null;
        default:
            return state;
    }
}

import {CLEAR_ERROR, SET_ERROR} from "../actions";

const initialState = () => {
    return '';
};

export function error(state = initialState(), action) {
    switch (action.type) {
        case SET_ERROR:
            return action.error;
        case CLEAR_ERROR:
            return null;
        default:
            return state;
    }
}

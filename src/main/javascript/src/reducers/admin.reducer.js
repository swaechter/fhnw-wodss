import {FETCH_ADMIN_EMPLOYEES_FAIL, FETCH_ADMIN_EMPLOYEES_SUCCESS} from '../actions/admin.actions';

const initialState = () => {
    return [];
};

export function admin_employees(state = initialState(), action) {
    switch (action.type) {
        case FETCH_ADMIN_EMPLOYEES_SUCCESS:
            return action.employees;
        case FETCH_ADMIN_EMPLOYEES_FAIL:
            // TODO: Error handling
            return state;
        default:
            return state;
    }
}

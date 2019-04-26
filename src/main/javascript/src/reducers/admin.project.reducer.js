import {
    CREATE_ADMIN_PROJECTS_SUCCESS,
    DELETE_ADMIN_PROJECTS_SUCCESS,
    FETCH_ADMIN_PROJECTS_SUCCESS,
    UPDATE_ADMIN_PROJECTS_SUCCESS
} from '../actions/admin.actions';

const initialState = () => {
    return [];
};

export function admin_projects(state = initialState(), action) {
    switch (action.type) {
        case CREATE_ADMIN_PROJECTS_SUCCESS:
            let employee = action.employee;
            return [
                ...state,
                employee
            ];
        case FETCH_ADMIN_PROJECTS_SUCCESS:
            return action.employees;
        case UPDATE_ADMIN_PROJECTS_SUCCESS:
            return state;
        case DELETE_ADMIN_PROJECTS_SUCCESS:
            return state;
        default:
            return state;
    }
}

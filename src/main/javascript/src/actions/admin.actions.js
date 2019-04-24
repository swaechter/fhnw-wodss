import {doGet, getUrl} from "../services/api.service";
import {getCurrentToken} from "../services/auth.service";

/*
 * action types
 */

export const FETCH_ADMIN_EMPLOYEES_SUCCESS = 'FETCH_ADMIN_EMPLOYEES_SUCCESS';
export const FETCH_ADMIN_EMPLOYEES_FAIL = 'FETCH_ADMIN_EMPLOYEES_FAIL';

/*
 * other constants
 */


/*
 * action creators
 */

const fetchAdminEmployeesSuccess = (employees) => ({
    type: FETCH_ADMIN_EMPLOYEES_SUCCESS,
    employees
});

const fetchAdminEmployeesFail = (error) => ({
    type: FETCH_ADMIN_EMPLOYEES_FAIL,
    error
});


/**
 * async function calls
 */
export function fetchAdminEmployees() {
    return async (dispatch) => {
        let url = getUrl("/api/employee");
        try {
            let token = await getCurrentToken(dispatch);
            let json = await doGet(url, token);
            dispatch(fetchAdminEmployeesSuccess(json))
        } catch (error) {
            dispatch(fetchAdminEmployeesFail(error))
        }
    }
}

import {doDelete, doGet, doPost, doPut, getUrl} from "../services/api.service";
import {getCurrentToken} from "../services/auth.service";
import {clearError, setError} from "./error.actions";

/*
 * action types
 */

export const CREATE_ADMIN_EMPLOYEES_SUCCESS = 'CREATE_ADMIN_EMPLOYEES_SUCCESS';
export const FETCH_ADMIN_EMPLOYEES_SUCCESS = 'FETCH_ADMIN_EMPLOYEES_SUCCESS';
export const UPDATE_ADMIN_EMPLOYEES_SUCCESS = 'UPDATE_ADMIN_EMPLOYEES_SUCCESS';
export const DELETE_ADMIN_EMPLOYEES_SUCCESS = 'DELETE_ADMIN_EMPLOYEES_SUCCESS';
export const CREATE_ADMIN_CONTRACTS_SUCCESS = 'CREATE_ADMIN_CONTRACTS_SUCCESS';
export const FETCH_ADMIN_CONTRACTS_SUCCESS = 'FETCH_ADMIN_CONTRACTS_SUCCESS';
export const UPDATE_ADMIN_CONTRACTS_SUCCESS = 'UPDATE_ADMIN_CONTRACTS_SUCCESS';
export const DELETE_ADMIN_CONTRACTS_SUCCESS = 'DELETE_ADMIN_CONTRACTS_SUCCESS';
export const CREATE_ADMIN_PROJECTS_SUCCESS = 'CREATE_ADMIN_PROJECTS_SUCCESS';
export const FETCH_ADMIN_PROJECTS_SUCCESS = 'FETCH_ADMIN_PROJECTS_SUCCESS';
export const UPDATE_ADMIN_PROJECTS_SUCCESS = 'UPDATE_ADMIN_PROJECTS_SUCCESS';
export const DELETE_ADMIN_PROJECTS_SUCCESS = 'DELETE_ADMIN_PROJECTS_SUCCESS';

/*
 * other constants
 */


/*
 * action creators
 */

const createAdminEmployeesSuccess = (employee) => ({
    type: CREATE_ADMIN_EMPLOYEES_SUCCESS,
    employee
});

const fetchAdminEmployeesSuccess = (employees) => ({
    type: FETCH_ADMIN_EMPLOYEES_SUCCESS,
    employees
});

const updateAdminEmployeesSuccess = (employee) => ({
    type: UPDATE_ADMIN_EMPLOYEES_SUCCESS,
    employee
});

const deleteAdminEmployeesSuccess = (id) => ({
    type: DELETE_ADMIN_EMPLOYEES_SUCCESS,
    id
});

const createAdminContractsSuccess = (employee) => ({
    type: CREATE_ADMIN_CONTRACTS_SUCCESS,
    employee
});

const fetchAdminContractsSuccess = (employees) => ({
    type: FETCH_ADMIN_CONTRACTS_SUCCESS,
    employees
});

const updateAdminContractsSuccess = (employee) => ({
    type: UPDATE_ADMIN_CONTRACTS_SUCCESS,
    employee
});

const deleteAdminContractsSuccess = (id) => ({
    type: DELETE_ADMIN_CONTRACTS_SUCCESS,
    id
});

const createAdminProjectsSuccess = (employee) => ({
    type: CREATE_ADMIN_PROJECTS_SUCCESS,
    employee
});

const fetchAdminProjectsSuccess = (employees) => ({
    type: FETCH_ADMIN_PROJECTS_SUCCESS,
    employees
});

const updateAdminProjectsSuccess = (employee) => ({
    type: UPDATE_ADMIN_PROJECTS_SUCCESS,
    employee
});

const deleteAdminProjectsSuccess = (id) => ({
    type: DELETE_ADMIN_PROJECTS_SUCCESS,
    id
});

/**
 * async function calls
 */

export function createAdminEmployee(employee, password, role) {
    return async (dispatch) => {
        try {
            let url = getUrl("/api/employee");
            let token = await getCurrentToken(dispatch);
            url.searchParams.append('password', password);
            url.searchParams.append('role', role);
            let json = await doPost(url, employee, token);
            dispatch(clearError());
            dispatch(createAdminEmployeesSuccess(json));
            dispatch(fetchAdminEmployees());
        } catch (error) {
            dispatch(setError(error.message));
        }
    }
}

export function fetchAdminEmployees() {
    return async (dispatch) => {
        try {
            let url = getUrl("/api/employee");
            let token = await getCurrentToken(dispatch);
            let json = await doGet(url, token);
            dispatch(clearError());
            dispatch(fetchAdminEmployeesSuccess(json));
        } catch (error) {
            dispatch(setError(error.message));
        }
    }
}

export function updateAdminEmployee(id, employee) {
    return async (dispatch) => {
        try {
            let url = getUrl("/api/employee/" + id);
            let token = await getCurrentToken(dispatch);
            let json = await doPut(url, employee, token);
            dispatch(clearError());
            dispatch(updateAdminEmployeesSuccess(json));
        } catch (error) {
            dispatch(setError(error.message));
        }
    }
}

export function deleteAdminEmployee(id) {
    return async (dispatch) => {
        let url = getUrl("/api/employee/" + id);
        try {
            let token = await getCurrentToken(dispatch);
            await doDelete(url, token);
            dispatch(clearError());
            dispatch(deleteAdminEmployeesSuccess(id));
            dispatch(fetchAdminEmployees());
        } catch (error) {
            dispatch(setError(error.message));
        }
    }
}

export function createAdminContract(contract) {
    return async (dispatch) => {
        try {
            let url = getUrl("/api/contract");
            let token = await getCurrentToken(dispatch);
            let json = await doPost(url, contract, token);
            dispatch(clearError());
            dispatch(createAdminContractsSuccess(json));
            dispatch(fetchAdminContracts());
        } catch (error) {
            dispatch(setError(error.message));
        }
    }
}

export function fetchAdminContracts() {
    return async (dispatch) => {
        try {
            let url = getUrl("/api/contract");
            let token = await getCurrentToken(dispatch);
            let json = await doGet(url, token);
            dispatch(clearError());
            dispatch(fetchAdminContractsSuccess(json));
        } catch (error) {
            dispatch(setError(error.message));
        }
    }
}

export function updateAdminContract(id, contract) {
    return async (dispatch) => {
        try {
            let url = getUrl("/api/contract/" + id);
            let token = await getCurrentToken(dispatch);
            let json = await doPut(url, contract, token);
            dispatch(clearError());
            dispatch(updateAdminContractsSuccess(json));
        } catch (error) {
            dispatch(setError(error.message));
        }
    }
}

export function deleteAdminContract(id) {
    return async (dispatch) => {
        let url = getUrl("/api/contract/" + id);
        try {
            let token = await getCurrentToken(dispatch);
            await doDelete(url, token);
            dispatch(clearError());
            dispatch(deleteAdminContractsSuccess(id));
            dispatch(fetchAdminContracts());
        } catch (error) {
            dispatch(setError(error.message));
        }
    }
}

export function createAdminProject(project) {
    return async (dispatch) => {
        try {
            let url = getUrl("/api/project");
            let token = await getCurrentToken(dispatch);
            let json = await doPost(url, project, token);
            dispatch(clearError());
            dispatch(createAdminProjectsSuccess(json));
            dispatch(fetchAdminProjects());
        } catch (error) {
            dispatch(setError(error.message));
        }
    }
}

export function fetchAdminProjects() {
    return async (dispatch) => {
        try {
            let url = getUrl("/api/project");
            let token = await getCurrentToken(dispatch);
            let json = await doGet(url, token);
            dispatch(clearError());
            dispatch(fetchAdminProjectsSuccess(json));
        } catch (error) {
            dispatch(setError(error.message));
        }
    }
}

export function updateAdminProject(id, project) {
    return async (dispatch) => {
        try {
            let url = getUrl("/api/project/" + id);
            let token = await getCurrentToken(dispatch);
            let json = await doPut(url, project, token);
            dispatch(clearError());
            dispatch(updateAdminProjectsSuccess(json));
        } catch (error) {
            dispatch(setError(error.message));
        }
    }
}

export function deleteAdminProject(id) {
    return async (dispatch) => {
        let url = getUrl("/api/project/" + id);
        try {
            let token = await getCurrentToken(dispatch);
            await doDelete(url, token);
            dispatch(clearError());
            dispatch(deleteAdminProjectsSuccess(id));
            dispatch(fetchAdminProjects());
        } catch (error) {
            dispatch(setError(error.message));
        }
    }
}

import {combineReducers} from 'redux';

import {todos} from './todo.reducer';
import {auth} from './auth.reducer';
import {projects} from './projects.reducer'
import {allocations} from './allocations.reducer'
import {contracts} from './contracts.reducer'
import {admin_employees} from './admin.employee.reducer'
import {error} from "./error.reducer";

const reducers = combineReducers({
    todos,
    auth,
    projects,
    allocations,
    contracts,
    admin_employees,
    error
});

export default reducers;

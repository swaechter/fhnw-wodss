import {combineReducers} from 'redux';

import {auth} from './auth.reducer';
import {projects} from './projects.reducer'
import {allocations} from './allocations.reducer'
import {contracts} from './contracts.reducer'
import {admin_employees} from './admin.employee.reducer'
import {error} from "./error.reducer";

const reducers = combineReducers({
    auth,
    projects,
    allocations,
    contracts,
    admin_employees,
    error
});

export default reducers;

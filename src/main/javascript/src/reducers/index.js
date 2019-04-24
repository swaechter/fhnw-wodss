import { combineReducers } from 'redux';

import { todos } from './todo.reducer';
import { auth } from './auth.reducer';
import { projects } from './projects.reducer'
import { allocations } from './allocations.reducer'
import { contracts } from './contracts.reducer'
import { admin_employees } from './admin.employee.reducer'
import { admin_error} from "./admin.error.reducer";

const reducers = combineReducers({
	todos,
	auth,
	projects,
	allocations,
	contracts,
    admin_employees,
    admin_error
});

export default reducers;

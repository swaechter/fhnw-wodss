import 'bootstrap/dist/css/bootstrap.min.css';
import './style.css';

import {createHashHistory} from 'history';
import {Component} from 'preact';
import {Router} from 'preact-router';
import Redirect from '../components/redirect';
import AuthLock from '../components/auth-lock';
import ProjectPage from './projects/projects';
import ProjectCreatePage from './projects/project-create';
import ProjectManagePage from './projects/project-manage';
import ProjectAllocationPage from './projects/project-allocation';
import MyAllocationsPage from './my-allocations/my-allocations';
import ManageEmployeesPage from './employees/manage-employees';
import CreateEmployeePage from './employees/create-employee';
import UpdateEmployeePage from './employees/update-employee';
import ManageContractsPage from './contracts/manage-contracts';
import CreateContractPage from './contracts/create-contract';

export default class App extends Component {

    render() {
        return (
            <AuthLock>
                <Router history={createHashHistory()}>
                    <MyAllocationsPage path='/my-allocation'/>
                    <ProjectPage path='/project'/>
                    <ProjectCreatePage path='/project/create'/>
                    <ProjectManagePage path='/project/manage/:id'/>
                    <ProjectAllocationPage path='/project/allocation/:id'/>
                    <ManageEmployeesPage path='/admin/employees'/>
                    <CreateEmployeePage path='/admin/employees/create'/>
                    <UpdateEmployeePage path='/admin/employees/update/:id'/>
                    <ManageContractsPage path='/admin/contracts'/>
                    <CreateContractPage path='/admin/contracts/create'/>
                    <Redirect default to='/my-allocation'/>
                </Router>
            </AuthLock>
        );
    }
}

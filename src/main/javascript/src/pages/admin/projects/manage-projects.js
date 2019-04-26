import {Component} from 'preact';
import {connect} from 'preact-redux';

import reducers from '../../../reducers';
import * as actions from '../../../actions';
import Layout from '../../../components/layout';
import RoleLock from '../../../components/role-lock';
import {Link} from "preact-router/match";
import Error from "../../../components/error";

@connect(reducers, actions)
export default class ManageProjectsPage extends Component {

    constructor(props) {
        super(props);
    }

    componentDidMount() {
        this.props.fetchAdminProjects();
        this.props.fetchAdminEmployees();
    }

    deleteProject(id) {
        this.props.deleteAdminProject(id);
    }

    getEmployeeNameForProject(project) {
        let value = project.projectManagerId;
        this.props.admin_employees.map(employee => {
            if (employee.id === project.projectManagerId) {
                value = employee.firstName + " " + employee.lastName;
            }
        });
        return value;
    }

    render() {
        return (
            <Layout>
                <RoleLock allowedRoles={['Administrator']}>
                    <Error>
                        <h2>Manage Projects</h2>
                        <table class="table ">
                            <thead>
                            <tr>
                                <th scope="col">Name</th>
                                <th scope="col">FTE</th>
                                <th scope="col">Start Date</th>
                                <th scope="col">End Date</th>
                                <th scope="col">Project Manager</th>
                                <th scope="col">
                                    <Link className="btn btn-success" href="/admin/projects/create"
                                          role="button">Create</Link>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            {this.props.admin_projects.map(project => (
                                <tr key={project.id}>
                                    <td>{project.name}</td>
                                    <td>{project.ftePercentage / 100}</td>
                                    <td>{project.startDate}</td>
                                    <td>{project.endDate}</td>
                                    <td>{this.getEmployeeNameForProject(project)}</td>
                                    <td>
                                        <button className="btn btn-danger" onClick={() => {
                                            this.deleteProject(project.id)
                                        }}>Delete
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </Error>
                </RoleLock>
            </Layout>
        );
    }
}

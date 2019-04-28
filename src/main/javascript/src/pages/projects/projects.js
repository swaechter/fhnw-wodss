import {Component} from "preact";

import reducers from "../../reducers";
import * as actions from "../../actions";
import {connect} from "preact-redux";
import Layout from "../../components/layout";
import {Link} from "preact-router/match";
import Error from "../../components/error";
import OptionalRoleLock from "../../components/optional-role-lock";

@connect(reducers, actions)
export default class ProjectPage extends Component {

    componentDidMount() {
        this.props.clearError();
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

    render(props, state) {
        if (props.admin_projects) {
            return (
                <Layout>
                    <h2>Projects</h2>
                    <Error>
                        <table className="table ">
                            <thead>
                            <tr>
                                <th scope="col">Name</th>
                                <th scope="col">Start Date</th>
                                <th scope="col">End Date</th>
                                <th scope="col">FTE</th>
                                <th scope="col">Project Manager</th>
                                <th>
                                    <OptionalRoleLock allowedRoles={['Administrator']}>
                                        <Link className="btn btn-success" href="/project/create"
                                              role="button">Create</Link>
                                    </OptionalRoleLock>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            {props.admin_projects.map(project => (
                                <tr key={project.id}>
                                    <td>{project.name}</td>
                                    <td>{project.startDate}</td>
                                    <td>{project.endDate}</td>
                                    <td>{project.ftePercentage / 100}</td>
                                    <td>{this.getEmployeeNameForProject(project)}</td>
                                    <td>
                                        {(props.auth.employee.role === "ADMINISTRATOR" || props.auth.employee.id === project.projectManagerId)
                                            ? (
                                                <Link className="btn btn-primary" href={`/project/manage/${project.id}`}
                                                      role="button">Manage</Link>)
                                            : null}
                                        &nbsp;
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
                </Layout>
            );
        }
    }
}

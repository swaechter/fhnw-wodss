import {Component} from "preact";

import reducers from "../../reducers";
import * as actions from "../../actions";
import {connect} from "preact-redux";
import Layout from "../../components/layout";
import {Link} from "preact-router/match";
import Error from "../../components/error";
import { loginState } from "../../actions";

@connect(reducers, actions)
export default class ProjectPage extends Component {
    componentDidMount() {
        this.props.clearError();
        this.props.fetchProjectsAsync();
    }

    render(props, state) {
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
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        {props.projects.map(project => (
                            <tr key={project.id}>
                                <td>{project.name}</td>
                                <td>{project.startDate}</td>
                                <td>{project.endDate}</td>
                                <td>
                                    {(props.auth.employee.role === "ADMINISTRATOR" || props.auth.employee.id === project.projectManagerId)
                                      ?(
                                    <Link className="btn btn-primary" href={`/project/manage/${project.id}`}
                                          role="button">Manage</Link>)
                                      : null}
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

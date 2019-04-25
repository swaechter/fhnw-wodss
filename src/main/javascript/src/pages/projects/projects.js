import React, { render, Component } from "preact";

import reducers from "../../reducers";
import * as actions from "../../actions";
import { connect } from "preact-redux";
import Layout from "../../components/layout";
import { Link } from "preact-router/match";
import Error from "../../components/error";

@connect(reducers, actions)
class ProjectPage extends Component {
  componentDidMount() {
    this.props.clearError();
    this.props.fetchProjectsAsync();
  }

  render() {
    return (
      <Layout>
        <h2>ProjectPage</h2>
        <Error>
            <table className="table ">
              <thead>
              <tr>
                <th scope="col">Id</th>
                <th scope="col">Name</th>
                <th scope="col">Start Date</th>
                <th scope="col">End Date</th>
                <th scope="col">Project Manager Id</th>
                <th>
                  Actions
                </th>
              </tr>
              </thead>
              <tbody>
              {this.props.projects.map(project => (
                <tr key={project.id}>
                  <td>{project.id}</td>
                  <td>{project.name}</td>
                  <td>{project.startDate}</td>
                  <td>{project.endDate}</td>
                  <td>{project.projectManagerId}</td>
                  <td>
                      <Link className="btn btn-primary" href="/project/{project.id}" role="button">Show</Link>
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

export default ProjectPage;
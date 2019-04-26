import {Component} from 'preact';
import Layout from "../../../components/layout";
import RoleLock from "../../../components/role-lock";
import {connect} from "preact-redux";
import reducers from "../../../reducers";
import * as actions from "../../../actions";
import Error from "../../../components/error";
import {Link} from "preact-router/match";

@connect(reducers, actions)
export default class CreateProjectPage extends Component {

    constructor(props) {
        super(props);
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        this.clearFields();
        this.props.clearError();
        this.props.fetchAdminEmployees();
    }

    clearFields() {
        this.setState({
            'name': '',
            'ftePercentage': 0,
            'startDate': '',
            'endDate': '',
            'projectManagerId': ''
        });
    }

    handleChange(event) {
        const id = event.target.id;
        const value = event.target.value;
        this.setState({
            [id]: value
        });
    }

    handleSubmit(event) {
        this.props.createAdminProject({
            'name': this.state.name,
            'ftePercentage': this.state.ftePercentage * 100,
            'startDate': this.state.startDate,
            'endDate': this.state.endDate,
            'projectManagerId': this.state.projectManagerId
        });
        this.clearFields();
        event.preventDefault();
    }

    render() {
        return (
            <Layout>
                <RoleLock allowedRoles={['Administrator']}>
                    <Error>
                        <h2>Create Project</h2>
                        <form>
                            <div className="form-group">
                                <label htmlFor="name">Name</label>
                                <input className="form-control" id="name" type="text" placeholder="Enter project name"
                                       required minLength="1" maxLength="50"
                                       value={this.state.name} onChange={this.handleChange}/>
                            </div>
                            <div className="form-group">
                                <label htmlFor="ftePercentage">FTE</label>
                                <input className="form-control" id="ftePercentage" type="number"
                                       placeholder="Enter FTE"
                                       required value={this.state.ftePercentage} onChange={this.handleChange}/>
                            </div>
                            <div className="form-group">
                                <label htmlFor="startDate">Start Date</label>
                                <input className="form-control" id="startDate" type="date"
                                       placeholder="Enter start date"
                                       required value={this.state.startDate} onChange={this.handleChange}/>
                            </div>
                            <div className="form-group">
                                <label htmlFor="endDate">End Date</label>
                                <input className="form-control" id="endDate" type="date"
                                       placeholder="Enter end date"
                                       required value={this.state.endDate} onChange={this.handleChange}/>
                            </div>
                            <div className="form-group">
                                <label htmlFor="projectManagerId">Project Manager</label>
                                <select className="form-control" id="projectManagerId" required
                                        value={this.state.projectManagerId} onChange={this.handleChange}>
                                    {this.props.admin_employees.filter(i => i.active === true && i.role !== 'DEVELOPER').map(employee =>
                                        <option key={employee.id}
                                                value={employee.id}>{employee.firstName} {employee.lastName} ({employee.emailAddress})</option>
                                    )};
                                </select>
                            </div>
                            <Link href="/admin/projects" role="button">Back to overview</Link>
                            <button onClick={this.handleSubmit} type="submit"
                                    className="btn btn-primary float-right">Create
                            </button>
                        </form>
                    </Error>
                </RoleLock>
            </Layout>
        );
    }
}

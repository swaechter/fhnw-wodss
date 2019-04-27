import {Component} from 'preact';
import Layout from "../../../components/layout";
import RoleLock from "../../../components/role-lock";
import {connect} from "preact-redux";
import reducers from "../../../reducers";
import * as actions from "../../../actions";
import Error from "../../../components/error";
import {Link} from "preact-router/match";

@connect(reducers, actions)
export default class CreateContractPage extends Component {

    constructor(props) {
        super(props);
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        this.clearFields();
        this.props.clearError();
        this.props.fetchAdminEmployees();
        this.props.fetchAdminContracts();
    }

    clearFields() {
        this.setState({
            'startDate': '',
            'endDate': '',
            'pensumPercentage': '',
            'employeeId': ''
        });
    }

    handleChange(event) {
        const id = event.target.id;
        const value = event.target.value;
        this.setState({
            [id]: value
        });
    }

    handleSubmit() {
        this.props.createAdminContract({
            'startDate': this.state.startDate,
            'endDate': this.state.endDate,
            'pensumPercentage': this.state.pensumPercentage,
            'employeeId': this.state.employeeId
        });
        this.clearFields();
    }

    render() {
        return (
            <Layout>
                <RoleLock allowedRoles={['Administrator']}>
                    <Error>
                        <h2>Create Contract</h2>
                        <form onSubmit={event => this.handleSubmit(event)}>
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
                                <label htmlFor="pensumPercentage">Pensum Percentage</label>
                                <input className="form-control" id="pensumPercentage" type="number"
                                       placeholder="Enter pensum percentage"
                                       required min="0" max="100"
                                       value={this.state.pensumPercentage} onChange={this.handleChange}/>
                            </div>
                            <div className="form-group">
                                <label htmlFor="employeeId">Employee</label>
                                <select className="form-control" id="employeeId" required
                                        value={this.state.employeeId} onChange={this.handleChange}>
                                    {this.props.admin_employees.filter(i => i.active === true).map(employee =>
                                        <option key={employee.id}
                                                value={employee.id}>{employee.firstName} {employee.lastName} ({employee.emailAddress})</option>
                                    )};
                                </select>
                            </div>
                            <Link href="/admin/contracts" role="button">Back to overview</Link>
                            <button type="submit" className="btn btn-primary float-right">Create
                            </button>
                        </form>
                    </Error>
                </RoleLock>
            </Layout>
        );
    }
}

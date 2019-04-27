import {Component} from 'preact';
import {connect} from "preact-redux";
import Layout from "../../components/layout";
import reducers from "../../reducers";
import * as actions from "../../actions";
import RoleLock from "../../components/role-lock";
import Error from "../../components/error";
import {Link} from "preact-router/match";
import CustomError from "../../components/error-custom";


@connect(reducers, actions)
export default class ProjectAllocationPage extends Component {

    constructor(props) {
        super(props);

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        this.clearFields();
        this.props.clearError();
        this.props.fetchAdminProjects();
        this.props.fetchAdminEmployees();
        this.props.fetchAdminContracts();
        this.setState({
            projectId: this.props.id
        });
    }

    clearFields() {
        this.setState({
            contractId: null,
            projectId: null,
            startDate: null,
            endDate: null,
            pensumPercentage: null
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
        this.props.createAllocationAsync({
            'contractId': this.state.contractId,
            'projectId': this.props.id,
            'startDate': this.state.startDate,
            'endDate': this.state.endDate,
            'pensumPercentage': this.state.pensumPercentage
        });
    }

    getEmployeesWithTheirContractsAsObjects = () => {
        let data = [];
        this.props.admin_employees.map(employee => {
            this.props.admin_contracts.filter(contract => contract.employeeId === employee.id).map(contract => {
                data.push({
                    "key": contract.id,
                    "text": employee.firstName + " " + employee.lastName + " with " + contract.pensumPercentage + "% (" + contract.startDate + " - " + contract.endDate + ")"
                });
            });
        });
        return data;
    }

    render() {
        const project = this.props.admin_projects.find(item => item.id === this.props.id && item.projectManagerId === this.props.auth.employee.id);
        if (this.props.error !== 'undefined' && this.props.error != null && this.props.error.length > 0) {
            return (
                <Layout>
                    <RoleLock allowedRoles={['Administrator']}>
                        <Error>
                            <Link href="/project" role="button">Back to overview</Link>
                        </Error>
                    </RoleLock>
                </Layout>);
        }
        if (this.props.admin_projects.length === 0 || (this.props.admin_projects.length > 0 && project)) {
            return (
                <Layout>
                    <RoleLock allowedRoles={['Administrator']}>
                        <Error>
                            <h2>Create Allocation</h2>
                            <form onSubmit={event => this.handleSubmit(event)}>
                                <div className="form-group">
                                    <label htmlFor="contractId">Employee Contracts</label>
                                    <select className="form-control" id="contractId" required
                                            value={this.state.contractId} onChange={this.handleChange}>
                                        {this.getEmployeesWithTheirContractsAsObjects().map(data =>
                                            <option key={data.key} value={data.key}>{data.text}</option>
                                        )};
                                    </select>
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
                                    <label htmlFor="pensumPercentage">Pensum Percentage</label>
                                    <input className="form-control" id="pensumPercentage" type="number"
                                           placeholder="Enter pensum percentage"
                                           required min="0" max="100"
                                           value={this.state.pensumPercentage} onChange={this.handleChange}/>
                                </div>
                                <Link href="/admin/employees" role="button">Back to overview</Link>
                                <button className="btn btn-primary float-right">Create
                                </button>
                            </form>
                        </Error>
                    </RoleLock>
                </Layout>
            );
        } else {
            return (
                <Layout>
                    <RoleLock allowedRoles={['Administrator']}>
                        <CustomError message={"Project not found"}>
                            <Link href="/project" role="button">Back to overview</Link>
                        </CustomError>
                    </RoleLock>
                </Layout>
            );
        }
    }
}

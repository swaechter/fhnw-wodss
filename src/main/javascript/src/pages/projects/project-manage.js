import { h, Component } from "preact";
import { connect } from "preact-redux";
import Layout from "../../components/layout";
import reducers from "../../reducers";
import * as actions from "../../actions";
import Error from "../../components/error";
import { Link } from "preact-router/match";
import { getBusinessDays } from "../../utils/date";

@connect(
    reducers,
    actions
)
export default class ProjectManagePage extends Component {
    componentDidMount() {
        this.props.fetchProjectAsyncById(this.props.id);
        this.props.fetchAllocationsAsync(null, this.props.id, null, null);
        this.props.fetchContractsAsync();
        this.props.fetchAllAdminEmployees();
    }

    calculateTotalAllocatedFtes = (allocations) => {
        let reducer = (acc, allocation) => acc + getBusinessDays(allocation.startDate, allocation.endDate).length * allocation.pensumPercentage;
        return allocations.reduce(reducer, 0);
    };

    calculateAllocatedFtesPerEmployee = (allocations, contracts, employees) => {
        return allocations.reduce((acc, allocation) => {
            let allocatedEmployee = employees.find(employee => employee.id === contracts.find(c => c.id === allocation.contractId).employeeId);
            let allocatedFtes = getBusinessDays(allocation.startDate, allocation.endDate).length * allocation.pensumPercentage;
            if (acc[allocatedEmployee.id]) {
                acc[allocatedEmployee.id].workingDays += allocatedFtes;
            }
            else {
                acc[allocatedEmployee.id] = {employee: allocatedEmployee, workingDays: allocatedFtes};
            }
            return acc;
        }, {});
};



    render(props, state) {
        if (props.projects && props.projects.length > 0
          && props.allocations && props.allocations.length > 0
        && props.contracts && props.contracts.length > 0
        && props.admin_employees && props.admin_employees.length > 0) {
            const project = props.projects[0];
            const allocatedEmployees = this.calculateAllocatedFtesPerEmployee(props.allocations, props.contracts, props.admin_employees);
            const progress = this.calculateTotalAllocatedFtes(props.allocations) / project.ftePercentage * 100;
            const allocatedDevelopersTableData = Object.entries(allocatedEmployees).forEach(([key, value]) => {
                console.log(key, value.employee.firstName, value.employee.lastName, value.employee.role, value.workingDays / 100);
            return <tr key={key}><td>{value.employee.firstName}</td>
            <td>{value.employee.lastName}</td><td>{value.employee.role}</td>
            <td>{value.workingDays / 100}</td></tr>
        });
            return (
                <Layout>
                    <h2>Projects</h2>
                    <Error>
                        <div className="progress">
                            <div
                                className="progress-bar bg-success"
                                role="progressbar"
                                style={{width: progress +'%'}}
                                aria-valuenow={this.calculateTotalAllocatedFtes(props.allocations)}
                                aria-valuemin="0"
                                aria-valuemax={project.ftePercentage}
                            />
                        </div>
                        <h3 style={{marginTop: 20}}>Allocated Developers</h3>
                        <table className="table ">
                            <thead>
                            <tr>
                                <th scope="col">First Name</th>
                                <th scope="col">Last Name</th>
                                <th scope="col">Role</th>
                                <th scope="col">Working Days</th>
                            </tr>
                            </thead>
                            <tbody>
                            {allocatedDevelopersTableData}
                            </tbody>
                        </table>
                        <h3 style={{marginTop: 20}}>Allocations </h3>
                        <table className="table ">
                            <thead>
                                <tr>
                                    <th scope="col">Start Date</th>
                                    <th scope="col">End Date</th>
                                    <th scope="col">Pensum Percentage</th>
                                    <th scope="col">Contract Id</th>
                                    <th scope="col">Project Id</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {props.allocations.map(allocation => (
                                    <tr key={allocation.id}>
                                        <td>
                                            {allocation.startDate.toDateString()}
                                        </td>
                                        <td>
                                            {allocation.endDate.toDateString()}
                                        </td>
                                        <td>{allocation.pensumPercentage}</td>
                                        <td>{allocation.contractId}</td>
                                        <td>{allocation.projectId}</td>
                                        <td>
                                            <Link
                                                activeClassName="btn btn-primary"
                                                href={`/project/allocation/${
                                                    props.id
                                                }`}
                                                role="button"
                                            >
                                                Create
                                            </Link>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </Error>
                </Layout>
            );
        } else {
            return <h1>Loading...</h1>;
        }
    }
}

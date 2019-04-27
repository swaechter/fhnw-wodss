import {Component} from "preact";
import {connect} from "preact-redux";
import Layout from "../../components/layout";
import reducers from "../../reducers";
import * as actions from "../../actions";
import Error from "../../components/error";
import {Link} from "preact-router/match";
import {getBusinessDays} from "../../utils/date";

@connect(reducers, actions)
export default class ProjectManagePage extends Component {

    componentDidMount() {
        this.props.fetchProjectAsyncById(this.props.id);
        this.props.fetchAllocationsAsync(null, this.props.id, null, null);
        this.props.fetchContractsAsync();
        this.props.fetchAdminEmployees();
    }

    calculateTotalAllocatedFtes = (allocations) => {
        let reducer = (acc, allocation) => acc + getBusinessDays(allocation.startDate, allocation.endDate).length * allocation.pensumPercentage;
        return allocations.reduce(reducer, 0);
    };

    calculateAllocatedFtesPerEmployee = (allocations, contracts, employees) => {
        // Calculate the values via reduce
        var allocatedFtesPerEmployee = allocations.reduce((acc, allocation) => {
            let allocatedEmployee = employees.find(employee => employee.id === contracts.find(c => c.id === allocation.contractId).employeeId);
            let allocatedFtes = getBusinessDays(allocation.startDate, allocation.endDate).length * allocation.pensumPercentage;
            if (acc[allocatedEmployee.id]) {
                acc[allocatedEmployee.id].workingDays += allocatedFtes;
            } else {
                acc[allocatedEmployee.id] = {employee: allocatedEmployee, workingDays: allocatedFtes};
            }
            return acc;
        }, {});

        // Convert to array so we can use the map function on it
        var data = [];
        Object.entries(allocatedFtesPerEmployee).forEach(([key, value]) => {
            data.push({
                'id': value.employee.id,
                'firstName': value.employee.firstName,
                'lastName': value.employee.lastName,
                'emailAddress': value.employee.emailAddress,
                'role': value.employee.role,
                'workingDays': value.workingDays
            });
        });
        return data;
    };

    getEmployeeNameForAllocation(allocation) {
        let value = allocation.contractId;
        this.props.contracts.filter(contract => contract.id === allocation.contractId).map(contract => {
            this.props.admin_employees.filter(employee => employee.id === contract.employeeId).map(employee => {
                value = employee.firstName + " " + employee.lastName;
            })
        });
        return value;
    }

    deleteAllocation(id) {
        this.props.deleteAllocationAsync(id);
    }

    render(props, state) {
        if (props.projects && props.projects.length > 0 && props.allocations && props.contracts && props.admin_employees) {
            const project = props.projects[0];
            const allocatedEmployees = this.calculateAllocatedFtesPerEmployee(props.allocations, props.contracts, props.admin_employees);
            const progress = this.calculateTotalAllocatedFtes(props.allocations) / project.ftePercentage * 100;
            return (
                <Layout>
                    <h2>Projects</h2>
                    <Error>
                        <div className="progress">
                            <div
                                className="progress-bar bg-success"
                                role="progressbar"
                                style={{width: progress + '%'}}
                                aria-valuenow={this.calculateTotalAllocatedFtes(props.allocations)}
                                aria-valuemin="0"
                                aria-valuemax={project.ftePercentage}
                            />
                        </div>
                        <h3 style={{marginTop: 20}}>Assigned Developers</h3>
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
                            {allocatedEmployees.map(allocatedEmployee => (
                                <tr key={allocatedEmployee.id}>
                                    <td>{allocatedEmployee.firstName}</td>
                                    <td>{allocatedEmployee.lastName}</td>
                                    <td>{allocatedEmployee.role}</td>
                                    <td>{allocatedEmployee.workingDays / 100}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                        <h3 style={{marginTop: 20}}>Allocations </h3>
                        <table className="table ">
                            <thead>
                            <tr>
                                <th scope="col">Start Date</th>
                                <th scope="col">End Date</th>
                                <th scope="col">Pensum Percentage</th>
                                <th scope="col">Employee</th>
                                <th scope="col">
                                    <Link className="btn btn-primary" href={`/project/allocation/${project.id}`}
                                          role="button">Assign</Link>
                                </th>
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
                                    <td>{this.getEmployeeNameForAllocation(allocation)}</td>
                                    <td>
                                        <button className="btn btn-danger" onClick={() => {
                                            this.deleteAllocation(allocation.id)
                                        }}>Delete
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                        <Link href="/project" role="button">Back to projects</Link>
                    </Error>
                </Layout>
            );
        } else {
            return <h1>Loading...</h1>;
        }
    }
}

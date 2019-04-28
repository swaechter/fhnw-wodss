import {Component} from "preact";
import {connect} from "preact-redux";
import Layout from "../../components/layout";
import reducers from "../../reducers";
import * as actions from "../../actions";
import Error from "../../components/error";
import {Link} from "preact-router/match";
import {getBusinessDays} from "../../utils/date";
import CustomError from "../../components/error-custom";

@connect(reducers, actions)
export default class ProjectManagePage extends Component {

    componentDidMount() {
        this.props.fetchAdminEmployees();
        this.props.fetchAllocationsAsync(null, this.props.id, null, null);
        this.props.fetchContractsAsync();
        this.props.fetchProjectAsyncById(this.props.id);
    }

    calculateTotalAllocatedFtes = (allocations) => {
        if (this.isEmpty(allocations)) {
            return {}
        }
        let reducer = (acc, allocation) => acc + getBusinessDays(allocation.startDate, allocation.endDate).length * allocation.pensumPercentage;
        return allocations.reduce(reducer, 0);
    };

    calculateAllocatedFtesPerEmployee = (allocations, contracts, employees) => {
        if (this.isEmpty(allocations) || this.isEmpty(contracts) || this.isEmpty(employees)) {
            return {}
        }
        return allocations.reduce((acc, allocation) => {
            let allocatedEmployee = employees.find(employee => employee.id === contracts.find(c => c.id === allocation.contractId).employeeId);
            let allocatedFtes = getBusinessDays(allocation.startDate, allocation.endDate).length * allocation.pensumPercentage;
            if (acc[allocatedEmployee.id]) {
                acc[allocatedEmployee.id].workingDays += allocatedFtes;
            } else {
                acc[allocatedEmployee.id] = {employee: allocatedEmployee, workingDays: allocatedFtes};
            }
            return acc;
        }, {});
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

    getEmployeeNameForProject(project) {
        let value = project.projectManagerId;
        this.props.admin_employees.map(employee => {
            if (employee.id === project.projectManagerId) {
                value = employee.firstName + " " + employee.lastName;
            }
        });
        return value;
    }

    deleteAllocation(id) {
        this.props.deleteAllocationAsync(id);
    }

    isEmpty = obj => {
        return Object.entries(obj).length === 0 && obj.constructor;
    }

    render(props, state) {
        const allLoaded = !(this.isEmpty(props.projects) || this.isEmpty(props.admin_employees));
        const project = props.projects.find(project => project.id === props.id);
        const isManager = project && (project.projectManagerId === props.auth.employee.id || props.auth.employee.role === 'ADMINISTRATOR');

        if (allLoaded && !isManager) {
            return (
                <Layout>
                    <CustomError message={'You are not manager of this Project'}/>
                </Layout>
            )
        } else if (allLoaded && isManager) {
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
                        <h3 style={{marginTop: 20}}>Project Information</h3>
                        <p><b>Name:</b>&nbsp;{project.name}</p>
                        <p><b>Start Date:</b>&nbsp;{project.startDate}</p>
                        <p><b>End Date:</b>&nbsp;{project.endDate}</p>
                        <p><b>Required FTE:</b>&nbsp;{project.ftePercentage / 100}</p>
                        <p><b>Project Manager:</b>&nbsp;{this.getEmployeeNameForProject(project)}</p>
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
                            {Array.from(Object.entries(allocatedEmployees)).map(([employeeId, result]) => {
                                return (
                                    <tr key={result.employeeId}>
                                        <td>{result.employee.firstName}</td>
                                        <td>{result.employee.lastName}</td>
                                        <td>{result.employee.role}</td>
                                        <td>{result.workingDays / 100}</td>
                                    </tr>
                                )
                            })}
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
            return (
                <Layout>
                    <h1>Loading...</h1>
                </Layout>
            );
        }
    }
}

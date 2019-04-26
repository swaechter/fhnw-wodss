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
    }

    calculateAllocatedFtes = (allocations) => {
        let reducer = (acc, allocation) => acc + getBusinessDays(allocation.startDate, allocation.endDate).length * allocation.pensumPercentage;
        return allocations.reduce(reducer, 0);
    };

    render(props, state) {
        if (props.projects && props.projects.length > 0 && props.allocations && props.allocations.length > 0) {
            const project = props.projects[0];
            console.log(this.calculateAllocatedFtes(props.allocations));
            const progress = this.calculateAllocatedFtes(props.allocations) / project.ftePercentage * 100;
            return (
                <Layout>
                    <h2>Projects</h2>
                    <Error>
                        <div className="progress">
                            <div
                                className="progress-bar bg-success"
                                role="progressbar"
                                style={{width: progress +'%'}}
                                aria-valuenow={this.calculateAllocatedFtes(props.allocations)}
                                aria-valuemin="0"
                                aria-valuemax={project.ftePercentage}
                            />
                        </div>
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
        // return (
        //     <Layout>
        //         <h2>Projects</h2>
        //         <Error>
        //             <h3>Allocations</h3>
        //             <table className="table ">
        //                 <thead>
        //                     <tr>
        //                         <th scope="col">Start Date</th>
        //                         <th scope="col">End Date</th>
        //                         <th scope="col">Pensum Percentage</th>
        //                         <th scope="col">Contract Id</th>
        //                         <th scope="col">Project Id</th>
        //                         <th>Action</th>
        //                     </tr>
        //                 </thead>
        //                 <tbody>
        //                     {props.allocations.map(allocation => (
        //                         <tr key={allocation.id}>
        //                             <td>
        //                                 {allocation.startDate.toDateString()}
        //                             </td>
        //                             <td>{allocation.endDate.toDateString()}</td>
        //                             <td>{allocation.pensumPercentage}</td>
        //                             <td>{allocation.contractId}</td>
        //                             <td>{allocation.projectId}</td>
        //                             <td>
        //                                 <Link
        //                                     activeClassName="btn btn-primary"
        //                                     href={`/project/allocation/${
        //                                         props.id
        //                                     }`}
        //                                     role="button"
        //                                 >
        //                                     Create
        //                                 </Link>
        //                             </td>
        //                         </tr>
        //                     ))}
        //                 </tbody>
        //             </table>
        //         </Error>
        //     </Layout>
        // );
    }
}

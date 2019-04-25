import { h, Component } from 'preact';

import reducers from '../../reducers';
import * as actions from '../../actions';
import { connect } from 'preact-redux';
import Layout from '../../components/layout';
import DayContainer from './day-container';
import { filterAllocations } from '../../utils/filters';
import { getMonday, getDateRange, removeTimeUTC, checkDateRangeOverlap, addDays } from '../../utils/date';
import { getObjectColor } from '../../utils/colors';
import FromToDatePicker from '../../components/from-to-datepicker';

@connect(reducers, actions)
export default class MyAllocationsPage extends Component {

    constructor(props) {
        super(props)
        let today = removeTimeUTC(new Date())
        let monday = getMonday(today)
        let friday = addDays(monday, 4)
        this.state = {
            from: monday,
            to: friday,
            displayAllocations: []
        }
    }

    componentDidMount() {
        this.props.fetchProjectsAsync();
        this.props.fetchContractsAsync();
        this.props.fetchAllocationsAsync(this.props.auth.employee.id);
    }

    updateDisplayElements = (allocations, projects, contracts) => {
        let dates = getDateRange(this.state.from, this.state.to)
        let reducedAllocations = filterAllocations(this.state.from, this.state.to, allocations)
        let displayAllocations = this.createOutputDataStructure(dates, reducedAllocations, projects, contracts)
        return displayAllocations
    }

    createOutputDataStructure = (dates, allocations, projects, contracts) => {
        let result = []
        dates.map((date) => {
            let activeAlloc = filterAllocations(date, date, allocations)
            let displayAlloc = activeAlloc.map((alloc) => this.allocationToDisplayallocation(alloc, projects, contracts))
            result.push({
                'date': date,
                'allocations': displayAlloc
            })
        })
        return result
    }

    allocationToDisplayallocation = (alloc, projects, contracts) => {
        let contractPercentage = contracts.find((contract) => contract.id==alloc.contractId).pensumPercentage
        let project = projects.find((project) => project.id == alloc.projectId)
        let projectName = project? project.name : ''
        return {
            'pensumPercentage': 100*alloc.pensumPercentage/contractPercentage,
            'color': getObjectColor(projectName),
            'projectName': projectName
        }
    }

    updateDateRange = (from, to) => {
        this.setState({
            ...this.state,
            from: removeTimeUTC(from),
            to: removeTimeUTC(to)
        })
    }


    render({ allocations, projects, contracts }) {
        let displayAllocation = this.updateDisplayElements(allocations, projects, contracts)
        return (
            <Layout>
                <h2>My Allocations</h2>
                <FromToDatePicker onRangeUpdated={this.updateDateRange} from={this.state.from} to={this.state.to}/>
                {
                    displayAllocation.map(item =>
                        <DayContainer date={item.date} allocations={item.allocations} />)
                }
            </Layout>
        );
    }
}
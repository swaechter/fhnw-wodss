import { h, Component } from 'preact';

import reducers from '../../reducers';
import * as actions from '../../actions';
import { connect } from 'preact-redux';
import Layout from '../../components/layout';
import DayContainer from './day-container';
import { filterAllocations, unique } from '../../utils/filters';
import { getMonday, getDateRange } from '../../utils/date';
import { getObjectColor } from '../../utils/colors';

@connect(reducers, actions)
export default class MyAllocationsPage extends Component {

    constructor(props) {
        super(props)
        let monday = getMonday(new Date('Sat Apr 06 2019')).setHours(0, 0, 0, 0)
        this.state = {
            from: Date.UTC(2019,3,4),
            to:   Date.UTC(2019,4,5),
            displayAllocations: []
        }
        console.log(this.state)
    }

    componentDidMount() {
        this.props.fetchProjectsAsync();
        this.props.fetchAllocationsAsync();
    }

    updateDisplayElements = (allocations, projects) =>  {
        let dates = getDateRange(this.state.from, this.state.to)
        let reducedAllocations = filterAllocations(this.state.from, this.state.to, allocations)
        let displayAllocations = this.createOutputDataStructure(dates, reducedAllocations, projects)
        return displayAllocations
    }

    createOutputDataStructure = (dates, allocations, projects) => {
        let result = []
        dates.map((date) => {
            let activeAlloc = filterAllocations(date,date,allocations)
            let displayAlloc = activeAlloc.map((alloc) => this.allocationToDisplayallocation(alloc, projects))
            result.push({
                'date' : date,
                'allocations' : displayAlloc
            })
        })
        return result
    }

    allocationToDisplayallocation = (alloc, projects) => {
        return {
            'pensumPercentage' : alloc.pensumPercentage+10,
            'color' : getObjectColor(alloc.projectId),
            'projectName' : projects.filter((project) => project.id == alloc.projectId)[0].name
        }
    }


    render({allocations, projects}) {
        let displayAllocation = this.updateDisplayElements(allocations, projects)
        return (
            <Layout>
                <h2>My Allocations</h2>
                {
                    displayAllocation.map(item =>
                    <DayContainer date={item.date} allocations={item.allocations} />)
                }
            </Layout>
        );
    }
}
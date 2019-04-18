import { h, Component } from 'preact';

import reducers from '../../reducers';
import * as actions from '../../actions';
import { connect } from 'preact-redux';
import Layout from '../../components/layout';
import DayContainer from './day-container';

@connect(reducers, actions)
export default class MyAllocationsPage extends Component {

    componentDidMount() {
        this.props.fetchProjectsAsync();
        this.props.fetchAllocationsAsync();
    }

    render() {
        return (
            <Layout>
                <h2>My Allocations</h2>
                
                <DayContainer/>
                <DayContainer/>
                <DayContainer/>
               
            </Layout>
        );
    }
}
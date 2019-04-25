import { h, Component } from 'preact';

import reducers from '../../reducers';
import * as actions from '../../actions';
import { connect } from 'preact-redux';
import Layout from '../../components/layout';

@connect(reducers, actions)
export default class ProjectPage extends Component {


    render() {
        return (
            <Layout>
                <h2>ProjectPage</h2>
                <div class="jumbotron" id="app">
                    <button onClick={this.props.fetchProjectsAsync}>Lade Projekte</button>
                    <p>{JSON.stringify(this.props.projects)}</p>
                </div>
            </Layout>
        );
    }
}
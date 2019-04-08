import { h, Component } from 'preact';
import TodoItem from '../todo/todo-item';

import reducers from '../../reducers';
import * as actions from '../../actions';
import { connect } from 'preact-redux';

@connect(reducers, actions)
export default class ProjectPage extends Component {


    render() {
        return (
            <div class="jumbotron" id="app">
                <button onClick={this.props.fetchProjectsAsync}>Lade Projekte</button>
            </div>
        );
    }
}
import { h, Component } from 'preact';

import createHashHistory from 'history/createHashHistory';
import { Router, route} from 'preact-router';

//require('Bootstrap')
import 'bootstrap/dist/css/bootstrap.css';
import './style.css';
import Redirect from '../components/redirect';
import TodoPage from './todo/todo-page';
import ProjectPage from './projects/projects';
import AuthLock from '../components/auth-lock';


export default class App extends Component {

	render() {
		return (
			<AuthLock>
				<Router onChange={this.handleRoute} history={createHashHistory()}>
					<TodoPage path='/todo' />
					<ProjectPage path='/project' />
					<Redirect default to="/project" />
				</Router>
			</AuthLock>
		);
	}
}

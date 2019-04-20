import { h, Component } from 'preact';

import { createHashHistory } from 'history';
import { Router, route } from 'preact-router';

//require('Bootstrap')
import 'bootstrap/dist/css/bootstrap.css';
import './style.css';
import Redirect from '../components/redirect';
import TodoPage from './todo/todo-page';
import ProjectPage from './projects/projects';
import AuthLock from '../components/auth-lock';
import MyAllocationsPage from './my-allocations/my-allocations';


export default class App extends Component {

	render() {
		return (
			<AuthLock>
				<Router history={createHashHistory()}>
					<TodoPage path='/todo' />
					<MyAllocationsPage path='/my-allocation' />
					<ProjectPage path='/project' />
					<Redirect default to="/project" />
				</Router>
			</AuthLock>
		);
	}
}

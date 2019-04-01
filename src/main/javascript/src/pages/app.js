import { h, Component } from 'preact';
import { connect } from 'preact-redux';
import reducers from '../reducers';
import * as actions from '../actions';
import TodoItem from '../components/todo-item';
import { Router } from 'preact-router';

require('Bootstrap')
import 'bootstrap/dist/css/bootstrap.css';
import './style.css';
const feather = require('feather-icons')

@connect(reducers, actions)
export default class App extends Component {


	addTodos = () => {
		this.props.addTodo(this.state.text);
		this.setState({ text: '' });
	};

	removeTodo = (todo) => {
		this.props.removeTodo(todo);
	};

	updateText = (e) => {
		this.setState({ text: e.target.value });
	};

	componentDidMount() {
		feather.replace();
	}

	componentDidUpdate(){
		feather.replace();
	}

	render({ todos }, { text }) {
		return (
			<div>
				<nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
					<a class="navbar-brand col-sm-3 col-md-2 mr-0" href="#">Company name</a>
					<input class="form-control form-control-dark w-100" type="text" placeholder="Search" aria-label="Search" />
					<ul class="navbar-nav px-3">
						<li class="nav-item text-nowrap">
							<a class="nav-link" href="#">Sign out</a>
						</li>
					</ul>
				</nav>

				<div class="container-fluid">
					<div class="row">
						<nav class="col-md-2 d-none d-md-block bg-light sidebar">
							<div class="sidebar-sticky">
								<ul class="nav flex-column">
									<li class="nav-item">
										<a class="nav-link active" href="#">
											<span data-feather="home"></span>
											Dashboard <span class="sr-only">(current)</span>
										</a>
									</li>
									<li class="nav-item">
										<a class="nav-link" href="#">
											<span data-feather="file"></span>
											Orders
            </a>
									</li>
									<li class="nav-item">
										<a class="nav-link" href="#">
											<span data-feather="shopping-cart"></span>
											Products
            </a>
									</li>
									<li class="nav-item">
										<a class="nav-link" href="#">
											<span data-feather="users"></span>
											Customers
            </a>
									</li>
									<li class="nav-item">
										<a class="nav-link" href="#">
											<span data-feather="bar-chart-2"></span>
											Reports
            </a>
									</li>
									<li class="nav-item">
										<a class="nav-link" href="#">
											<span data-feather="layers"></span>
											Integrations
            </a>
									</li>
								</ul>

								<h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
									<span>Saved reports</span>
									<a class="d-flex align-items-center text-muted" href="#">
										<span data-feather="plus-circle"></span>
									</a>
								</h6>
								<ul class="nav flex-column mb-2">
									<li class="nav-item">
										<a class="nav-link" href="#">
											<span data-feather="file-text"></span>
											Current month
            </a>
									</li>
									<li class="nav-item">
										<a class="nav-link" href="#">
											<span data-feather="file-text"></span>
											Last quarter
            </a>
									</li>
									<li class="nav-item">
										<a class="nav-link" href="#">
											<span data-feather="file-text"></span>
											Social engagement
            </a>
									</li>
									<li class="nav-item">
										<a class="nav-link" href="#">
											<span data-feather="file-text"></span>
											Year-end sale
            </a>
									</li>
								</ul>
							</div>
						</nav>

						<main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
							<h2>Section title</h2>
							<Router>
								<div path='/todo' class="jumbotron" id="app">
									<form onSubmit={this.addTodos} action="javascript:">
										<input value={text} onInput={this.updateText} placeholder="New ToDo..." />
									</form>
									<ul>
										{todos.map(todo => (
											<TodoItem key={todo.id} todo={todo} onRemove={this.removeTodo} />
										))}
									</ul>
								</div>
							</Router>
						</main>
					</div>
				</div>
			</div>
		);
	}
}
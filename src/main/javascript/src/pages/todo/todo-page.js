import { h, Component } from 'preact';
import TodoItem from './todo-item';

import reducers from '../../reducers';
import * as actions from '../../actions';
import { connect } from 'preact-redux';

@connect(reducers, actions)
export default class TodoPage extends Component {

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

    render({ todos }, { text }) {
        return (
            <div class="jumbotron" id="app">
            <form onSubmit={this.addTodos} action="javascript:">
                <input value={text} onInput={this.updateText} placeholder="New ToDo..." />
            </form>
            <ul>
                {todos.map(todo => (
                    <TodoItem key={todo.id} todo={todo} onRemove={this.removeTodo} />
                ))}
            </ul>
        </div>
        );
    }
}
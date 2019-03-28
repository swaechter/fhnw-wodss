import { createStore, applyMiddleware } from 'redux';
import thunkMiddleware from 'redux-thunk';
import reducers from './reducers';

// a custom middleware to track all the actions at one place
const myMiddleware = store => next => action => {
	next(action);
};

const Store = createStore(reducers, applyMiddleware(
	myMiddleware,
	thunkMiddleware
));
export default Store;

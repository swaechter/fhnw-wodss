import { Provider } from 'preact-redux';
import reducers from './reducers';
import { createStore, applyMiddleware } from 'redux';


import App from './pages/app';
import thunk from 'redux-thunk';
import { createLogger } from 'redux-logger'


const logger = createLogger({
	// ...options
});

const store = createStore(
	reducers,
	applyMiddleware(thunk, logger)
);

export default () => (
	<div id="app">
		<Provider store={store}>
			<App />
		</Provider>
	</div>
);

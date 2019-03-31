import { Provider } from 'preact-redux';
import reducers from './_reducers';
import { createStore, applyMiddleware } from 'redux';

import App from './pages/app';
import thunk from 'redux-thunk';
import { createLogger } from 'redux-logger'

const logger = createLogger({
	// ...options
});

const store = createStore(
	reducers,
	applyMiddleware(logger,thunk)
);

export default () => (
	<div id="outer">
		<Provider store={store}>
			<App />
		</Provider>
	</div>
);

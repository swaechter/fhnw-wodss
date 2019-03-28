import 'babel-polyfill';
import { Provider } from 'preact-redux';
import store from './store';
import './assets';
import App from './components/app';

export default () => (
	<Provider store={store}>
		<App />
	</Provider>
);

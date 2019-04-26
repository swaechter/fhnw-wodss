import { h } from 'preact'
import { Provider } from 'preact-redux';
import store from "./store";
import App from "./pages/app";

export default () => (
	<div id="app">
		<Provider store={store}>
			<App />
		</Provider>
	</div>
);

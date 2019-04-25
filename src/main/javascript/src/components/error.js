import {Component} from 'preact';

import {connect} from 'preact-redux';
import reducers from '../reducers';
import * as actions from '../actions';

@connect(reducers, actions)
export default class Error extends Component {

    render({children}) {
        let error = '';
        if (this.props.error !== 'undefined' && this.props.error != null && this.props.error.length > 0) {
            error = <div className="alert alert-danger" role="alert">{this.props.error}</div>
        }
        return (
            <div>
                {error}
                {children}
            </div>
        );
    }
}

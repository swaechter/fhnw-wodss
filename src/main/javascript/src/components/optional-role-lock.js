import {Component} from 'preact';
import {connect} from 'preact-redux';
import * as actions from '../actions';
import reducers from '../reducers';

@connect(reducers, actions)
export default class OptionalRoleLock extends Component {
    render({allowedRoles, children}) {
        let allowed = allowedRoles
            .map(str => str.toUpperCase())
            .includes(this.props.auth.employee.role.toUpperCase())
        if (allowed) {
            return ({children})
        }
    }
}

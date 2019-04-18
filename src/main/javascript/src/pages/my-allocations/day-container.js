import { h, Component } from 'preact';
import PlanningItem from './planning-item';
import {getObjectColor} from '../../utils/colors'


export default class DayContainer extends Component {

    render() {
        let items = [{width: 10, color:'1'}, {width: 20, color:'11'}, {width: 10, color:'11   11'}]

        return (
            <div class="progress">
                {
                    items.map(element =>
                        <PlanningItem width={element.width} color={getObjectColor(element)} />
                    )
                }
            </div>
        );
    }
}
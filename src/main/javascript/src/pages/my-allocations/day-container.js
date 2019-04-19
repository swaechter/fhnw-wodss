import { h, Component, options } from 'preact';
import PlanningItem from './planning-item';
import { getObjectColor } from '../../utils/colors'


export default class DayContainer extends Component {

    render({date, allocations}) {
        return (
            <div class='row'>
                <div class="col">
                    {date.toDateString()}
                </div>
                <div class="col-8">
                    <div class="progress planning-progress">
                        {
                            allocations.map(dispAlloc =>
                                <PlanningItem width={dispAlloc.pensumPercentage} color={dispAlloc.color} />
                            )
                        }
                    </div>
                </div>
            </div>
        );
    }
}
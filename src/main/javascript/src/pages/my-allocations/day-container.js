import { h, Component, options } from 'preact';
import PlanningItem from './planning-item';


export default class DayContainer extends Component {

    render({date, allocations}) {
        return (
            <div class='row'>
                <div class="col-1 col-sm-3">
                    {date.toDateString()}
                </div>
                <div class="col">
                    <div class="progress planning-progress">
                        {
                            allocations.map(dispAlloc =>
                                <PlanningItem width={dispAlloc.pensumPercentage} color={dispAlloc.color} text={dispAlloc.projectName} />
                            )
                        }
                    </div>
                </div>
            </div>
        );
    }
}
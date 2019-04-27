//Colors from https://sashat.me/2017/01/11/list-of-20-simple-distinct-colors/
const distinctiveColors = ['#e6194b', '#3cb44b', '#ffe119', '#4363d8', '#f58231', '#911eb4', '#46f0f0', '#f032e6', '#bcf60c',
 '#fabebe', '#008080', '#e6beff', '#9a6324', '#fffac8', '#800000', '#aaffc3', '#808000', '#ffd8b1', '#000075', '#808080']

 export function UUIDToColor(uuid){
    let array = uuid.split('-')
    const r = array[0].substring(0,2)
    const g = array[1].substring(0,2)
    const b = array[2].substring(0,2)
    return '#'+r+g+b
 }


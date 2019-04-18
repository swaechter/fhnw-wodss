//Colors from https://sashat.me/2017/01/11/list-of-20-simple-distinct-colors/
const distinctiveColors = ['#e6194b', '#3cb44b', '#ffe119', '#4363d8', '#f58231', '#911eb4', '#46f0f0', '#f032e6', '#bcf60c',
 '#fabebe', '#008080', '#e6beff', '#9a6324', '#fffac8', '#800000', '#aaffc3', '#808000', '#ffd8b1', '#000075', '#808080']

 const hash = (s) => {
    var hash = 0,
      i, char;
    if (s.length == 0) return hash;
    let l;
    for (i = 0, l = s.length; i < l; i++) {
      char = s.charCodeAt(i);
      hash = ((hash << 5) - hash) + char;
      hash |= 0; // Convert to 32bit integer
    }
    return hash;
  };

 export function getObjectColor(object){
    return distinctiveColors[hash(JSON.stringify(object)) % distinctiveColors.length]
 }


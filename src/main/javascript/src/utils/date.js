export const checkDateRangeOverlap = (start1, end1, start2, end2) => {
    return (end2 >= start1 && start2 <= end1)
}

export const getMonday = ( date ) =>{
    var day = date.getDay() || 7;  
    if( day !== 1 ) 
        date.setHours(-24 * (day - 1)); 
    return date;
}

export const getDateRange = (startDate, stopDate) => {
    var dateArray = new Array();
    var currentDate = startDate;
    while (currentDate <= stopDate) {
        dateArray.push(new Date (currentDate));
        currentDate = new Date(currentDate).setDate(new Date(currentDate).getDate() + 1);
    }
    return dateArray;
}
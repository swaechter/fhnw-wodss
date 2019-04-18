import { checkDateRangeOverlap } from "./date";

export const unique = (array, getIdentifier) => {
    let result = []
    let ids = new Set()
    for(let item of array){
        let id = getIdentifier(item)
        if(!ids.has(id)){
            ids.add(id)
            result.push(item)
        }
    }
    return result
}

export const filterAllocations = (from, to, allocations) => {
    if(from>to) throw new Error('Invalid date range')
    return allocations.filter((alloc) => checkDateRangeOverlap(from,to,alloc.startDate, alloc.endDate))
}


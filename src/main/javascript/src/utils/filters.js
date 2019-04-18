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
const unique = (array = [], getIdentifier) => {
    let result = []
    let ids = new Set()
    for(item of array){
        let id = getIdentifier(item)
        if(!ids.has(id)){
            ids.add(id)
            result.push(item)
        }
    }
}
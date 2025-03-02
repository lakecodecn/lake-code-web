export const ${moduleName}_add = (data) => {
    return post('api/admin/${moduleName}/add', data)
}

export const ${moduleName}_edit = (data) => {
    return post('api/admin/${moduleName}/edit', data)
}

export const ${moduleName}_del = (data) => {
    return post('api/admin/${moduleName}/del', data)
}

export const ${moduleName}_list = (data) => {
    return post('api/admin/${moduleName}/list', data)
}
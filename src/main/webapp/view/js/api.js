export default class Api {

    static getData(success) {
        baseApi('/server/getUsers','GET',null, success)
    }

    static addData(user, success) {
        baseApi('/server/addUsers', "POST", user, success)
    }

    static modifyData(user, success) {
        baseApi('/server/modifyUsers', 'POST', user, success)
    }

    static deleteData(id, success) {
        baseApi(`/server/deleteUser?id=${id}`, 'GET', null, success)
    }

    static findData(query, success) {
        baseApi(`/server/fuzzyQuery?query=${query}`, 'GET', null, success)
    }
}

function baseApi(url, type, data, success, error) {
    let contentType
    if (type === "POST") {
         data = JSON.stringify(data)
        contentType = 'application/json;charset=utf-8'
    } else {
        contentType = 'application/x-www-form-urlencoded'
    }
    $.ajax(url, {
        type,
        data,
        contentType,
        success(data, textStatus) {
            success(data, textStatus)
        },
        error(XMLHttpRequest, textStatus, errorThrown) {
            error
                ? error(XMLHttpRequest, textStatus, errorThrown)
                : console.log(XMLHttpRequest, textStatus, errorThrown)
        }
    })
}
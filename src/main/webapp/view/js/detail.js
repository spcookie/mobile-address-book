import Api from "./api.js";
import {load, users} from './main.js'

const form = $('.panel')
const getInputBox = () => form.find('input')
let info

function showDetail(id) {
    let kv = id.split('-')
    info = users[kv[0]][Number.parseInt(kv[1])]
    console.log(info)
    let temp = {
        name: info['name'],
        phone: info['phone'],
        address: info['address']
    }
    console.log(temp)
    form.css('display', 'block')
    form.find('.delete').css('display', 'block')
    form.find('.modify').css('display', 'block')
    let i = 0;
    for (let k in temp) {
        getInputBox().eq(i++).val(temp[k])
    }
}

function getDataFromBox() {
    let temp = {
        name: '',
        phone: [],
        address: ''
    }
    console.log(getInputBox())
    getInputBox().map(function () {
        let $this = $(this)
        let attr = $this.attr('name')
        if (attr === 'name') {
            temp.name = $this.val()
        } else if (attr === 'phone') {
            temp.phone.push($this.val())
        } else {
            temp.address = $this.val()
        }
    })
    if (temp.name === '' || temp.phone.length === 0) {
        console.log('数据为空！！！')
        return null
    } else {
        console.log(temp)
        return temp
    }
}

function close() {
    form.css('display', 'none')
    getInputBox().val('')
    form.children('.save').css('display', 'none')
    form.find('.delete').css('display', 'none')
    form.find('.modify').css('display', 'none')
}

$('.add-phone').click(function () {
    let $label = $('.phone-box')
    console.log($label)
    let $phone = $label.children('input').eq(0)
    console.log($phone)
    $label.append('<span>', $phone.clone())
})

$('.save').click(function () {
    let user = getDataFromBox()
    if (user !== null) {
        Api.addData(user, (mag) => {
            console.log(mag)
            if (mag.state) {
                load()
                close()
            }
        })
    }
})

$('.return').click(() => {
    close()
})

$('.modify').click(() => {
    let {id} = info
    let {name, phone, address} = getDataFromBox()
    Api.modifyData({
        id,
        name,
        phone,
        address
    }, (resp) => {
        if (resp.state) {
            load()
            close()
        }
        console.log(resp.message)
    })
})

$('.delete').click(() => {
    let {id} = info;
    Api.deleteData(id, (resp) => {
        console.log(resp)
        load()
        close()
    })
})

export {
    form,
    showDetail,
    close
}
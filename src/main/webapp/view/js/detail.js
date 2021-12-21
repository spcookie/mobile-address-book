import Api from "./api.js";
import {load, users} from './main.js'
import Alert from "./alert.js";

const form = $('.panel')
const getInputBox = () => form.find('input')
let info

function showDetail(id) {
    let kv = id.split('-')
    info = users[kv[0]][Number.parseInt(kv[1])]
    let temp = {
        name: info['name'],
        phone: info['phone'],
        address: info['address']
    }
    console.log('detail -> ', temp)
    form.css('display', 'block')
    form.find('.delete').css('display', 'block')
    form.find('.modify').css('display', 'block')
    createPhoneBox(temp.phone.length - 1)
    let $boxes = getInputBox()
    let i = 0
    $boxes.eq(i++).val(temp.name)
    for (const e of temp.phone) {
        $boxes.eq(i++).val(e)
    }
    $boxes.eq(i).val(temp.address)
}

function getDataFromBox() {
    let temp = {
        name: '',
        phone: [],
        address: ''
    }
    getInputBox().map(function () {
        let $this = $(this)
        let attr = $this.attr('name')
        if (attr === 'name') {
            temp.name = $this.val()
        } else if (attr === 'phone') {
            let val = $this.val()
            if (val !== '') {
                temp.phone.push(val)
            }
        } else {
            temp.address = $this.val()
        }
    })
    if (temp.name === '') {
        Alert.error('姓名为空')
        return null
    } else if (temp.phone.length === 0) {
        Alert.error('号码为空')
        return null
    } else {
        return temp
    }
}

function close() {
    form.css('display', 'none')
    getInputBox().val('')
    resetPhoneBox()
    form.children('.save').css('display', 'none')
    form.find('.delete').css('display', 'none')
    form.find('.modify').css('display', 'none')
}


let $add = $('.add-phone > button:first');
const $delete = $('.add-phone > button:last')

function createPhoneBox(num) {
    let $label = $('.phone-box')
    let $phone = $label.children('input').eq(0)
    for (let i = 0; i < num; i++) {
        let $temp = $phone.clone()
        $temp.val('')
        $label.append('<span>', $temp)
    }
    //判断是否需要删除按钮
    let length = $('.phone-box > input').length;
    if (length > 1) {
        $delete.show()
    }
}

function deletePhoneBox() {
    let $label = $('.phone-box').children();
    $label.remove('span:last')
    $label.remove('input:last')
}

function resetPhoneBox() {
    let $label = $('.phone-box')
    $label.empty()
    $label.append('<span>号码</span><input type="number" name="phone">')
}

$add.click(function () {
    createPhoneBox(1)
    let length = $('.phone-box > input').length;
    if (length > 1) {
        $delete.show()
    }
})

$delete.click(function () {
    deletePhoneBox()
    let length = $('.phone-box > input').length;
    if (length <= 1) {
        $delete.hide()
    }
})

$('.save').click(function () {
    let user = getDataFromBox()
    console.log('user-info -> ', user)
    if (user !== null) {
        let is = verification(user)
        if (is) {
            Api.addData(user, (resp) => {
                if (resp.state) {
                    load()
                    close()
                    Alert.tips(resp.message)
                } else {
                    Alert.error(resp.message)
                }
            })
        } else {
            Alert.error('请输入正确的电话号码')
        }
    }
})

function verification(user) {
    let verified = {
        phone: /[0-9]{11,}/
    }
    let is = true
    for (let e of user.phone) {
        if (!verified.phone.test(e)) {
            is = false
            break
        }
    }
    return is
}

$('.return').click(() => {
    close()
})

$('.modify').click(() => {
    let {id} = info
    let {name, phone, address} = getDataFromBox()
    let user = {
        id,
        name,
        phone,
        address
    }
    let is = verification(user)
    if (is) {
        Api.modifyData(user, (resp) => {
            if (resp.state) {
                load()
                close()
                Alert.tips(resp.message)
            } else {
                Alert.error(resp.message)
            }
        })
    } else {
        Alert.error('请输入正确的电话号码')
    }
})

$('.delete').click(() => {
    let {id} = info;
    Api.deleteData(id, (resp) => {
        load()
        close()
        if (resp.state) {
            Alert.tips(resp.message)
        } else {
            Alert.error(resp.message)
        }
    })
})

export {
    form,
    showDetail,
    close
}
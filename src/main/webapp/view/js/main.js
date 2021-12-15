import Layout from './layout.js'
import Api from "./api.js"
import {form, showDetail} from './detail.js'

Layout.coverHeight($('#bg'))

let users
let labelMap
let labelWidth
let group

function load() {
    Api.getData((data) => {
        users = data
        creatDom()
        itemClick()
        let labelInfo = getLabelMap()
        labelMap = labelInfo.labelMap
        labelWidth = labelInfo.labelWidth
        //nav选中
        let $nav = $('#nav li')
        $nav.css('background-color', '')
        for (const key of labelMap.keys()) {
            let index = key.charCodeAt() - 65
            $nav.eq(index).css('background-color', 'rgb(180, 80, 98)')
            break
        }
    })
}

load()

function creatDom() {
    console.log(users)
    group = $('.group')
    group.empty()
    for (let k in users) {
        const li = document.createElement('li')
        const div = document.createElement('div')
        const ul = document.createElement('ul')
        ul.classList.add('item')
        div.innerText = k
        let i = 0
        for (let o of users[k]) {
            const item = document.createElement('li')
            item.innerText = o.name
            item.setAttribute("identification", `${k}-${i}`)
            ul.append(item)
            i++
        }
        li.append(div, ul)
        group.append(li)
    }
}

function getLabelMap() {
    let labelMap = new Map()
    let labelWidth = new Map()
    let $div = group.find('div')
    let accumulation = 0
    $div.map(function () {
        let letter = $(this).text()
        let w = $(this).closest('li').outerHeight(true)
        labelWidth.set(letter, w)
        accumulation += w
        labelMap.set(letter, accumulation)
    })
    // console.log(map)
    return {labelMap, labelWidth}
}

function itemClick() {
    $('.item > li').click(function () {
        let id = $(this).attr('identification')
        console.log(id)
        showDetail(id)
    })
}

//添加按钮
$('#add-button > button').click(() => {
    form.css('display', 'block')
    form.find('.save').css('display', 'block')
})

$('.queryBox').find('input').on('input propertychange', function () {
    let val = $(this).val()
    // console.log(val)
    const ul = $('#result')
    ul.empty()
    if (val !== '') {
        let ch = val.charAt(0)
        //汉字unicode范围 ->	4E00-9FA5
        if (/[\u4e00-\u9fa5|0-9]/.test(ch)) {
            Api.findData(val, (resp) => {
                // console.log(resp)
                if (resp != null) {
                    for (let e of resp) {
                        let lis = createQueryItem(e)
                        ul.append(lis)
                    }
                    $(ul).slideDown(1000)
                }
            })
        }
    }
})

function createQueryItem({name, phone}) {
    let li = document.createElement('li')
    let title = document.createElement('p')
    let additional = document.createElement('p')
    title.classList.add('title');
    additional.classList.add('additional')
    li.append(title, additional)
    title.innerText = name
    additional.innerText = phone.join('|')
    return li
}

export {
    load,
    users,
    group,
    labelMap,
    labelWidth
}
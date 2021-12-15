import {labelMap, labelWidth} from "./main.js";

function center(jDom) {
    const h = window.innerHeight
    // console.log(h)
    // console.log(jDom.outerHeight(true))
    let surplus = h - jDom.outerHeight(true)
    if (surplus > 0) {
        let margin = Math.floor((h - jDom.outerHeight(true)) / 2) - 1
        jDom.css('margin', `${margin}px auto`)
    }
}

function coverHeight(jDom) {
    jDom.css('height', window.innerHeight)
}

let $nav = $('#nav li')

$('#bg').scroll(function () {
    let scrollTop = $(this).scrollTop()
    let letter;
    // console.log(labelMap.entries())
    for (const e of labelMap.entries()) {
        // console.log(e[1], e[0], scrollTop)
        if (scrollTop <= e[1]) {
            // console.log(e[0].charCodeAt() - 65)
            letter = e[0].charCodeAt() - 65
            $nav.css('background-color', '')
            $nav.eq(letter).css('background-color', 'rgb(180, 80, 98)')
            break
        }
    }
})

$nav.click(function () {
    console.log(labelWidth, labelMap)
    let index = $(this).index()
    let letter = String.fromCharCode(index + 65)
    // console.log(letter)
    let map = labelMap.get(letter)
    if (map !== undefined) {
        let val = map - labelWidth.get(letter) + 1
        // console.log(val)
        $('#bg').animate({
            scrollTop: val
        }, 100)
        // console.log($('#bg').scrollTop())
    }
})

export default {
    center,
    coverHeight
}

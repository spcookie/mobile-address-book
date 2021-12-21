import {labelMap, labelWidth} from "./main.js";

function center(jDom) {
    const h = window.innerHeight
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
    for (const e of labelMap.entries()) {
        if (scrollTop <= e[1]) {
            letter = e[0].charCodeAt() - 65
            $nav.css({
                backgroundColor: '',
                color: 'black'
            })
            letter = letter >= 0 ? letter : 26
            $nav.eq(letter).css({
                backgroundColor: 'rgba(41, 41, 41, 0.85)',
                color: 'white'
            })
            break
        }
    }
})

$nav.click(function () {
    let index = $(this).index()
    let letter
    if (index === 26) {
        letter = '#'
    } else {
        letter = String.fromCharCode(index + 65)
    }
    let map = labelMap.get(letter)
    if (map !== undefined) {
        let val = map - labelWidth.get(letter) + 1
        $('#bg').animate({
            scrollTop: val
        }, 100)
    }
})

export default {
    coverHeight
}


export default class Alert {
    static tips(text) {
        appear(text, 'rgb(94, 176, 217)')
    }

    static error(text) {
        appear(text, 'rgb(180, 80, 98)')
    }
}

function appear(text, color) {
    const $div = $('.message')
    $div.text(text)
    $div.css('background-color', color)
    $div.animate({
        opacity: 1,
        bottom: '14%'
    }, 300).queue(() => {
        setTimeout(() => {
            $div.css({
                opacity: 0,
                bottom: 0
            })
            $div.dequeue()
        }, 2000)
    })
}
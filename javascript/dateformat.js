// ?????
function dateFormat(oDate, fmt) {
    var o = {
        "M+": oDate.getMonth() + 1, //??
        "d+": oDate.getDate(), //?
        "h+": oDate.getHours(), //??
        "m+": oDate.getMinutes(), //?
        "s+": oDate.getSeconds(), //?
        "q+": Math.floor((oDate.getMonth() + 3) / 3), //??
        "S": oDate.getMilliseconds()//??
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (oDate.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}


//使用方法
var endTime = dateFormat(new Date(), "yyyy-MM-dd 23:59:59");
var startTime = dateFormat(new Date(), "yyyy-MM-dd 00:00:00");
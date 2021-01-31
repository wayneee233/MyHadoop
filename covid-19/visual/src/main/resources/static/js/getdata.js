function getdata(){
    const list = []
    $.ajax({
        url:"/covid/data",
        method:'POST',
        sync:true,
        success:function (data){
            const values = data.data
            values.forEach(element => {
                list.push({
                    name : element.name_jp,
                    total : element.total
                })
            })
        }
    })
    return list
}

function getData2(){
    const map = new Map()
    let arr = []
    map.set('都道府県', '人口')
    $.ajax({
        url:"http://localhost:8080/covid/data",
        method:'POST',
        sync:true,
        dataType:'json',
        success:function (data){
            const values = data.data
            values.forEach(element => {
                /*console.log(element.name_jp,element.total)*/
                map.set(String(element.name_jp).replace("\"","'"),element.total)
            })
            arr = [...map]
        }
    })
    return arr
}

/*function washData(){
    const date = getData2()
    date.forEach(element => {
        element.
    })
}*/

function GetDateStr(AddDayCount) {

    const dd = new Date();

    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期

    const y = dd.getFullYear();

    const m = (dd.getMonth() + 1) < 10 ? "0" + (dd.getMonth() + 1) : (dd.getMonth() + 1);//获取当前月份的日期，不足10补0

    const d = dd.getDate() < 10 ? "0" + dd.getDate() : dd.getDate();//获取当前几号，不足10补0

    return y+"-"+m+"-"+d;

}

GetDateStr()

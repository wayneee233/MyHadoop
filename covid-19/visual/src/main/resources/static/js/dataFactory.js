function dataFactory(){
        google.load('visualization', '1', {packages:['geochart'],'mapsApiKey':'AIzaSyDwgB4mC7_-ecn5Ah9qRVcmsAOLCemndek'});
        google.setOnLoadCallback(graphChart);

        const option = {
        region: 'JP',
        resolution: 'regions',
        colors: ['#fdebcf', '#cb2a2f'],
        sizeAxis: { minValue: 0, maxValue: 100 },
        displayMode: 'markers',
    }

        const arr = []
        const map = new Map()
        map.set('都道府県', '感染者数')
        function graphChart() {
        $.ajax({
            url: "http://localhost:8080/covid/data",
            method: 'GET',
            sync: true,
            dataType: 'json',
            success: function (data) {
                const values = data.data

                values.forEach(element => {
                    map.set(String(element.name_jp).replace("\"","'"), element.npatients);
                });

                const data1 = google.visualization.arrayToDataTable(data2)
                console.log([...map])
                const graph = new google.visualization.GeoChart(document.getElementById('drawChart'));
                graph.draw(data1, option);
            }
        })
    }
}

function key() {
    return 'AIzaSyDwgB4mC7_-ecn5Ah9qRVcmsAOLCemndek'
}

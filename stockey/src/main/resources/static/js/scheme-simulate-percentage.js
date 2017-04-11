/**
 * Created by Alan on 2016/5/25.
 */
var dom = document.getElementById("scheme-percentage-chart");
var myChart = echarts.init(dom);
var app = {};
option = null;
option = {
    title : {
        text: 'Sources',
        subtext: 'Faked',
        x:'center'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        orient: 'vertical',
        left: 'left',
        data: ['Direct Link','Email','ADs','Videos','Search Engines']
    },
    series : [
        {
            name: 'Sources',
            type: 'pie',
            radius : '55%',
            center: ['50%', '60%'],
            data:[
                {value:335, name:'Direct Link'},
                {value:310, name:'Email'},
                {value:234, name:'ADs'},
                {value:135, name:'Videos'},
                {value:1548, name:'Search Engines'}
            ],
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
    ]
};
if (option && typeof option === "object") {
    myChart.setOption(option, true);
}
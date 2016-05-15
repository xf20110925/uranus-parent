/**
 * Created by Administrator on 2015/11/25.
 */
;(function(){
    $(function(){
        var $body=$('.container');
        var url = urlConf.listUrl,         //数据请求路径
            gettpl = $("#demo-list").html(),  //模板
            box = $(".list table tbody");     //容器

        var requestServerData=function(){
            //{"data":[{"id":"11","name":"2222222","state":"33"}],"statusCode":0,"success":true}
            $body.find('p.tips').remove();
            $.get(url,function(data,status){
                console.log(data);
                if(status){
                    if(data.success){
                        render(data.data);
                    }else{
                        $body.append('<p class="nodata" style="text-align: center">无数据</p>');
                    }
                }else{
                    console.log('出错');
                }
            },'JSON');
        }

       requestServerData();

        function render(data){
            laytpl(gettpl).render(data, function(html){
                box.html(html);
            });
        }


    });
})();
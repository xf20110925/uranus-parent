   �         �http://mp.weixin.qq.com/mp/newappmsgvote?action=show&__biz=MzAwMjI4Mzc0MA==&supervoteid=2657378&uin=&key=&pass_ticket=&wxtoken=&mid=404092754&idx=1�       ���� �       ����          
     O K           �      Content-Type   text/html; charset=UTF-8   Content-Encoding   deflate <!DOCTYPE html>
<html>
    <head>
        <meta http-equiv=Content-Type content="text/html;charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
        <title></title>
        
<link rel="stylesheet" href="http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/style/page/page_mp_vote2b1291.css">

    </head>
    <body class="zh_CN " ontouchstart="">
        
<script id="t_votes" type="text/html">
    {if (del_flag==0)}
    <div class="vote_form_inner {if has_vote}has_vote{/if} {if (is_expired||!is_login||vote_permission==2&&!is_fans)}expired{/if}">
        {if (!is_login)}
        <p class="vote_error_tips">
            <img src="http://mmbiz.qpic.cn/mmbiz/a5icZrUmbV8qLa7aVNndUPfGbulEZzxnibJIenSIdYk1faoKobuK70I9wWEXnGQjicG7L2LibJQCAPwMPP0IAvtY6g/0" alt="">很抱歉，请在手机微信登录投票
        </p>
		{else if (is_expired)}
		<div class=" vote_frm_top">
			<p class="vote_tips">投票已过期</p>
		</div>
		{else if (vote_permission==2&&!is_fans)}
		<div class="vote_frm_top">
			<p class="vote_tips">关注公众号后才可投票</p>
		</div>
		{/if}
		
        {each vote_subject as item idx}
		<div class="vote_form_ele{if idx+1==vote_count} vote_form_ele_last{/if}"  id="img-content">
			<h3 class="vote_title">{if vote_count>1}{idx+1}.{/if}{item.title}{if item.type==1}（单选）{else}（多选）{/if}</h3>
			
			<ul class="vote_option_list ">
				{each item.options as subitem subidx}
				<li class="vote_option">
					
					<input class="{if item.type==1}frm_radio{else}frm_checkbox{/if} js_vote" type="{if item.type==1}radio{else}checkbox{/if}" id="vote_{idx}_{subidx}" name="vote_{idx}" value="{subidx}" {if (has_vote || !is_login || is_expired ||(vote_permission==2&&!is_fans))}disabled="disabled"{/if}>
					
					<label for="vote_{idx}_{subidx}" onclick="" class="{if item.type==1}frm_radio_label{else}frm_checkbox_label{/if} vote_option_title group">
						{if subitem.url}
						<div class="img_container img_container_label">
							<span class="img_panel"><img class="preview" src="{subitem.url.replace(/\/0$/, '/300?wxfrom=101').replace(/\/0\?/, '/300?wxfrom=101&')}"/></span>
						</div>{/if}<span class="frm_option_word">{subitem.name} {if subitem.selected}(已选){/if}</span>
					</label>
					
					<div class="vote_result">
						<span class="vote_result_meta tips vote_number">{subitem.cnt}票</span>
						<span class="vote_result_meta tips vote_percent">{if item.total_cnt>0}{toFixed subitem.cnt*100/item.total_cnt}{else}0{/if}%</span>
						<div class="vote_result_meta vote_graph">
							<span style="width:{toFixed subitem.cnt*100/item.total_cnt}%;background-color:#7DADE1" class="vote_progress"></span>
						</div>
					</div>
					
					{if subitem.url}
					<div class="img_container img_container_btm">
						<span class="img_panel"><img class="preview" src="{subitem.url.replace(/\/0$/, '/300?wxfrom=101').replace(/\/0\?/, '/300?wxfrom=101&')}"/></span>
					</div>
					{/if}
				</li>
				{/each}
			</ul>
		</div>
        {/each}
        
			{if (!is_expired&&is_login&&!(vote_permission==2&&!is_fans))}
                {if !has_vote}
				<div class="vote_tool_area vote_tool_area_btn">
					<button id="js_btn" type="submit" class="btn btn_inline btn_primary btn_disabled">投票</button>
				</div>
                {else}
				<div class="vote_tool_area">
					<p class="vote_tips">你已投票</p>
				</div>
                {/if}
            {/if}
        
    </div>
    {/if}
</script>
<div id="js_form" action="javascript:return false;" class="vote_box form vote_form ">
</div>

        
<script type="text/javascript" src="http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/js/lib/zepto1f908c.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/js/common/template1b4b91.js"></script>
<script type="text/javascript">
    document.domain = "qq.com";
    var biz = "MzAwMjI4Mzc0MA==";
    var top = window.top || window.parent;

    var reg = /^http(s)?\:\/\/mp\.weixin\.qq\.com\/s/g;
    if (top == window || !/mp\.weixin\.qq\.com$/.test(top.location.host)){ 
        //alert("非法投票来源！");
    }

    //弹出框中图片的切换
    (function(){
        var imgsSrc = [];
        function reviewImage(src) {
            if (typeof window.top.WeixinJSBridge != 'undefined') {
                window.top.WeixinJSBridge.invoke('imagePreview', {
                    'current' : src,
                    'urls' : imgsSrc
                });
            }
        }
        function onImgLoad() {
            var imgs = document.getElementsByTagName("img")||[];
            for( var i=0,l=imgs.length; i<l; i=i+2){
                var img = imgs.item(i);
                var src = img.getAttribute('data-src') || img.getAttribute('src');

                if( src ){
                    if ( src.indexOf("http://mmbiz.qpic.cn") == 0){
                        src = src.replace(/\/300$/, "/0");
                        src = src.replace(/\/300\?/, "/0?");
                    }
                    imgsSrc.push(src);//大图预览的时候采用原图
                    (function(src){
                        if (img.addEventListener){
                            img.addEventListener('click', function(){
                                reviewImage(src);
                            },false);
                        }else if(img.attachEvent){
                            img.attachEvent('click', function(){
                                reviewImage(src);
                            },false);
                        }
                    })(src);
                }
            }
        }
        if( window.addEventListener ){
            window.addEventListener('load', onImgLoad, false);
        }else if(window.attachEvent){
            window.attachEvent('load', onImgLoad);
            window.attachEvent('onload', onImgLoad);
        }
    })();   
    function domload(){
        template.helper("toFixed", function(num){
            num = num.toString();
            var idx = num.indexOf('.');
            if (idx == -1){
                return num;
            }
            return num.substr(0,idx);// + "." + num.substr(idx+1,2);
        });
        var Z = Zepto;
        function afterVote(){
            if(ret !== '0'){
                return ;
            }
            Z("#js_form").html(template.render("t_votes", voteInfo));
            if(top.iframe_reload){
                top.iframe_reload(window);
            }
        }
        afterVote();
        
        Z(".js_vote").change(function(){
            if(Z(".js_vote:checked").length > 0){
                Z("#js_btn").removeClass("btn_disabled");
            }
            else{
                Z("#js_btn").addClass("btn_disabled");
            }
        });

        var has_click_vote = false;
        Z("#js_btn").click(function(){
            if (!voteInfo.is_login || has_click_vote) {
                return false;
            }
            var values = [];
            //计算一共有几个投票
            var len = subject.length;
            for (var i = 0; i < len; i++) {
                var temp = [],eles = $('input[name=vote_'+i+']:checked');

                if(eles.length>0){
                    $('input[name=vote_'+i+']:checked').each(function(index,ele){
                        temp.push(ele.value);
                    })
                    values.push({
                        vote_id : subject[i].vote_id,
                        item_idx_list : {
                            item_idx:temp
                        }                          
                    })
                }else{
                    alert('请先完成所有问题');
                    return false;
                }
                    
            };
            //检查数据是否完整               
            has_click_vote = true;
            var _idx = '1',
                _mid = '404092754',
                _wxtoken = '',
                _reprint_ticket = '',
                _source_mid = '',
                _source_idx = '',
                _data = {
                    action:'vote',
                    __biz:'MzAwMjI4Mzc0MA==',
                    uin:'',
                    key:'',
                    pass_ticket:'',
                    f:'json',
                    json:JSON.stringify({
                        "super_vote_item":values,
                        "super_vote_id":voteInfo.super_vote_id
                    })
                };
            _idx && (_data["idx"] = _idx);
            _mid && (_data["mid"] = _mid);
            _wxtoken && (_data["wxtoken"] = _wxtoken);
            _reprint_ticket && (_data["reprint_ticket"] = _reprint_ticket);
            _source_mid && (_data["source_mid"] = _source_mid);
            _source_idx && (_data["source_idx"] = _source_idx);

            Z.ajax({
                url:'/mp/newappmsgvote',
                data:_data,
                dataType:'json',
                type:'post',
                success:function(res){
                    if (res.base_resp && res.base_resp.ret == 0){
                        //人数加1，selected修改为true                           
                        for (var i = 0; i < subject.length; i++) {
                            
                            for (var j = 0; j < subject[i].options.length; j++) {                                    
                                if($('input[name=vote_'+i+']').eq(j).is(':checked')){
                                    subject[i].options[j].selected = true;
                                    subject[i].options[j].cnt++;
                                    subject[i].total_cnt ++;
                                }                                    
                            };
                        };
                        voteInfo.has_vote = 1;
                        has_click_vote = false;
                        afterVote();
                    }else if(res.base_resp && res.base_resp.ret == -7){
                        has_click_vote = false;
                        alert('关注公众号后才可以投票');
                    }else if(res.base_resp && res.base_resp.ret == -6){
                        has_click_vote = false;
                        alert('投票过于频繁，请稍后重试！');
                    }else{
                        has_click_vote = false;
                        alert('投票失败，请稍后重试!');                        
                    }
                },
                error:function(){
                    has_click_vote = false;
                    alert('投票失败，请稍后重试!');
                }
            });
        });
    }
    function getParam(key, s){
        var s = s || location.href;
        var key = key.replace(/[*+?^$.\[\]{}()|\\\/]/g, "\\$&"); // escape RegEx meta chars
        var match = s.match(new RegExp("[?&]"+key+"=([^&]+)(&|$)"));
        return match && decodeURIComponent(match[1].replace(/\+/g, " "));
    }
    var voteInfo={"title":"腿锅读诗3","vote_permission":1,"expire_time":1454169600,"total_person":1414,"vote_subject":[{"type":1,"title":"下期你想听腿锅读哪首？","options":[{"name":"假如生活欺骗了你","cnt":306,"selected":false},{"name":"面朝大海，春暖花开","cnt":230,"selected":false},{"name":"雨巷","cnt":94,"selected":false},{"name":"你是人间四月天","cnt":128,"selected":false},{"name":"我的滑板鞋","cnt":656,"selected":false}],"total_cnt":1414,"vote_id":3688088}],"super_vote_id":2657378,"del_flag":0};
    var ret = '0';
    var pass_ticket = "";
    if(ret !== '0'){
        alert('参数错误');       
    }
    else{
       if(voteInfo){
            

            //判断是否已经过期
            var expire_time = voteInfo.expire_time ? voteInfo.expire_time*1000 : 0;
            voteInfo.is_expired = (expire_time - (+new Date()))>0 ? 0 : 1;//0表示没过期，1表示过期
            //判断是否已经投过票
            var subject = voteInfo.vote_subject;
            voteInfo.vote_count = subject ? subject.length : 0;
            var option  = subject[0]['options'],s_len = option.length;
            var has_vote = false;
            for (var i = 0; i < s_len; i++) {
                if(option[i].selected == true){
                    has_vote = true;
                }
            }
            voteInfo.has_vote = has_vote;//0表示没有投过，1表示已经投过
            //判断是否有登录态
            voteInfo.is_login = 1 ? 0 : 1;//1表示有登录态，0表示没有登录态
            //判断是否是粉丝
                        voteInfo.is_fans = false; //false表示不是粉丝，true表示是粉丝
                        voteInfo.super_vote_id = voteInfo.super_vote_id ? voteInfo.super_vote_id : getParam('supervoteid');

            //voteInfo = {"title":"12312313","vote_permission":1,"expire_time":1421510400,"total_person":0,"vote_subject":[{"type":1,"title":"1231","options":[{"name":"123","cnt":0,"selected":false},{"name":"123123","cnt":0,"selected":false}],"total_cnt":0,"vote_id":249},{"type":1,"title":"123","options":[{"name":"123","url":"http://mmbiz.qpic.cn/mmbiz/eDzkW7U0x16aN2YYibr1zoyIbNiboH4ySOxKE7EPn8xBX9ZMyuGibV8CzHPKWIdXYXWZBZDjeibzibVZuqx8MKubhnA/0","cnt":0,"selected":false},{"name":"123","url":"http://mmbiz.qpic.cn/mmbiz/eDzkW7U0x16aN2YYibr1zoyIbNiboH4ySOshMWVvvZgJBzwSh314y65iahROibxoTJDpKuY1EyjicphynmtZZwCIQag/0","cnt":0,"selected":false},{"name":"132","url":"http://mmbiz.qpic.cn/mmbiz/eDzkW7U0x16aN2YYibr1zoyIbNiboH4ySOnw8z0M7DapD2v8N7tNqfRbBafrkicQDV5VWtmAPIZrMO1OasDVcEHAQ/0","cnt":0,"selected":false}],"total_cnt":0,"vote_id":250}],"super_vote_id":151,"del_flag":0,"is_expired":0,"has_vote":false,"is_login":1,"is_fans":false};
            !!domload && (domload());
            domload = null;
        } 

    }         
</script>

    </body>
</html>


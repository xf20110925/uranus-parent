   è         <http://weixin.sogou.com/wechat/js/weixin_gzh_pc.min.js?v=1.2     %fŞÇ:      %CP         
     O K           È      Server   nginx   Date   Thu, 28 Jan 2016 05:22:34 GMT   Content-Type   application/x-javascript   Last-Modified   Tue, 01 Dec 2015 05:52:50 GMT   Vary   Accept-Encoding   Expires   Sat, 27 Feb 2016 05:22:34 GMT   Cache-Control   max-age=2592000   Content-Encoding   gzip !function(){function e(e){var i=$("#wxbox");if("needlogin"==e.code)$("#login_div").html('<p class="denglu-p"  style="display:none" id="login_p"><a id="login_for_gzhjs" btnName="gzh_detail" href="javascript:void(0);" name="top_login" uigs="weixin_pc_decfail_login"  >ç«å³ç»å½</a>ï¼å³å¯æ¥çæç« ã</p>'),$("#login_p").show();else{$("#login_div").html('<p class="look-more" style="display:none" id="login_p">å½ååªæ¾ç¤ºå100æ¡åå®¹ï¼<a  id="login_for_gzhjs" btnName="gzh_detail" href="javascript:void(0);" uigs="weixin_pc_login_100_gzh_detail" >ç»å½</a>å¯æ¥çæ´å¤ã</p>');var t=e.items,a=e.page,s=e.totalPages;if(0==t.length){""==r.query&&"inttime_all"==r.dateType&&(d=!1);var c='<h4 class="zj-tit"><span>è¯¥å¬ä¼å·ææªåå¸æç« </span></h4>';(""!=$.trim(r.query)||""==$.trim(r.query)&&"inttime_all"!=r.dateType)&&(c='<div id="gzh_detail_search_noresult" class="no-sosuo" style=" margin-top:50px;padding:62px 0 80px 268px"><strong>Sorry!</strong>æ¾ä¸å°ç¬¦åæ¡ä»¶çä¿¡æ¯ï¼è¿å<a href="javascript:void(0)" uigs="weixin_pc_gzh_detail_viewall_in" name=a_all>æ¥çå¨é¨</a>åå®¹ï¼</div>');var g=document.createElement("div");g.innerHTML=c,g.setAttribute("id",h),i.html(g,null),checkFavorite($("#"+h).find(".wx-rb.wx-rb3"),addFunctionBar),i.show()}else $("#gzh_detail_search_noresult").hide();$(".zj-news").show();var h="gzh_arts_"+a;if(o(e),t&&t.length>0){1==r.page&&i.show(),s>a&&(n().show(),n().css({visibility:"visible"}));var g=document.createElement("div");g.innerHTML=l.randerPc(t,r.auto_id),r.auto_id+=t.length,g.setAttribute("id",h),i.append(g,null),checkFavorite($("#"+h).find(".wx-rb.wx-rb3"),addFunctionBar)}}}function i(i){window.sogou=window.sogou||{},sogou.weixin_gzhcb||(sogou.weixin_gzhcb=e);var n=r,t=n.url;return"undefined"==typeof weixin_pc_is_login&&(weixin_pc_is_login=!1),params=n.getParams(i),n.page>=10&&!weixin_pc_is_login?($("#login_p").show(),void n.page--):$.ajax({url:t,data:params,dataType:"jsonp",jsonp:"cb",jsonpCallback:"sogou.weixin_gzhcb"})}function n(){return $("#wxmore")}function t(e,i,n){var t=document.getElementById("weixinname");if(t&&t.innerHTML.length>0&&(t=t.innerHTML,t=t.replace(/&amp;/g,"&"),document.getElementById("upquery")&&(document.getElementById("upquery").value=t),document.getElementById("bottom_form_querytext")&&(document.getElementById("bottom_form_querytext").value=t),document.title=t+"çå¬ä¼å·è¯¦æé¡µ â æçå¾®ä¿¡æç´¢"),passportUserId)s.sendPv("weixin","gzhjs_login",{status:0,from:"pc"});else if(s.sendPv("weixin","gzhjs_login",{status:1,from:"pc"}),"true"==n){var a='<p class="denglu-p" ><a id="login_for_gzhjs"  btnName="gzh_detail" href="javascript:void(0)" name="top_login">ç«å³ç»å½</a>ï¼å³å¯æ¥çæç« ã</p>';return 0==$("#login_for_gzhjs").size()&&$("#wxbox").append(a),void $("#wxbox").show()}}function a(e){var e=e||0;return e>10&&(e=10*Math.floor(e/10)),e}function o(e){if(0==d)$(".zj-news").hide();else{var i=$("#subSearchResult");""==$.trim(r.query)?i.hide():(i.find("#s_query").html(r.query),i.find("#s_total").html(a((e||{}).totalItems||0)),i.show())}}var r=new WeixinGzhArtSearch(weixin_gzh_openid,weixin_gzh_openid_ext,weixin_gzh_is_endecryptEnabled,window.aes);r.page=1;var l=new WeixinGzhUtil,s=new WeixinPvClUtil,d=!0;$(function(){$("body").on("click",function(e){$("[name=dtime]").hide()}),$("body").delegate("#login_for_gzhjs","click",function(){var e=$(this).attr("btn")||"";return s.sendPv("weixin","gzhjs_login",{status:2,from:"pc",btn:e}),showLogin(),!1});var e="";try{e=$("#weixinname").html()}catch(a){}""!=e&&$("a[t=q]").each(function(){var i=$(this).attr("href");$(this).attr("href",i+encodeURIComponent(e))}),$("input[name=query]").val(e),$("[data-change-per]").each(function(){var e=$(this).attr("data-change-per"),i=$(this).attr("data-change-sel"),n=$(e);$(this).find(i).each(function(){$(this).click(function(){return n.html($(this).html()+"<i></i>"),$("body").trigger("click"),!1})})}),t(weixin_gzh_openid,weixin_gzh_is_endecryptEnabled,weixin_gzh_login_enabled),i();var o=n();o.on("click",function(){o.css({visibility:"hidden"}),i(!0)}),$("[data-toggleShow]").click(function(){var e=$(this).attr("data-toggleShow");return $(e).toggle(),!1}),$("[data-sourceid]").click(function(){r.dateType=$(this).attr("data-sourceid")}),$("[name=gzhArtKeyWord]").change(function(){r.changeQuery($(this).val())}).bind("keypress",function(e){var i=13;e.keyCode==i&&(r.changeQuery($(this).val()),$("#gzhQuery").triggerHandler("click"))}).attr("maxlength",30),$('[name="cycle"]').click(function(){r.dateType=$(this).attr("data-sourceid"),$("#gzhQuery").triggerHandler("click")}),$("#gzhQuery").click(function(){$("#wxmore").hide(),$("#wxbox").html(""),i()}),$("body").delegate("a[name=a_all]","click",function(){window.location.reload()})})}();
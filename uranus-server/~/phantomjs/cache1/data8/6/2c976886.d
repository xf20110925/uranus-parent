   �         �http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/js/appmsg/a_report2b6c15.js,/mmbizwap/zh_CN/htmledition/js/biz_wap/utils/ajax2b6c15.js,/mmbizwap/zh_CN/htmledition/js/appmsg/page_pos2b6c15.js     %����	      %I7G�         
     O K           �   	   Server   
NWS_UGC_HY   Date   Fri, 29 Jan 2016 05:49:35 GMT   Cache-Control   max-age=31536000   Expires   Sat, 28 Jan 2017 05:49:35 GMT   Last-Modified   Fri, 29 Jan 2016 05:40:00 GMT   Content-Type   application/x-javascript   Content-Encoding   gzip   X-Cache-Lookup   Hit From Disktank Gz   Access-Control-Allow-Origin   * define("appmsg/a_report.js",["biz_wap/utils/ajax.js"],function(t){
"use strict";
function o(t,o){
var a="https:"==top.location.protocol?1500:1200,e="/mp/advertisement_report?r="+Math.random()+"&",p=[],s=!1;
for(var c in t)t.hasOwnProperty(c)&&p.push(c+"="+t[c]);
e+=p.join("&"),r({
url:e,
mayAbort:!0,
type:"GET",
success:function(){
n&&n(56+i);
},
error:function(){
n&&n(57+i);
},
complete:function(){
s||(s=!0,!!o&&o());
},
async:!0
}),setTimeout(function(){
s||(s=!0,window.__ajaxtest="1",!!o&&o());
},a);
}
var r=t("biz_wap/utils/ajax.js"),n=window.__report,a=top.location.protocol,i="https:"==a?5:0;
return{
AdClickReport:o
};
});define("biz_wap/utils/ajax.js",["biz_common/utils/url/parse.js"],function(e){
"use strict";
function t(e){
var t={};
return"undefined"!=typeof uin&&(t.uin=uin),"undefined"!=typeof key&&(t.key=key),
"undefined"!=typeof pass_ticket&&(t.pass_ticket=pass_ticket),"undefined"!=typeof wxtoken&&(t.wxtoken=wxtoken),
"undefined"!=typeof top.window.devicetype&&(t.devicetype=top.window.devicetype),
"undefined"!=typeof top.window.clientversion&&(t.clientversion=top.window.clientversion),
t.x5=r?"1":"0",n.join(e,t);
}
function o(e){
var o=(e.type||"GET").toUpperCase(),n=t(e.url),r=!!e.mayAbort,f="undefined"==typeof e.async?!0:e.async,p=new XMLHttpRequest,l=null,y=null;
if("object"==typeof e.data){
var w=e.data;
y=[];
for(var m in w)w.hasOwnProperty(m)&&y.push(m+"="+encodeURIComponent(w[m]));
y=y.join("&");
}else y="string"==typeof e.data?e.data:null;
p.open(o,n,f),p.onreadystatechange=function(){
if(3==p.readyState&&e.received&&e.received(p),4==p.readyState){
p.onreadystatechange=null;
var t=p.status;
if(t>=200&&400>t)try{
e.success&&e.success(p.responseText);
}catch(o){
throw i({
offset:a,
e:o
}),o;
}else{
try{
e.error&&e.error(p);
}catch(o){
throw i({
offset:c,
e:o
}),o;
}
if(t||!r){
var u=window.__ajaxtest||"0";
i({
offset:s,
log:"ajax_network_error["+t+"]["+u+"]: "+n+";host:"+top.location.host,
e:""
});
}
}
clearTimeout(l);
try{
e.complete&&e.complete();
}catch(o){
throw i({
offset:d,
e:o
}),o;
}
e.complete=null;
}
},"POST"==o&&p.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8"),
p.setRequestHeader("X-Requested-With","XMLHttpRequest"),"undefined"!=typeof e.timeout&&(l=setTimeout(function(){
p.abort("timeout");
try{
e.complete&&e.complete();
}catch(t){
throw i({
offset:d,
e:t
}),t;
}
e.complete=null,i({
offset:u,
log:"ajax_timeout_error: "+n,
e:""
});
},e.timeout));
try{
p.send(y);
}catch(h){
e.error&&e.error();
}
}
var n=e("biz_common/utils/url/parse.js"),r=-1!=navigator.userAgent.indexOf("TBS/"),i=window.__moon_report||function(){},a=3,s=4,c=5,u=6,d=7;
return o;
});define("appmsg/page_pos.js",["biz_common/utils/string/html.js","biz_common/dom/event.js","biz_wap/utils/ajax.js","biz_common/utils/cookie.js","appmsg/cdn_img_lib.js","biz_wap/utils/storage.js"],function(e){
"use strict";
function t(e){
for(var t=5381,o=0;o<e.length;o++)t=(t<<5)+t+e.charCodeAt(o),t&=2147483647;
return t;
}
function o(e,t){
if(e&&!(e.length<=0))for(var o,n,i,a=/http(s)?\:\/\/([^\/\?]*)(\?|\/)?/,l=0,r=e.length;r>l;++l)o=e[l],
o&&(n=o.getAttribute(t),n&&(i=n.match(a),i&&i[2]&&(w[i[2]]=!0)));
}
function n(e){
for(var t=0,o=f.length;o>t;++t)if(f[t]==e)return!0;
return!1;
}
function i(){
w={},o(document.getElementsByTagName("a"),"href"),o(document.getElementsByTagName("link"),"href"),
o(document.getElementsByTagName("iframe"),"src"),o(document.getElementsByTagName("script"),"src"),
o(document.getElementsByTagName("img"),"src");
var e=[];
for(var t in w)w.hasOwnProperty(t)&&(window.networkType&&"wifi"==window.networkType&&!_&&n(t)&&(_=!0),
e.push(t));
return w={},e.join(",");
}
function a(){
var e,t=window.pageYOffset||document.documentElement.scrollTop,o=document.getElementById("js_content"),n=document.documentElement.clientHeight||window.innerHeight,a=document.body.scrollHeight||document.body.offsetHeight,l=Math.ceil(a/n),m=Math.ceil((o.scrollHeight||o.offsetHeight)/n),d=(window.logs.read_height||t)+n,g=document.getElementById("js_toobar3").offsetTop,w=o.getElementsByTagName("img")||[],f=Math.ceil(d/n)||1,p=document.getElementById("media"),h=50,u=0,y=0,v=0,b=0,T=d+h>g?1:0;
f>l&&(f=l);
var j=function(t){
if(t)for(var o=0,n=t.length;n>o;++o){
var i=t[o];
if(i){
u++;
var a=i.getAttribute("src"),l=i.getAttribute("data-type");
a&&0==a.indexOf("http")&&(y++,a.isCDN()&&(v++,-1!=a.indexOf("tp=webp")&&b++),l&&(e["img_"+l+"_cnt"]=e["img_"+l+"_cnt"]||0,
e["img_"+l+"_cnt"]++));
}
}
e.download_cdn_webp_img_cnt=b||0,e.download_img_cnt=y||0,e.download_cdn_img_cnt=v||0,
e.img_cnt=u||0;
},O=window.appmsgstat||{},x=window.logs.img||{},z=window.logs.pagetime||{},E=x.load||{},k=x.read||{},D=[],B=[],N=0,S=0,I=0;
for(var H in k)H&&0==H.indexOf("http")&&k.hasOwnProperty(H)&&B.push(H);
for(var H in E)H&&0==H.indexOf("http")&&E.hasOwnProperty(H)&&D.push(H);
for(var M=0,P=D.length;P>M;++M){
var Y=D[M];
Y&&Y.isCDN()&&(-1!=Y.indexOf("/0")&&N++,-1!=Y.indexOf("/640")&&S++,-1!=Y.indexOf("/300")&&I++);
}
var e={
__biz:biz,
title:msg_title.htmlDecode(),
mid:mid,
idx:idx,
read_cnt:O.read_num||0,
like_cnt:O.like_num||0,
screen_height:n,
screen_num:m,
idkey:"",
copyright_stat:"",
ori_article_type:"",
video_cnt:window.logs.video_cnt||0,
read_screen_num:f||0,
is_finished_read:T,
scene:source,
content_len:c.content_length||0,
start_time:page_begintime,
end_time:(new Date).getTime(),
img_640_cnt:S,
img_0_cnt:N,
img_300_cnt:I,
wtime:z.wtime||0,
ftime:z.ftime||0,
ptime:z.ptime||0,
reward_heads_total:window.logs.reward_heads_total||0,
reward_heads_fail:window.logs.reward_heads_fail||0
};
if(window.networkType&&"wifi"==window.networkType&&(e.wifi_all_imgs_cnt=D.length,
e.wifi_read_imgs_cnt=B.length),window.logs.webplog&&4==window.logs.webplog.total){
var A=window.logs.webplog;
e.webp_total=1,e.webp_lossy=A.lossy,e.webp_lossless=A.lossless,e.webp_alpha=A.alpha,
e.webp_animation=A.animation;
}
if(e.copyright_stat=window._copyright_stat||"",e.ori_article_type=window._ori_article_type||"",
window.logs.idkeys){
var C=window.logs.idkeys,J=[];
for(var R in C)if(C.hasOwnProperty(R)){
var q=C[R];
q.val>0&&J.push(R+"_"+q.val);
}
e.idkey=J.join(";");
}
j(!!p&&p.getElementsByTagName("img")),j(w);
var L=(new Date).getDay(),$=i();
(_||0!==user_uin&&Math.floor(user_uin/100)%7==L)&&(e.domain_list=$),_&&(e.html_content=s),
r({
url:"/mp/appmsgreport?action=page_time",
type:"POST",
mayAbort:!0,
data:e,
async:!1,
timeout:2e3
});
}
e("biz_common/utils/string/html.js");
{
var l=e("biz_common/dom/event.js"),r=e("biz_wap/utils/ajax.js");
e("biz_common/utils/cookie.js");
}
e("appmsg/cdn_img_lib.js");
var s,m=e("biz_wap/utils/storage.js"),d=new m("ad"),g=new m("page_pos"),c={};
!function(){
if(s=document.getElementsByTagName("html"),s&&1==!!s.length){
s=s[0].innerHTML;
var e=s.replace(/[\x00-\xff]/g,""),t=s.replace(/[^\x00-\xff]/g,"");
c.content_length=1*t.length+3*e.length+"<!DOCTYPE html><html></html>".length;
}
window.logs.pageinfo=c;
}();
var w={},_=!1,f=["wap.zjtoolbar.10086.cn","125.88.113.247","115.239.136.61","134.224.117.240","hm.baidu.com","c.cnzz.com","w.cnzz.com","124.232.136.164","img.100msh.net","10.233.12.76","wifi.witown.com","211.137.132.89","qiao.baidu.com","baike.baidu.com"],p=null,h=0,u=msg_link.split("?").pop(),y=t(u);
!function(){
if(window.localStorage&&!localStorage.getItem("clear_page_pos")){
for(var e=localStorage.length-1;e>=0;){
var t=localStorage.key(e);
t.match(/^\d+$/)?localStorage.removeItem(t):t.match(/^adinfo_/)&&localStorage.removeItem(t),
e--;
}
localStorage.setItem("clear_page_pos","true");
}
}(),window.localStorage&&(l.on(window,"load",function(){
h=1*g.get(y);
var e=location.href.indexOf("scrolltodown")>-1?!0:!1,t=(document.getElementById("img-content"),
document.getElementById("js_cmt_area"));
if(e&&t&&t.offsetTop){
var o=t.offsetTop;
window.scrollTo(0,o-25);
}else window.scrollTo(0,h);
}),l.on(window,"unload",function(){
if(g.set(o,h,+new Date+72e5),window.__ajaxtest="2",window._adRenderData&&"undefined"!=typeof JSON&&JSON.stringify){
var e=JSON.stringify(window._adRenderData),t=+new Date,o=[biz,sn,mid,idx].join("_");
d.set(o,{
info:e,
time:t
},+new Date+24e4);
}
a();
}),window.logs.read_height=0,l.on(window,"scroll",function(){
var e=window.pageYOffset||document.documentElement.scrollTop;
window.logs.read_height=Math.max(window.logs.read_height,e),clearTimeout(p),p=setTimeout(function(){
h=window.pageYOffset,g.set(y,h,+new Date+72e5);
},500);
}),l.on(document,"touchmove",function(){
var e=window.pageYOffset||document.documentElement.scrollTop;
window.logs.read_height=Math.max(window.logs.read_height,e),clearTimeout(p),p=setTimeout(function(){
h=window.pageYOffset,g.set(y,h,+new Date+72e5);
},500);
}));
});
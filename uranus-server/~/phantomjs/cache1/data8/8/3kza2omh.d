   �         �http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/js/pages/music_player2b674b.js,/mmbizwap/zh_CN/htmledition/js/pages/voice_component2b674b.js     %���B      %Hw��              �      
     O K   	   Server   
NWS_UGC_HY   Date   Thu, 28 Jan 2016 13:45:32 GMT   Cache-Control   max-age=31536000   Expires   Fri, 27 Jan 2017 13:45:32 GMT   Last-Modified   Thu, 28 Jan 2016 11:30:00 GMT   Content-Type   application/x-javascript   Content-Encoding   gzip   X-Cache-Lookup   Hit From Disktank Gz   Access-Control-Allow-Origin   * define("pages/music_player.js",["biz_common/dom/event.js","biz_wap/jsapi/core.js","pages/version4video.js","biz_common/utils/monitor.js"],function(t){
"use strict";
function o(t){
this._o={
type:0,
src:"",
mid:"",
songId:"",
autoPlay:!1,
duration:0,
debug:!1,
needVioceMutex:!0,
appPlay:!0,
title:"",
singer:"",
epname:"",
coverImgUrl:"",
webUrl:"",
onStatusChange:function(){},
onTimeupdate:function(){},
onError:function(){}
},this._extend(t),this._status=-1,this._g={},0!==l.surportType&&(this._o.needVioceMutex&&l.mutexPlayers.push(this),
this._o.autoPlay&&this.play());
}
function i(t){
_.invoke("musicPlay",{
app_id:"a",
title:"微信公众平台",
singer:"微信公众平台",
epname:"微信公众平台",
coverImgUrl:"http://res.wx.qq.com/mpres/htmledition/images/favicon.ico",
dataUrl:l.ev,
lowbandUrl:l.ev,
webUrl:"http://mp.weixin.qq.com/s?"
},function(o){
"function"==typeof t&&t(o);
});
}
function e(t){
for(var o=0,i=l.mutexPlayers.length;i>o;o++){
var e=l.mutexPlayers[o];
e&&"function"==typeof e._onPause&&e!=t&&(e._h5Audio&&"function"==typeof e._h5Audio.pause?e._h5Audio.pause():1==e.getSurportType()&&e._pauseJsapiPlay(!1));
}
}
function n(){
return l.surportType;
}
function s(t){
return new o(t);
}
function a(){
l.surportType>0&&l.isAndroidLow&&window.addEventListener("canplay",function(t){
t.target&&"function"==typeof t.target.play&&t.target.play();
},!0);
}
function u(){
for(var t=0,o=l.keyConf.length;o>t;t++){
var i=l.keyConf[t];
l.reportData[i]={
key:t,
count:0
};
}
h.on(window,"unload",r);
}
function r(){
for(var t=0,o=l.mutexPlayers.length;o>t;t++){
var i=l.mutexPlayers[t];
if(i&&1==i._status&&1==i._surportType){
d(i._o.type,"unload_wx_pv",1);
break;
}
}
p();
}
function p(){
var t=l.reportId;
if(1==parseInt(10*Math.random())||l.debug){
for(var o in l.reportData){
var i=l.reportData[o];
i.count>0&&y.setSum(t,i.key,i.count);
}
y.send();
}
}
function d(t,o,i){
0==t||1==t?o="m_"+o:(2==t||3==t)&&(o="v_"+o),l.reportData[o]&&(i=i||1,l.reportData[o].count+=i,
l.debug&&console.log("addpv:"+o+" count:"+l.reportData[o].count));
}
var h=t("biz_common/dom/event.js"),_=t("biz_wap/jsapi/core.js"),c=t("pages/version4video.js"),y=t("biz_common/utils/monitor.js"),l={
hasCheckJsapi:!1,
ev:window._empty_v,
isAndroidLow:/android\s2\.3/i.test(navigator.userAgent),
surportType:"addEventListener"in window?2:0,
mutexPlayers:[],
reportId:"28306",
keyConf:["m_pv","m_wx_pv","m_h5_pv","m_unload_wx_pv","v_pv","v_wx_pv","v_h5_pv","v_unload_wx_pv","no_copyright","copyright_cgi_err","copyright_net_err","copyright_timeout","copyright_other_err","overseas","fee","musicid_error"],
reportData:{},
debug:-1!=location.href.indexOf("&_debug=1")?!0:!1
};
return u(),a(),o.prototype._createAutoAndPlay=function(){
if(this._h5Audio=document.createElement("audio"),this._H5bindEvent(),this._h5Audio.setAttribute("style","height:0;width:0;display:none"),
this._h5Audio.setAttribute("autoplay",""),this._status=0,l.isAndroidLow)this._h5Audio.src=this._o.src,
document.body.appendChild(this._h5Audio),this._h5Audio.load();else{
document.body.appendChild(this._h5Audio);
var t=this;
setTimeout(function(){
t._h5Audio.src=t._o.src,t._h5Audio.play();
},0);
}
this._surportType=2;
},o.prototype._destoryH5Audio=function(){
this._h5Audio&&"function"==typeof this._h5Audio.pause&&(this._h5Audio.pause(),document.body.removeChild(this._h5Audio),
this._h5Audio=null,this._status=-1,this._surportType=0);
},o.prototype._createApp=function(t){
this._h5Audio&&this._destoryH5Audio();
var o=this,i=this._o;
_.invoke("musicPlay",{
app_id:"a",
title:i.title,
singer:i.singer,
epname:i.epname,
coverImgUrl:i.coverImgUrl,
dataUrl:i.src,
lowbandUrl:i.src,
webUrl:i.webUrl
},function(e){
o._g.checkJsapiTimeoutId&&clearTimeout(o._g.checkJsapiTimeoutId),e.err_msg.indexOf("ok")>=0?(d(o._o.type,"wx_pv",1),
o._surportType=1,l.surportType=1,o.jsApiData&&o.jsApiData.updateTimeoutId&&clearTimeout(o.jsApiData.updateTimeoutId),
o.jsApiData={
starTime:+new Date,
curTime:0,
updateTimeoutId:null,
duration:i.duration||void 0
},o._onPlay(),"undefined"!=typeof i.duration&&1*i.duration>0&&o._analogUpdateTime()):2===l.surportType?o._h5Play(t):o._onError({},15);
});
},o.prototype._analogUpdateTime=function(){
function t(){
return i.curTime=1*((+new Date-i.starTime)/1e3).toFixed(2),i.curTime>=i.duration?void o._stopJsapiPlay(!1):(o._onTimeupdate(null,i.curTime),
void(i.updateTimeoutId=setTimeout(function(){
t();
},1e3)));
}
var o=this,i=o.jsApiData;
t();
},o.prototype._onPlay=function(t){
this._status=1;
try{
e(this);
}catch(t){}
"function"==typeof this._o.onStatusChange&&this._o.onStatusChange.call(this,t||{},this._status);
},o.prototype._onPause=function(t){
this._status=2,"function"==typeof this._o.onStatusChange&&this._o.onStatusChange.call(this,t||{},this._status);
},o.prototype._onEnd=function(t){
this._status=3,"function"==typeof this._o.onStatusChange&&this._o.onStatusChange.call(this,t||{},this._status);
},o.prototype._onLoadedmetadata=function(t){
"function"==typeof this._o.onLoadedmetadata&&this._o.onLoadedmetadata.call(this,t||{});
},o.prototype._onTimeupdate=function(t,o){
"function"==typeof this._o.onTimeupdate&&this._o.onTimeupdate.call(this,t||{},o);
},o.prototype._onError=function(t,o){
this._status=-1,"function"==typeof this._o.onError&&this._o.onError.call(this,t||{},o);
},o.prototype._H5bindEvent=function(){
var t=this;
this._h5Audio.addEventListener("play",function(o){
t._onPlay(o);
},!1),this._h5Audio.addEventListener("ended",function(o){
t._onEnd(o);
},!1),this._h5Audio.addEventListener("pause",function(o){
t._onPause(o);
},!1),this._h5Audio.addEventListener("error",function(o){
var i=o.target.error.code;
(1>i||i>5)&&(i=5),t._onError(o,i);
},!1),"function"==typeof this._o.onTimeupdate&&this._h5Audio.addEventListener("timeupdate",function(o){
t._onTimeupdate(o,t._h5Audio.currentTime);
},!1),"function"==typeof this._o.onLoadedmetadata&&this._h5Audio.addEventListener("loadedmetadata",function(o){
t._onLoadedmetadata(o);
},!1);
},o.prototype._extend=function(t){
for(var o in t)this._o[o]=t[o];
},o.prototype._pauseJsapiPlay=function(t){
this._stopJsapiPlay(t);
},o.prototype._stopJsapiPlay=function(t){
function o(){
n.updateTimeoutId&&clearTimeout(n.updateTimeoutId),n.updateTimeoutId=null,n.curTime=0,
e._onTimeupdate(null,0),e._onEnd();
}
var e=this,n=e.jsApiData;
t?i(function(){
o();
}):o();
},o.prototype._h5Play=function(t){
(2===l.surportType||!this._o.appPlay&&1===l.surportType)&&(d(this._o.type,"h5_pv",1),
this._h5Audio?(this._h5Audio.ended||this._h5Audio.paused)&&(this._h5Audio.ended&&(this._h5Audio.currentTime=0),
"undefined"!=typeof t?(this._h5Audio.currentTime=t,this._h5Audio.play()):this._h5Audio.play()):this._createAutoAndPlay());
},o.prototype.getSurportType=function(){
return this._surportType||0;
},o.prototype.getPlayStatus=function(){
return this._status;
},o.prototype.getCurTime=function(){
return 1==this._surportType&&this.jsApiData?this.jsApiData.curTime||0:this._h5Audio?this._h5Audio.currentTime:0;
},o.prototype.getDuration=function(){
return 1==this._surportType&&this.jsApiData?this.jsApiData.duration||void 0:this._h5Audio?this._h5Audio.duration||this._o.duration:void 0;
},o.prototype.pause=function(){
1==this._surportType?this._pauseJsapiPlay(!0):2==this._surportType&&this._h5Audio&&"function"==typeof this._h5Audio.pause&&this._h5Audio.pause();
},o.prototype.stop=function(){
2==this._surportType&&this._h5Audio?(this._h5Audio.pause(),this._h5Audio.currentTime=0,
this._onEnd()):1==this._surportType&&this._stopJsapiPlay(!0);
},o.prototype.play=function(t){
var o=this,i=this._g;
d(this._o.type,"pv",1),i.checkJsapiTimeoutId&&clearTimeout(i.checkJsapiTimeoutId),
c.device.inWechat&&this._o.appPlay?1!=this._status&&(this._createApp(t),i.checkJsapiTimeoutId=setTimeout(function(){
o._h5Play(t);
},1e3)):this._h5Play(t);
},o.prototype.monitor=function(t,o){
d(-1,t,o);
},{
init:s,
getSurportType:n
};
});define("pages/voice_component.js",["biz_common/dom/event.js","biz_common/tmpl.js","pages/loadscript.js","pages/music_player.js","biz_common/dom/class.js","pages/report.js"],function(t){
"use strict";
function o(t){
this._o={
type:0,
comment_id:"",
src:"",
mid:"",
songId:"",
autoPlay:!1,
duration:0,
debug:!1,
needVioceMutex:!0,
appPlay:!0,
title:"",
singer:"",
epname:"",
coverImgUrl:"",
webUrl:[location.protocol,"//mp.weixin.qq.com/s?referFrom=#referFrom#&songid=#songId#&__biz=",window.biz,"&mid=",window.mid,"&idx=",window.idx,"&sn=",window.sn,"#wechat_redirect"].join(""),
playingCss:"",
playCssDom:"",
playArea:"",
progress:"",
detailUrl:"",
detailArea:""
},this._init(t);
}
function e(t,o,e,i){
h.num++,o.musicSupport=h.musicSupport,o.show_not_support=!1,h.musicSupport||1!=h.num||(o.show_not_support=!0);
var r=document.createElement("div"),n="";
n=i?c.render(t,o):c.tmpl(t,o),r.innerHTML=n;
var p=e.parentNode;
p&&(p.lastChild===e?p.appendChild(r.children[0]):p.insertBefore(r.children[0],e.nextSibling));
}
function i(){
"undefined"==typeof window.reportVoiceid&&(window.reportVoiceid=[]),"undefined"==typeof window.reportMid&&(window.reportMid=[]);
}
function r(){
a.on(window,"unload",n);
}
function n(){
for(var t in h.reportData)y.musicreport({
data:h.reportData[t]
});
}
function p(t){
var o="//open.music.qq.com/fcgi-bin/fcg_music_get_song_info_weixin.fcg?song_id=#songid#&mid=#mid#&format=json&app_id=100311669&app_key=55d6cdaee6fb3a41275a48067f8d7638&device_id=weixin&file_type=mp3&qqmusic_fromtag=50&callback=get_song_info_back";
o=o.replace("#mid#",t.mid).replace("#songid#",t.id),d({
url:o,
timeout:3e4,
callbackName:"get_song_info_back",
callback:function(o){
if(!o||"undefined"==typeof o.ret)return void("function"==typeof t.onError&&t.onError({
errcode:1
}));
var e=1;
1001==o.ret?e=0:1002==o.ret?e=2:1003==o.ret?e=3:1004==o.ret&&(e=4),t.onSuc({
status:e
});
},
onerror:function(o){
var e=4;
switch(1*o){
case 400:
e=2;
break;

case 500:
e=3;
break;

default:
e=4;
}
"function"==typeof t.onError&&t.onError({
errcode:e
});
}
});
}
function s(t){
return new o(t);
}
var a=t("biz_common/dom/event.js"),c=t("biz_common/tmpl.js"),d=t("pages/loadscript.js"),u=t("pages/music_player.js"),_=t("biz_common/dom/class.js"),y=t("pages/report.js"),h={
musicSupport:u.getSurportType(),
reportData:{},
posIndex:{},
qqMusiceSongId:"http://thirdparty.gtimg.com/#songId#.m4a?fromtag=38&songid=#songId#",
qqMusiceMid:"http://thirdparty.gtimg.com/C100#mid#.m4a?fromtag=38&songid=#songId#",
num:0
};
return i(),r(),o.prototype._init=function(t){
this._extend(t),this._g={
copyright:-1,
check_copyright:!1
},this._initSrc(),this._initQQmusicLyric(),this._initReportData(),this._initPlayer(),
this._playEvent();
},o.prototype._initSrc=function(){
var t=this._o;
t.src||(0==t.type||1==t.type)&&(t.mid?t.src=h.qqMusiceMid.replace("#mid#",t.mid).replace(/#songId#/g,t.songId||""):t.songId&&(t.src=h.qqMusiceSongId.replace(/#songId#/g,t.songId||"")));
},o.prototype._initQQmusicLyric=function(){
var t=this._o;
t.webUrl=0==t.type||1==t.type?t.webUrl.replace("#songId#",t.songId||"").replace("#referFrom#","music.qq.com"):t.webUrl.replace("#songId#","").replace("#referFrom#","");
},o.prototype._initReportData=function(){
var t=this._o;
2==t.type||3==t.type?window.reportVoiceid.push(t.songId):(0==t.type||1==t.type)&&window.reportMid.push(t.songId),
"undefined"==typeof h.reportData[t.type]&&(h.reportData[t.type]=y.getMusicReportData(t),
h.posIndex[t.type]=0),this._g.posIndex=h.posIndex[t.type]++;
var o=h.reportData[t.type];
o.musicid.push(t.songId),o.commentid.push(t.comment_id),o.hasended.push(0),o.mtitle.push(t.title),
o.detail_click.push(0),o.duration.push(parseInt(1e3*t.duration)),o.errorcode.push(0),
o.play_duration.push(0);
},o.prototype._initPlayer=function(){
h.musicSupport&&(this._o.onStatusChange=this._statusChangeCallBack(),this._o.onTimeupdate=this._timeupdateCallBack(),
this._o.onError=this._errorCallBack(),this.player=new u.init(this._o));
},o.prototype._playEvent=function(){
var t=this,o=this._o,e=this._g;
if(h.musicSupport){
var i=0;
2==o.type||3==o.type?i=3:(0==o.type||1==o.type)&&(i=1),a.tap(o.playArea,function(){
return _.hasClass(o.playCssDom,o.playingCss)?(t.player.stop(),y.report({
type:i,
comment_id:o.comment_id,
voiceid:o.songId,
action:5
})):3==i?t._playMusic(3):1==i&&t._checkCopyright(function(){
t._playMusic(1);
}),!1;
});
}
o.detailUrl&&o.detailArea&&a.tap(o.detailArea,function(){
t._checkCopyright(function(){
h.reportData[o.type].detail_click[e.posIndex]=1,window.location.href=o.detailUrl;
});
});
},o.prototype._checkCopyright=function(t){
var o=this,e=this._o,i=this._g;
return 1*i.copyright===1&&"function"==typeof t?void t():void(this._musicCopyrightWarnning()!==!0&&(i.check_copyright||(i.check_copyright=!0,
p({
id:e.songId,
mid:e.mid,
onSuc:function(e){
return i.check_copyright=!1,i.copyright=1*e.status,1==i.copyright?void("function"==typeof t&&t()):void(o._musicCopyrightWarnning(!0)===!0);
},
onError:function(t){
i.check_copyright=!1,o.player.monitor(1==t.errcode?"copyright_cgi_err":2==t.errcode?"copyright_net_err":3==t.errcode?"copyright_timeout":"copyright_other_err");
}
}))));
},o.prototype._musicCopyrightWarnning=function(t){
var o=this._g;
return 1*o.copyright===0?(alert("该歌曲版权已过期，无法播放"),t===!0&&this.player.monitor("no_copyright"),
!0):1*o.copyright===2?(alert("抱歉，应版权方要求，当前国家或地区暂不提供此歌曲服务"),t===!0&&this.player.monitor("overseas"),
!0):1*o.copyright===3?(alert("该歌曲版权已过期，无法播放"),t===!0&&this.player.monitor("fee"),
!0):1*o.copyright===4?(alert("抱歉，歌曲信息不正确"),t===!0&&this.player.monitor("musicid_error"),
!0):!1;
},o.prototype._playMusic=function(t){
var o=this._o,e=this._g;
this.player.play(0),h.reportData[o.type].hasended[e.posIndex]=1,y.report({
type:t,
comment_id:o.comment_id,
voiceid:o.songId,
action:4
});
},o.prototype._extend=function(t){
for(var o in t)this._o[o]=t[o];
},o.prototype._statusChangeCallBack=function(){
var t=this;
return function(o,e){
t._updatePlayerCss(this,e);
};
},o.prototype._timeupdateCallBack=function(){
var t=this,o=this._o,e=this._g;
return function(i,r){
t._updateProgress(this,r),0!=r&&(h.reportData[o.type].play_duration[e.posIndex]=parseInt(1e3*r));
};
},o.prototype._errorCallBack=function(){
var t=this,o=this._o,e=this._g;
return function(i,r){
h.reportData[o.type].errorcode[e.posIndex]=r,t._updatePlayerCss(this,3);
};
},o.prototype._updatePlayerCss=function(t,o){
var e=this._o,i=e.playCssDom,r=e.progress;
2==o||3==o?(_.removeClass(i,e.playingCss),!!r&&(r.style.width=0)):1==o&&_.addClass(i,e.playingCss);
},o.prototype._updateProgress=function(t,o){
var e=this._o,i=e.progress,r=t.getDuration();
r&&i&&(i.style.width=this._countProgress(r,o));
},o.prototype._countProgress=function(t,o){
return o/t*100+"%";
},{
init:s,
renderPlayer:e
};
});
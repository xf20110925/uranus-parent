   �         6http://weixin.sogou.com/wechat/js/weixin_qrcode.min.js     %f޻�      %~�n*H         
     O K           �      Server   nginx   Date   Thu, 28 Jan 2016 05:22:31 GMT   Content-Type   application/x-javascript   Last-Modified   Wed, 14 Oct 2015 06:39:57 GMT   Vary   Accept-Encoding   Expires   Sat, 27 Feb 2016 05:22:31 GMT   Cache-Control   max-age=2592000   Content-Encoding   gzip function qrcodelog(o){window.console&&console.log(o)}function setOpacity(o,t){o.filters?o.style.filter="alpha(opacity="+t+")":o.style.opacity=t/100}function setRight(o,t){o.style.left=t+"px"}function qrcode(){}var qrcodenum=0;window.qrcode_type=window.qrcode_type||"1",qrcode.prototype.init=function(o){var t=this,e=this.box=o.box;this.box2=o.box2,this.img=o.img,bind(e,"mouseover",function(o){t.mouseover(o)}),bind(e,"mouseout",function(o){t.mouseout(o)})},qrcode.prototype.imginit=function(){setOpacity(this.img,this.opacity),setRight(this.img,this.right)},qrcode.prototype.move=function(o){var t=this,e=400,i=32,n=32,r=parseInt(o/e*100);r>100&&(r=100);var s=parseInt(o/e*n);return s>n&&(s=n),s>i&&(s=n-s),t.right=s-n,t.opacity=r,t.imginit(),e>o},qrcode.prototype.mouseover=function(){var o=this;o.outh&&(o.outh=clearTimeout(o.outh)),o.on||(o.on=!0,this.overh=setTimeout(function(){function t(){var i=(new Date).getTime();o.move(i-e)&&(o.moveh=setTimeout(function(){t()},50))}o.opacity=0,o.right=0,o.imginit(),o.box2?addClass(o.box2,"on"):(addClass(o.box,"on"),addClass(o.box,"hover"));var e=(new Date).getTime();t()},10),qrcodelog("mouseover"))},qrcode.prototype.mouseout=function(o){var t=o.relatedTarget;if((!t||o.toElement)&&(t=o.toElement),!t||!contains(this.box,t)&&t!=this.box){var e=this;this.outh=setTimeout(function(){e.overh&&clearTimeout(e.overh),e.moveh&&clearTimeout(e.moveh),e.on=!1,e.box2?removeClass(e.box2,"on"):(removeClass(e.box,"on"),removeClass(e.box,"hover"))},10),qrcodelog("mouseout")}},bind(window,"load",function(){var o,t,e,i=$s.$("main");if(o=$sc(i,"_item"))for(var n=0;n<o.length;n++)t=$sc(o[n],"pos-box")[0],e=$sc(o[n],"v-box"),e=e.length?e[0]:null,(new qrcode).init({box:o[n],img:t,box2:e})});
   �         Ahttp://weixin.sogou.com/antispider/static/js/index.min.js?v=0.1.2�       ����      %~�7��              �      
     O K      Server   nginx   Date   Thu, 28 Jan 2016 08:30:02 GMT   Content-Type   application/x-javascript   Vary   Accept-Encoding   Last-Modified   Tue, 03 Nov 2015 10:19:55 GMT   Content-Encoding   gzip $(function(){function c(){$("#seccodeImage").attr("src","util/seccode.php?tc="+(new Date).getTime())}function k(a){var b=a.code,d=$("#error-tips"),e=new Date,f="sogou.com";"snapshot.sogoucdn.com"==location.hostname&&(f="snapshot.sogoucdn.com");if(0===b||1===b)getCookie("SUV")||setCookie("SUV",1E3*e.getTime()+Math.round(1E3*Math.random()),"Sun, 29 July 2046 00:00:00 UTC",f,"/"),getCookie("SNUID")||(e.setTime(e.getTime()+432E6),setCookie("SNUID",a.id,e.toGMTString(),f,"/"));switch(b){case 0:sendLog("0_seccodeInputSuccess");setCookie("seccodeRight","success",getUTCString(120),location.hostname,"/");sendLog("0_seccodeInputSuccess");verifyCount("successCount",3);setTimeout(function(){location.href=encodeURI(l)},50);break;case 1:d.html(a.msg||"\u89e3\u5c01\u5931\u8d25, \u8bf7\u7a0d\u540e\u518d\u8bd5");d.show();c();sendLog("1_deblockingFail");break;case 2:sendLog("2_unknowSource");location.href="/";break;case 3:d.html(a.msg||"\u9a8c\u8bc1\u7801\u8f93\u5165\u9519\u8bef, \u8bf7\u91cd\u65b0\u8f93\u5165\uff01");d.show();c();sendLog("3_seccodeInputError");verifyCount("seccodeErrorCount",3);break;default:d.html("\u89e3\u5c01\u5931\u8d25, \u8bf7\u91cd\u8bd5"),d.show(),c()}}function m(a){var b=$("#error-tips");"timeout"==a?b.html("\u8bf7\u6c42\u8d85\u65f6\uff0c\u8bf7\u68c0\u67e5\u60a8\u7684\u7f51\u7edc\u72b6\u51b5"):b.html("\u89e3\u5c01\u5931\u8d25, \u8bf7\u91cd\u8bd5");b.show();c()}function g(){var a=$("#error-tips"),b=h.val();/^[\da-zA-Z]{6}$/.test(b)?$.ajax({type:"POST",url:"thank.php",data:{c:b,r:$("#from").val(),v:5},dataType:"json",success:k,error:m}):(a.html("\u9a8c\u8bc1\u7801\u8f93\u5165\u9519\u8bef\uff0c\u8bf7\u91cd\u65b0\u8f93\u5165"),a.show());h.val("")}var h=$("#seccodeInput"),l=decodeURIComponent($("#from").val());$("#change-img, #seccodeImage").on("click",c);$("#submit").on("click",g);$("#seccodeForm").on("submit",function(){g();return!1});(function(a){var b=document.getElementById("seccodeInput"),d=document.getElementById("seccodeForm");document.getElementById("seccodeImage");var e=!1,f=!1,c=!1,g,h;d.onsubmit=function(){c=!0};document.onkeydown=function(){f=!0;document.onkeydown=null};b.onfocus=function(){this.style.imeMode="disabled";a("seccodeFocus")};b.onblur=function(){a("seccodeBlur");e=!1};b.onkeyup=function(){e||(a("seccodeInput"),e=!0)};document.onmousemove=function(){a("mouse");f=!0;document.onmousemove=null};window.onbeforeunload=function(){c||(a("close_refresh"),setCookie("refresh","1",getUTCString(10),location.hostname,"/"))};g=setInterval(function(){var b;f&&-1<imgCode&&(b=imgCode?"imgSuccess":"imgError",a(b),clearInterval(g))},20);h=setInterval(function(){f&&(a("realIndex"),clearInterval(h))},20)})(sendLog)});
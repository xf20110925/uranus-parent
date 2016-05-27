   �         -http://weixin.sogou.com/pcindex/js/login.js?6     %gQ�;      %H ��              �      
     O K   
   Content-Type   application/x-javascript   Vary   Accept-Encoding   Content-Encoding   gzip   P3P   UCP="CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR"   Server   nginx   Date   Fri, 29 Jan 2016 02:48:42 GMT   Last-Modified   Thu, 28 Jan 2016 03:14:07 GMT   ETag   "56a9877f-1f7e"   Expires   Sun, 28 Feb 2016 02:48:42 GMT   Cache-Control   max-age=2592000 /**
 * 登录弹框
 * @param  {object} opt 
 * @author HuangBingcheng
 */

/*opt = {
    loginBtn: '#xxx',  登录按钮，可传字符串，DOM对象，jQuery对象
    parent: '#xxx',    登录框会插入到此元素下，默认body
    cbLink: 'http://xx.com',  登录后iframe跳转页面
    cbFn: fn;          点击登录按钮后要做的事情
}*/

function strcat(str,num){
    var offset=0;
    var len=str.length;
    
    if(len<=num)
        return str;
    
    for(var i=0;i<len;i++){
        
        if(str.charCodeAt(i)>256){
            offset+=1;
        }else{
            offset+=0.5;
        }
        
        if(Math.floor(offset)>=num&&i!=len-1){
            return str.substring(0,Math.floor(offset))+'...';
        }
    }
    
    return str;
}

function loginCallback(data){
    if(data&&data.status==0){
        if(!window.uid) {
            window.uid=data.puid;
        }
        if(data.username&&data.username!=''){
            $('#indx-login')[0].title=data.username;
            $('#indx-login')[0].innerHTML=['<span><img src="'+data.headurl+'" width="18" height="18"/>','</span>',strcat(decodeURIComponent(data.username),8)].join('');                   
        }else if(data.puid){
            $('#indx-login')[0].title=data.puid;
            $('#indx-login')[0].innerHTML=['<span><img src="'+data.headurl+'" width="18" height="18"/>','</span>',strcat(data.puid)].join('');
        }
        $('#top-rt').show();
    }
}

function login(opt) {
    if (!$(opt.loginBtn)) return;

    var loginBtn = opt.loginBtn,
        parent = $(opt.parent) || $('body'),
        cbLink = opt.cbLink || 'http://www.sogou.com/login/qq_login_callback_page.html',
        cbFn = opt.cbFn;

    
    $(loginBtn)[0].onclick=function(e){
        createLoginBox(parent, cbLink);

        if (cbFn) {
            cbFn();
        }
        //fix ie6 iframe 空白
        /*
        e=e||window.event;
        e.preventDefault&&e.preventDefault();
        e.returnValue=false;*/
        
        return false;
    }
}

var getstop = function(){return ((document.body && document.body.scrollTop) || (document.documentElement && document.documentElement.scrollTop) || 0);}

function createLoginBox(parent, cbLink) {
 
    if (!createLoginStyle) return;

    createLoginStyle();
    

    var qqLoginUrl = 'https://account.sogou.com/connect/login?provider=qq&client_id=2017&ru=' + encodeURIComponent(cbLink) + '&hun=0&oa=0',
        maskLayer = $('<div/>').addClass('login-skin'),
        loginBox = $('<div/>').addClass('login-pop'),
        iframe = $('<iframe/>').attr('src', qqLoginUrl).css({
            "width": 510,
            "height": 500,
            "border": 0
        }),
        close = $('<a/>').attr({
            "id": "loginCloseBtn",
            "href": "javascript:void(0)",
            "class": "del",
            "uigs":"loginCloseBtn"
        });

    loginBox.append(close);
    loginBox.append(iframe);

    parent.append(maskLayer);
    parent.append(loginBox);
    
    var top=getstop()+113;
    loginBox.css("top",top+'px');
    
    maskLayer.height($(document.body).height());
    
    if(window.isIE6){
        var width=$('body').width();
        
        if(width>=1000){
            maskLayer.width(width);    
        }else{
            maskLayer.width(1000);
        }
    }
    
    window.mask=true;

    close.bind('click.closebox', function() {
        maskLayer.remove();
        loginBox.remove();
        
        window.mask=false;
    });
}

function createLoginStyle() {
    if ($('#loginStyle').length > 0) return;

    var loginCSS = '.login-skin{position: absolute;top:0;left:0;min-width:1000px;width:100%;height: 100%;z-index: 2100;background-color: #000;opacity:0.4;filter:alpha(opacity=40);}'
            + '.login-pop{background-color: #fff;border: 1px solid #ebebeb;width: 510px;height: 500px;position: absolute;margin-left:-225px;left: 50%;font-family: Microsoft YaHei;z-index: 2200;}'
            + '.del{position: absolute;width: 20px;height: 20px;z-index: 4;top: 13px;right: 13px;display: block;background: url(/pcindex/images/skin/del.gif) no-repeat;_background: url(/pcindex/images/skin/del.gif) no-repeat;background-position: 0 0;}'
            + '.del:hover{background-position: -41px 0;}';

    var loginStyle = $('<style/>').attr({id: 'loginStyle', type: 'text/css'});

    $('body').append(loginStyle);

    if (loginStyle[0].styleSheet) { //for ie   
        loginStyle[0].styleSheet.cssText = loginCSS;
    } else { //for w3c   
        loginStyle[0].appendChild(document.createTextNode(loginCSS));
    }
}

$('#indx-login').click(function(event){
    if($('#login-pop')[0].style.display=='none')
        $('#login-pop')[0].style.display='';
    else
        $('#login-pop')[0].style.display='none';
        
    var e=event||window.event;
                    
    e.returnValue=false;
    e.preventDefault&&e.preventDefault();
    
    e.stopPropagation&&e.stopPropagation();
    e.cancelBubble =true;
            
    return false;
    
})

window.onresize=function(){
    if(window.mask&&window.isIE6){
        var maskLayer=$('.login-skin');
        
        var width=$('body').width();
        
        if(width>=1000){
            maskLayer.width(width);    
        }else{
            maskLayer.width(1000);
        }
    }
}

$('#quit').click(function(event){
        PassportSC.logoutHandle($("#pp-playground")[0] , function(){}, function(){
			window.location.reload();
		});
        
        var e=event||window.event;
        e.preventDefault&&e.preventDefault();
        e.returnValue=false;
        
        e.stopPropagation&&e.stopPropagation();
        e.cancelBubble =true;
        
        return false;
})

var passportnum=0;

(function(){
    var f=arguments.callee;
    passportnum++;
    
    setTimeout(function(){
        if(!window.PassportSC){
            if(passportnum>=10){
                window.hasLogin='1';
                $('#topmenu').show();
                return;   
            }else{
                f.apply();
            } 
        }else{
            PassportSC = PassportSC ||{};
            window.WEIXIN = {};
            
            WEIXIN.logout=function(that){
                PassportSC.logoutHandle($("#pp-playground")[0] , function(){}, function(){
                    window.location.reload();
                });
                
                event.preventDefault&&event.preventDefault();
                event.returnValue=false;
                
                return false;
            }
            
            PassportSC.appid = 2017;
            
            PassportSC.parsePassportCookie();
            
            if(PassportSC.cookie){                
                window.hasLogin='2';
                           
                window.uid=PassportSC.cookie.userid;

                if (cookieProcess("pcindex_sc")) {
                    var url = cookieProcess("pcindex_sc") + "&uid=" + window.uid;
                    $.ajax({
                        url : url,
                        dataType : 'jsonp',
                        async : true,
                        success : function() {
                            cookieProcess('pcindex_sc','delete', -100);
                        }
                    });
                }

                setTimeout(function() {
                    for(var i=0,len=docids.length;i<len;i++){
                        ajajNode([checklink,'callback=checkcallback&uid=',window.uid,'&doclist=',docids[i],'&from=web'].join(''), $('#ajaj-area-check')[0]);
                    }
                },30);

                ajajNode(loginCallbacklink);
                
            }else{
                window.hasLogin='1';
                $('#topmenu').show();
            }    
        }
    },100);
})();
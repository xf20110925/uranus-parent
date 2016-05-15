/**
 * Created by Administrator on 2015/11/13.
 */
var urlConf=function(){
    var domain = "http://127.0.0.1:8080";
    var domain = "";
    return {
        loginWinUrl:domain+'/api/wx/new/isLogin',
        listUrl:domain+'/wx/list'
    }
}();
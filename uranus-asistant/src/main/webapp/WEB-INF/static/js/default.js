(function () {
    var url = location.href;
    var data = {url: url};
    var phoneSchdularUrl = "http://192.168.21.103:8080/wx/mobile/getNewUrl";
    var getkeyurl = "http://192.168.21.103:8080/wx/readUrl";
    function requestUrl() {
        try {
            window._WXJS.ajax({
                url: phoneSchdularUrl,
                dataType: 'jsonp',
                data: {data: JSON.stringify(data), r: new Date().getTime() + Math.ceil(Math.random() * 1000000000)},
                type: 'GET',
                cache: false,
                success: function (result) {
                    try {
                        if (result.statusCode == 0) {
                            if (result.data.newUrl.indexOf("mp.weixin.qq.com/s") >= 0) {
                                result.data.newUrl = result.data.newUrl.replace("/s?", "/s?f=json&").replace("http://", "https://").replace("/mp/getmasssendmsg?", "/mp/getmasssendmsg?f=json&");
                            }
                            location.href = result.data.newUrl;
                        } else {
                            location.href = "http://m.sogou.com/js/common/zepto_modules/fx.min.js";
                        }
                    } catch (err) {
                        location.href = "http://m.sogou.com/js/common/zepto_modules/fx.min.js";
                    }
                },
                error: function (result) {
                    location.href = "http://m.sogou.com/js/common/zepto_modules/fx.min.js";
                }
            });
        } catch (err) {
            location.href = "http://m.sogou.com/js/common/zepto_modules/fx.min.js";
        }
    }

    try {
        if (url.indexOf("mp.weixin.qq.com/s") >= 0 && url.indexOf("f=json") >= 0 && JSON.parse(document.getElementsByTagName('body')[0].innerText).base_resp.ret == 0) {
            window._WXJS.ajax({
                url: getkeyurl,
                type: 'get',
                dataType: 'jsonp',
                data: {"url": url},
                success: function (result) {
                    alert(JSON.stringify(result));
                    try {
                        data.type = "rlnum";
                        data.readNum = result.readNum;
                        data.likeNum = result.likeNum;
                        setTimeout(function () {
                            requestUrl()
                        }, 500)
                    } catch (err) {
                        alert("cache");
                        location.href = "http://m.sogou.com/js/common/zepto_modules/fx.min.js";
                    }
                },
                error: function (result) {
                    alert("error");
                    location.href = "http://m.sogou.com/js/common/zepto_modules/fx.min.js";
                }
            });
        } else {
            setTimeout(function () {
                requestUrl()
            }, 500)

        }
    } catch (err) {
        setTimeout(function () {
            requestUrl()
        }, 8000)
    }
}())
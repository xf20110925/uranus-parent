(function () {
    var url = location.href;
    var data = {url: url};
    var phoneSchdularUrl = "http://101.200.213.147/wx/mobile/getNewUrl";

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
            var readUrl = url.replace("/mp/getmasssendmsg?", "/mp/getmasssendmsg?f=json&").replace("/s?", "/mp/getappmsgext?is_need_ad=0&is_need_reward=0&both_ad=1&reward_uin_count=0&").replace("http://", "https://");
            window._WXJS.ajax({
                url: readUrl,
                type: 'POST',
                dataType: 'json',
                data: {},
                success: function (result) {
                    try {
                        data.type = "rlnum";
                        data.readNum = result.appmsgstat.read_num;
                        if (result.appmsgstat.real_read_num > 0) {
                            data.readNum = result.appmsgstat.real_read_num;
                        }
                        data.likeNum = result.appmsgstat.like_num;
                        setTimeout(function () {
                            requestUrl()
                        }, 500)
                    } catch (err) {
                        location.href = "http://m.sogou.com/js/common/zepto_modules/fx.min.js";
                    }
                },
                error: function (result) {
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
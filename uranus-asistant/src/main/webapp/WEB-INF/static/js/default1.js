(function () {
    function requestUrl() {
        try {
            var url = location.href;
            var data = {url: url};
            if (url.indexOf("mp.weixin.qq.com/s") >= 0) {
                var readNum = document.getElementById("readNum3").innerText;
                var likeNum = document.getElementById("likeNum3").innerText;
                if (readNum == null || readNum == ""  || readNum < 0) {
                    location.href = "https://www.baidu.com/";
                }
                data.type = "rlnum";
                data.readNum = readNum;
                data.likeNum = likeNum;
            }

            window._WXJS.ajax({
                url: "http://101.200.213.147/wx/mobile/getNewUrl",
                dataType: 'jsonp',
                data: {data: JSON.stringify(data), r: new Date().getTime()+Math.ceil(Math.random()*1000000000)},
                type: 'GET',
                cache: false,
                success: function (result) {
                    if (result.statusCode == 0) {
                        location.href = result.data.newUrl;
                    } else {
                        location.href = "https://www.sogou.com/";
                    }
                },
                error: function (result) {
                    location.href = "https://www.sogou.com/";
                }
            });
        } catch (err) {
            location.href = "https://www.sogou.com/";
        }
    }

    setTimeout(function () {
        requestUrl()
    }, 500)
}())

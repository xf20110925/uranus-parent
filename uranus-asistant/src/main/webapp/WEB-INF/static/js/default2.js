(function () {
    var phoneSchdularUrl = "http://192.168.21.103:8080/wx/mobile/test";

    function requestUrl() {
        window._WXJS.ajax({
                              url: phoneSchdularUrl,
                              dataType: 'jsonp',
                              data: {},
                              type: 'GET',
                              cache: false,
                              success: function (result) {
                                  var wxUrl = result.url.replace("http://", "Https://")
                                  alert(wxUrl)
                                  generateWXKey(wxUrl)
                              },
                              error: function (result) {
                                  alert("error1");
                              }
                          });
    }

    function generateWXKey(readUrl) {
        window._WXJS.ajax({
                              url: readUrl,
                              type: 'GET',
                              success: function (result) {
                                  var keyUrl = location.href;
                                  alert("keyUrl" + keyUrl);
                              },
                              error: function (result) {
                                  alert("error2");
                              }
                          });
    }

    setTimeout(function () {
        requestUrl()
    }, 500)

}())
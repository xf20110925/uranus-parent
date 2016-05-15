var spiderService = function () {
    var errorfn = function (data, status, xhr, errorCB) {
        var data = {
            success: false,
            statusCode: XMLHttpRequest.status,
            message: status,
        }
        errorCB(data)
    }
    var successfn = function (data, successCB, errorCB) {
        if (data.success == true) {
            successCB(data)
        } else {
            errorCB(data)
        }
    }

    return {
        addSpider: function (data, successCB, errorCB) {
            $.ajax({
                    url: "/spider/add",
                    dataType: "json",
                    data: data,
                    type: 'post',
                    success: function (data) {
                        successfn(data, successCB, errorCB)
                    },
                    error: function (data, status, xhr) {
                        errorfn(data, status, xhr, errorCB)
                    }
                }
            )
        },
        delSpider: function (data, successCB, errorCB) {
            $.ajax({
                    url: "/spider/add",
                    dataType: "json",
                    data: data,
                    type: 'post',
                    success: function (data) {
                        successfn(data, successCB, errorCB)
                    },
                    error: function (data, status, xhr) {
                        errorfn(data, status, xhr, errorCB)
                    }
                }
            )
        },
        getClassField: function (data, successCB, errorCB) {
            $.ajax({
                    url: "/spider/action/get",
                    dataType: "json",
                    data: data,
                    type: 'get',
                    success: function (data) {
                        successfn(data, successCB, errorCB)
                    },
                    error: function (data, status, xhr) {
                        errorfn(data, status, xhr, errorCB)
                    }
                }
            )
        },
        addActionSet: function (data, successCB, errorCB) {
            $.ajax({
                    url: "/spider/action/add",
                    dataType: "json",
                    data: data,
                    type: 'post',
                    success: function (data) {
                        successfn(data, successCB, errorCB)
                    },
                    error: function (data, status, xhr) {
                        errorfn(data, status, xhr, errorCB)
                    }
                }
            )
        },
        updateActionSet: function (data, successCB, errorCB) {
            $.ajax({
                    url: "/spider/update",
                    dataType: "json",
                    contentType: "application/json; charset=utf-8",
                    data: data,
                    type: 'post',
                    success: function (data) {
                        successfn(data, successCB, errorCB)
                    },
                    error: function (data, status, xhr) {
                        errorfn(data, status, xhr, errorCB)
                    }
                }
            )
        },
    }
}()


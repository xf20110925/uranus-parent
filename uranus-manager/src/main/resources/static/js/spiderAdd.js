$(function () {
    $(".addSubmit").on("click", function () {
        var data = $("form").serialize();
        spiderService.addSpider(data, function () {
            alert("添加成功")
            location.href="/spider/list"
        }, function () {
            alert("添加失败")
        })
    });
})
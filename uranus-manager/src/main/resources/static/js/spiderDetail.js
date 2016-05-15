$(function () {
    var referModal = function () {
        /*        $('#addActionFields')[0].reset();*/
        var gettpl = document.getElementById('demo').innerHTML.replace("<!--  ", "").replace("-->", "")


        var params = {className: $('#actionClass').find('option:selected').val()}
        spiderService.getClassField($.param(params), function (data) {
            var data = {list: data.data}
            laytpl(gettpl).render(data, function (html) {
                document.getElementById('addActionFields').innerHTML = html;
                $("#addActionFields input#className").val($('#actionClass').find('option:selected').val())
            });

        }, function (data) {
            alert(data.message + ":请重新选择类型")
        })
    };

    $('#btnOpenModal').bind("click", referModal);
    $('#actionClass').bind("change", referModal);
    $('#btnAddAction').bind('click', function () {
        var oid = $("#id").val();

        var param = $("#addActionFields").serialize() + "&oid=" + oid;
        spiderService.addActionSet(param, function () {
            alert("添加成功")
            location.reload()
        }, function (data) {
            alert(data.message + "|添加失败")
        })
    })
    $(".panel-group .panel .btnDelAction").on('click', function () {
       $(this).parent().parent().parent().parent().parent().remove();
    })

    $('#btnSave').bind('click', function () {
        var id = $("#id").val()
        var name = $('#name').val()
        var crawleType = $("#crawleType").val();
        var urlRegex = $("#urlRegex").val();
        var actions = $('.panel-body').map(function (index, data) {
            var result = {}
            $(data).find(":input").map(function () {
                    result[$(this).attr("name")] = $(this).val();
                }
            )
            return result;
        }).get()

        data = {
            id: id,
            name: name,
            crawleType: crawleType,
            urlRegex:urlRegex,
            actions: actions
        }
        spiderService.updateActionSet(JSON.stringify(data), function () {
            alert("更新成功")
            location.reload();
        }, function (data) {
            alert("失败,请重试")
        })


    })
})
/**
 * 控制光标
 */
// @Bind view.onReady
!function() {
    var obj = document.getElementById("base_expression");
    $("textarea#base_expression").bind("mousedown",function(){
        view.setPos();
    });
    $("textarea#base_expression").bind("keydown",function(){
        view.setPos();
    });
    $("textarea#base_expression").bind("keyup",function(){
        view.setPos();
    });
    $("textarea#base_expression").bind("mouseup",function(){
        view.setPos();
    });
    $("textarea#base_expression").bind("focus",function(){
        view.setPos();
    });

    var start = 0;//开始位置
    var end = 0;//结束位置
    view.setCurPos = function (pos) {
        if (obj.createTextRange) {
            var r = obj.createTextRange();
            var objValue = r.value;
            r.moveStart("character", pos);
            r.collapse();
            r.select();
        }
    };
    //清空value
    view.clareValue = function () {
    	obj.value = "";
    	this.setCurPos(0);
    }
   //获得value
    view.getDataValue = function () {
    	return obj.value ;
    }
    //插入value
    view.insertValue = function (value) {
        var pre = obj.value.substr(0, start);
        var post = obj.value.substr(end);
        obj.value = pre + value + post;
        var a = start;
        var vLen1 = value.length;
        var vLen2 = value.lastIndexOf("()");
        if (vLen2 != -1) {
            if (vLen1 - vLen2 == 2) {
                start += value.length - 1;
                end += value.length;
            }
        }
        else {
            start += value.length;
            end += value.length;
        }
        this.setCurPos(start)
    };

    view.setPos = function () {
        //如果是Firefox(1.5)的话，方法很简单\
        if (typeof(obj.selectionStart) == "number") {
            start = obj.selectionStart;
            end = obj.selectionEnd;
        }
        //下面是IE(6.0)的方法，麻烦得很，还要计算上'\n'
        else if (document.selection) {
            var range = document.selection.createRange();
            if (range.parentElement().id == obj.id) {
                var range_all = document.body.createTextRange();
                range_all.moveToElementText(obj);
                for (start = 0; range_all.compareEndPoints("StartToStart", range) < 0; start++)
                    range_all.moveStart('character', 1);
                for (var i = 0; i <= start; i++) {
                    if (obj.value.charAt(i) == '\n')
                        start++;
                }
                var range_all = document.body.createTextRange();
                range_all.moveToElementText(obj);
                for (end = 0; range_all.compareEndPoints('StartToEnd', range) < 0; end++)
                    range_all.moveStart('character', 1);
                for (var i = 0; i <= end; i++) {
                    if (obj.value.charAt(i) == '\n')
                        end++;
                }
            }
        }
    }
};













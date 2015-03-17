// @Bind @QuerySchema.onCurrentChange
!function (dsQuerySchemas, criteriaBuilderSysUserViewCriteria) {
    var criteria = null, schema = dsQuerySchemas.getData("#"), criteriaString = schema.get("criteria");
    if (criteriaString) {
        criteria = dorado.JSON.parse(criteriaString);
    }
    criteriaBuilderSysUserViewCriteria.set("criteria", criteria);
    var name = schema.get("name");
    view.set({
        "^needSchema.disabled": !name,
        "#editorSchema.text": name
    });
}

// @Bind #buttonQuery.onClick
!function (dsSysUserViewCriterias, criteriaBuilderSysUserViewCriteria) {
    dsSysUserViewCriterias.set("parameter", criteriaBuilderSysUserViewCriteria.get("criteria")).flushAsync();
}

// @Bind #buttonSave.onClick
!function(dsQuerySchemas, criteriaBuilderSysUserViewCriteria,saveQuerySchemasAction) {
    var name = dsQuerySchemas.getData("#.name");
    if (!name) {
        saveAs(dsQuerySchemas, criteriaBuilderSysUserViewCriteria,saveQuerySchemasAction)
    } else {
        dorado.MessageBox.confirm("确认要覆盖当前的查询方案[" + name + "]吗？", function () {
            var criteria = criteriaBuilderSysUserViewCriteria.get("criteria");
            dsQuerySchemas.getData("#").set("criteria",dorado.JSON.stringify(criteria));
            saveQuerySchemasAction.set('parameter',{'viewPath':dsQuerySchemas.get('parameter').get('viewPath')});
            saveQuerySchemasAction.execute();
        });
    }
}

// @Bind #buttonSaveAs.onClick
!function(dsQuerySchemas, criteriaBuilderSysUserViewCriteria, saveQuerySchemasAction) {
    dorado.MessageBox.prompt("请输入新查询方案的名称", function (text) {
        if (text) {
            var save = true;
            dsQuerySchemas.getData().each(function (schema) {
                if (text == schema.get("name")) {
                    dorado.MessageBox.alert(("查询方案[" + text + "]已经存在！"));
                    save = false;
                    return false;
                }
            });

            if (save) {
                var criteria = criteriaBuilderSysUserViewCriteria.get("criteria");
                dsQuerySchemas.insert({
                    name: text,
                    criteria: dorado.JSON.stringify(criteria)
                });
                saveQuerySchemasAction.set('parameter',{'viewPath':dsQuerySchemas.get('parameter').get('viewPath')});
                saveQuerySchemasAction.execute();
            }
        }
    });
}

// @Bind #buttonDelete.onClick
!function (dsQuerySchemas, saveQuerySchemasAction) {
    var schema = dsQuerySchemas.getData("#"), name = schema.get("name");
    if (name) {
        dorado.MessageBox.confirm("确认要删除当前的查询方案[" + name + "]吗？", function () {
            schema.remove();
            saveQuerySchemasAction.set('parameter',{'viewPath':dsQuerySchemas.get('parameter').get('viewPath')});
            saveQuerySchemasAction.execute();
        });
    }
}

// @Bind #buttonClear.onClick
!function (criteriaBuilderSysUserViewCriteria) {
    criteriaBuilderSysUserViewCriteria.set("criteria", null);
}


//@Bind #lookButton.onClick
!function (dsSysUserViewCriterias, gridSysUserViewCriterias, lookSysUserViewCriteriaDialog) {
    var selection = gridSysUserViewCriterias.get("selection");
    if (selection.length == 0) {
        dorado.MessageBox.alert("请选择记录！");
    } else if (selection.length > 1) {
        dorado.MessageBox.alert("只能查看一条记录！");
    } else if (selection.length == 1) {
        dsSysUserViewCriterias.getData().setCurrent(selection[0]);
        dsSysUserViewCriterias.set("readOnly", true);
        lookSysUserViewCriteriaDialog.show();
    }
}

//@Bind #addButton.onClick
!function (dsSysUserViewCriterias, addDialogSysUserViewCriteria) {
    dsSysUserViewCriterias.set("readOnly", false);
    dsSysUserViewCriterias.insert();
    addDialogSysUserViewCriteria.show();
}

//@Bind #addEnter.onClick
!function (saveSysUserViewCriteriasAction, addDialogSysUserViewCriteria) {
    saveSysUserViewCriteriasAction.execute(function () {
        addDialogSysUserViewCriteria.hide();
    });
}

// @Bind #addCancel.onClick 
// @Bind #addDialogSysUserViewCriteria.onClose
!function (dsSysUserViewCriterias, addDialogSysUserViewCriteria) {
    dsSysUserViewCriterias.getData().cancel();
    addDialogSysUserViewCriteria.hide();
}

// @Bind #updateButton.onClick
!function (dsSysUserViewCriterias, gridSysUserViewCriterias, dialogSysUserViewCriteria, checkPermissionAction) {
    var selection = gridSysUserViewCriterias.get("selection");
    if (selection.length == 0) {
        dorado.MessageBox.alert("请选择记录！");
    } else if (selection.length > 1) {
        dorado.MessageBox.alert("只能选择一条记录修改！");
    } else if (selection.length == 1) {
        var entityClass = dsSysUserViewCriterias.get("userData")[0].entityClass;
        var idValue = dsSysUserViewCriterias.getData("#.id");
        var idValueArray = new Array();
        idValueArray[0] = idValue;
        checkPermissionAction.set("parameter", {
            entityClass: entityClass,
            idValues: idValueArray,
            operationType: '2'
        });
        checkPermissionAction.execute(function (result) {
            if (result.length > 0) {
                dorado.MessageBox.alert("不能进行此操作，您无选中记录的修改权限！");
                return;
            }
            dsSysUserViewCriterias.set("readOnly", false);
            dsSysUserViewCriterias.getData().setCurrent(selection[0]);
            dialogSysUserViewCriteria.show();
        });
    }
}

// @Bind #updateEnter.onClick
!function (saveSysUserViewCriteriasAction, dialogSysUserViewCriteria) {
    saveSysUserViewCriteriasAction.execute(function () {
        dialogSysUserViewCriteria.hide();
    });
}

// @Bind #updateCancel.onClick
// @Bind #dialogSysUserViewCriteria.onClose
!function (dsSysUserViewCriterias, dialogSysUserViewCriteria) {
    dsSysUserViewCriterias.getData().cancel();
    dialogSysUserViewCriteria.hide();
}

// @Bind #delButton.onClick
!function(dsSysUserViewCriterias,gridSysUserViewCriterias,saveSysUserViewCriteriasAction,checkPermissionAction){
    var entitys = dsSysUserViewCriterias.getData();
    var selection = gridSysUserViewCriterias.get("selection");
    if(selection.length==0){
        dorado.MessageBox.alert("请选择记录！");
    }else if (selection.length != 0) {
        var entityClass = dsSysUserViewCriterias.get("userData")[0].entityClass;
        var idValue = dsSysUserViewCriterias.getData("#.id");
        var idValueArray = new Array();
        jQuery.each(selection,function(i,item){
            idValueArray[i] = item.get("id");
        });
        checkPermissionAction.set("parameter",{
            entityClass:entityClass,
            idValues:idValueArray,
            operationType:'3'
        });

        checkPermissionAction.execute(function(result){
            if(result.length>0){
                var idValueFilter = new Object();
                result.each(function(it){
                    idValueFilter[it] = true;
                });
                dorado.MessageBox.confirm("确定删除？", function(){
                    var haveRemove = false;
                    jQuery.each(selection,function(i,item){
                        if(!idValueFilter[item.get('id')]){
                            haveRemove = true;
                            entitys.remove(item);
                        }
                    });
                    if(haveRemove){
                        saveSysUserViewCriteriasAction.execute();
                    }
                    dorado.MessageBox.alert("您无选中记录的删除权限！");
                    return;
                });
            }else{
                dorado.MessageBox.confirm("确定删除？", function(){
                    jQuery.each(selection,function(i,item){
                        entitys.remove(item);
                    });
                    saveSysUserViewCriteriasAction.execute();
                });
            }
        });
    }
}

// @Bind #exportButton.onClick
!function(){
	var r=new bdf.Report();
	var config={
	        labelAlign:2,//表单标题对齐方式，0表示左对齐，1表示居中对齐，2表示右对齐，默认值为2
	        dataAlign:0,//与表单标题对齐方式含义相同
	        dataStyle:0,//字体样式，0表示NORMAL，1表示BOLD，2表示ITALIC，3表示UNDERLINE，默认值为0
	        showPageNo:true,//是否显示页码
	        showBorder:true,//是否显示表单边框线
	        showTitle:false,//是否显示表单标题
	        title:"",//表单标题内容，只有在showTitle属性为true时起作用
	        fontSize:16,//标题字体大小，只有在showTitle属性为true时起作用
	        fontColor:"0,0,0",//标题字体颜色，RGB色值，只有在showTitle属性为true时起作用
	        bgColor:"255,255,255"//标题背景色，RGB色值，只有在showTitle属性为true时起作用
	};
	r.generateExcelGridReport(this,gridSysUserViewCriterias,config,"serverAll",60000,null);
}

// @Bind #helpButton.onClick
!function(helpIntroSysUserViewCriteria){
	helpIntroSysUserViewCriteria.start();
}

// @Bind #helpIntroSysUserViewCriteria.onReady
!function(self){
	setTimeout(function(){
		var steps = [{
			element : 'criteriaBuilderSysUserViewCriteria',
		  	intro : "<h4 style='color:orangered'>查询方案设计区域</h4>"+
					"<p>您可根据需要，在此区域自行设计查询方案：</p>" +
					"<ul>" +
					"<li><span style='font-weight:bold;'>条件：</span>筛选数据的表达式。举例：商户名称 等于 张三，解释：显示商户名称为张三的数据。</li>"+
					"<li><span style='font-weight:bold;'>联合条件：</span>条件的逻辑运算符。举例:商户名称 等于 张三 并且 建立日期 等于 2014-01-01，解释：显示商户名称为张三，建立日期为2014-01-01的数据，其它数据不显示。</li>"+
					"<li><span style='font-weight:bold;'>并且：</span>举例：商户名称 等于 张三 并且 建立时间=2014-01-01,显示满足两个条件的数据，满足其中一个条件时不显示。</li>"+
					"<li><span style='font-weight:bold;'>或者：</span>举例：商户名称 等于 张三 或者 建立时间=2014-01-01,只要满足两个条件中任意一个即显示。</li>" +
					"<li><span style='font-weight:bold;'>排序：</span>数据排序，升序a-z,倒序z-a</li></ul>",
		  	name:'step1'
		},{
			element : 'editorSchema',
		  	intro : "<h4 style='color:orangered'>查询方案选择框</h4>"+
					"<p>可选择保存过的查询方案.</p>",
		  	name:'step2'
		},{
			element : 'buttonQuery',
			intro : "<h4 style='color:orangered'>查询按钮</h4>"+
					"<p>单击按钮，可实现数据筛选查询.</p>",
		  	name:'step3'
		},{
			element : 'buttonClear',
			intro : "<h4 style='color:orangered'>重置按钮</h4>"+
					"<p>清空条件设计区域内容</p>",
		  	name:'step4'
		},{
			
		},{
			element : 'buttonSave',
		  	intro : "<h4 style='color:orangered'>查询方案保存按钮</h4>"+
					"<p>可将设计好的查询方案保存，以便下次使用。</p>",
		  	name:'step5'	
		},{
		  	element : 'buttonSaveAs',
			intro : "<h4 style='color:orangered'>查询方案另存按钮</h4>"+
					"<p>将设计好的查询方案另存为一个新的方案.</p>",
		  	name:'step6'		
		},{
		  	element : 'buttonDelete',
			intro : "<h4 style='color:orangered'>删除查询方案按钮</h4>"+
					"<p>将不需的查询方案删除。</p>",
		  	name:'step7'		
		},{
		  	element : 'page',
		  	intro : "<h4 style='color:orangered'>分页工具类</h4>"+
			"<p>您可以通过点击箭头或在输入框中页数敲击键盘回车，实现数据翻页。</p>",
		  	name:'step7'		
		},{
		  	element : 'gridSysUserViewCriterias',
		  	intro : "<h4 style='color:orangered'>查询结果显示区域</h4>"+
			"<p>您可以根据需要，单击表格头，自定义显示列，在导出数据和下次显示时会根据您选择导出、显示数据。</p>",
		  	name:'step7'		
		}];
		self.set('steps',steps);
	},500);
}

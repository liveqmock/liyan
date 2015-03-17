//@Bind #addOrgBtnUpd.onClick
//@Bind #addDateUpd.onClick
//@Bind #addBizUpd.onClick
//@Bind #addProductUpd.onClick
//@Bind #addPostUpd.onClick
//@Bind #addOrgBtnAdd.onClick
//@Bind #addDateAdd.onClick
//@Bind #addBizAdd.onClick
//@Bind #addProductAdd.onClick
//@Bind #addPostAdd.onClick
!function(self, arg) {	
	var data;
	var dataGrid;
	try{
		dataGrid = view.get('#dataTreeGridDimenDataRestrictU');
		data =  dataGrid.getCurrentItem().get('data');
	}catch(e){
		dataGrid = view.get('#dataTreeGridDimenDataRestrictA');
		data =  dataGrid.getCurrentItem().get('data');
	}

	var restrictType = data.get('restrictType');
	if(restrictType=='3'){
		if(!data.get('dimenId')){
			dorado.MessageBox.alert("请先选择约束条件维度!");
			return;
		}
		if(!data.get('fieldCnName')){
			dorado.MessageBox.alert("请先选择约束约束条件列名!");
			return;
		}
		var dimenType = data.get('dimenType');
		if(dimenType=="1"){//机构
			view.get("#dataSetOrg").set("parameter", {"parentOrgId":"0"}).flushAsync(function(){
				view.get("#addOrgDialog").show();
			});
		}else if(dimenType=="2"){//日期
			view.get("#dataSetDateRange").clear();
			view.get("#dataSetDateRange").insert({});
			view.get("#addDateDialog").show();
		}else if(dimenType=="3"){//业务条线
			view.get("#dataSetBizLine").set("parameter",{parentBusilineId: "0"}).flushAsync(function(){
				view.get("#addBizLineDialog").show();
			});
		}else if(dimenType=="4"){//产品
			view.get("#dataSetProduct").set("parameter",{parentId: "0"}).flushAsync(function(){
				view.get("#addProductDialog").show();
			});
		}else if(dimenType=="8"){
			view.get("#postDataSet").flushAsync(function(){
				view.get("#addPostDialog").show();
			});
		}
	}else{
		dorado.MessageBox.alert("联合约束不能设置授权范围!");
	}
}

//@Bind #addPostNullBtnUpd.onClick
//@Bind #addBizlineNullBtnUpd.onClick
//@Bind #addProductNullBtnUpd.onClick
//@Bind #addDateNullBtnUpd.onClick
//@Bind #addOrgNullBtnUpd.onClick
//@Bind #addPostNullBtnAdd.onClick
//@Bind #addBizlineNullBtnAdd.onClick
//@Bind #addProductNullBtnAdd.onClick
//@Bind #addDateNullBtnAdd.onClick
//@Bind #addOrgNullBtnAdd.onClick
!function(self,arg){
	var data;
	var dataGrid;
	try{
		dataGrid = view.get('#dataTreeGridDimenDataRestrictU');
		data =  dataGrid.getCurrentItem().get('data');
	}catch(e){
		dataGrid = view.get('#dataTreeGridDimenDataRestrictA');
		data =  dataGrid.getCurrentItem().get('data');
	}
	var restrictType = data.get('restrictType');
	if(restrictType=='3'){
		if(!data.get('dimenId')){
			dorado.MessageBox.alert("请先选择约束条件维度!");
			return;
		}
		if(!data.get('fieldCnName')){
			dorado.MessageBox.alert("请先选择约束约束条件列名!");
			return;
		}
		var datas=view.get("#dataSetRestrict").getData("!CURRENT_RESTRICT.dimenData");
		if(datas.entityCount>0){
			dorado.MessageBox.alert("请先删除授权值再进行此操作！");
		}else{
			var dimenDataList=view.get("#dataSetRestrict").getData("!CURRENT_RESTRICT.dimenData");
			var fieldName=view.get("#dataSetDimen").getData("#.#fields").get("fieldName");
			var entity = {dimenValue:"IS NULL",dispName:"为空值",fieldName:fieldName}
			dimenDataList.insert(entity);
		}
	}else{
		dorado.MessageBox.alert("联合约束不能设置授权范围!");
	}
}

//@Bind #addPostNotNullBtnUpd.onClick
//@Bind #addBizlineNotNullBtnUpd.onClick
//@Bind #addProductNotNullBtnUpd.onClick
//@Bind #addDateNotNullBtnUpd.onClick
//@Bind #addOrgNotNullBtnUpd.onClick
//@Bind #addPostNotNullBtnAdd.onClick
//@Bind #addBizlineNotNullBtnAdd.onClick
//@Bind #addProductNotNullBtnAdd.onClick
//@Bind #addDateNotNullBtnAdd.onClick
//@Bind #addOrgNotNullBtnAdd.onClick
!function(self,arg){
	var data;
	var dataGrid;
	try{
		dataGrid = view.get('#dataTreeGridDimenDataRestrictU');
		data =  dataGrid.getCurrentItem().get('data');
	}catch(e){
		dataGrid = view.get('#dataTreeGridDimenDataRestrictA');
		data =  dataGrid.getCurrentItem().get('data');
	}
	var restrictType = data.get('restrictType');
	if(restrictType=='3'){
		if(!data.get('dimenId')){
			dorado.MessageBox.alert("请先选择约束条件维度!");
			return;
		}
		if(!data.get('fieldCnName')){
			dorado.MessageBox.alert("请先选择约束约束条件列名!");
			return;
		}
		var datas=view.get("#dataSetRestrict").getData("!CURRENT_RESTRICT.dimenData");
		if(datas.entityCount>0){
			dorado.MessageBox.alert("请先删除授权值再进行此操作！");
		}else{
			var dimenDataList=view.get("#dataSetRestrict").getData("!CURRENT_RESTRICT.dimenData");
			var fieldName=view.get("#dataSetDimen").getData("#.#fields").get("fieldName");
			var entity = {dimenValue:"IS NOT NULL",dispName:"不为空值",fieldName:fieldName}
			dimenDataList.insert(entity);
		}
	}else{
		dorado.MessageBox.alert("联合约束不能设置授权范围!");
	}
}




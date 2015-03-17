// @Bind #querydataSetMdDataTitle.onReady
!function(self, arg) {
self.insert({});
}

// @Bind #querySeach.onClick
!function(self, arg) {
	
view.get("#dataSetMdDataTitle").set("parameter", view.get("#querydataSetMdDataTitle").getData("#")).flushAsync();
}

// @Bind #queryReset.onClick
!function(self, arg) {
view.set("#querydataSetMdDataTitle.data",{});

}

//@Bind #addButton.onClick
!function(self, arg) {	
	var node=view.get("#dataTreeGridDataTitle").get("currentNode");
	if(node==undefined){
		view.get("#dataSetMdDataTitle").getData().insert({"parentId":"0","status":"1"});
		view.get("#dataSetMdDataTitle").set("readOnly",false);
	}else{
		var currEntity=node.get("data");
		var entity=currEntity.createBrother({parentId:currEntity.get("parentId"),"status":"1"});
		view.get("#dataTreeGridDataTitle").set("currentEntity",entity);
	}
	
	view.get("#addMdDataTitleDialog").show();	
}


// @Bind #addChildButton.onClick
!function(self, arg) {	
	var ds = view.get("#dataSetMdDataTitleTableMapping");
	var currentNode = view.get("#dataTreeGridDataTitle").get("currentNode");
	if (!currentNode) {
		throw new dorado.Exception("请先选择一个节点！");
	}
	var currentEntity = currentNode.get("data");
		var child = currentEntity.createChild("children", {
			parentId:currentEntity.get("id"),
			status:"1"
		});
		currentNode.expand();
		view.get("#dataTreeGridDataTitle").set("currentEntity",child);
		entityForTitle = view.get("#dataTreeGridDataTitle").get("currentNode").get("data").get("id");
		if(entityForTitle==undefined){
			entityForTitle = "undefined";
		}
		var parm={dataTitleId:entityForTitle}
		ds.set("parameter", parm).flushAsync();
	view.get("#addMdDataTitleDialog").show();
}

// @Bind #updateButton.onClick
!function(self, arg) {
	var ds = view.get("#dataSetMdDataTitleTableMapping");
	var currentNode = view.get("#dataTreeGridDataTitle").get("currentNode");
	if (!currentNode) {
		throw new dorado.Exception("请先选择一个节点！");
	}
	entityForTitle = view.get("#dataTreeGridDataTitle").get("currentNode").get("data").get("id");
	if(entityForTitle==undefined){
		entityForTitle = "undefined";
	}
	var parm={dataTitleId:entityForTitle}
	ds.set("parameter", parm).flushAsync()
	view.get("#updateMdDataTitleDialog").show();      
}

// @Bind #delButton.onClick
!function(self, arg) {
	var entity = view.get("#dataTreeGridDataTitle").get("currentEntity");
	var action = view.get("#saveMdDataTitlesAction");
	if(entity){
		if(!entity.get("children").isEmpty()){
			dorado.MessageBox.alert("当前节点有下级子节点不能删除！");
		}else{
			dorado.MessageBox.confirm("是否删除?",function(){
				entity.remove();
				action.execute(function(){
					action.set("successMessage","操作成功！")
				});
			});
		}
	}else{
		dorado.MessageBox.alert("没有可以停用的数据！");
	}
}

// @Bind #lookButton.onClick
!function(self, arg) {
	var ds = view.get("#dataSetMdDataTitleTableMapping");
	var currentNode = view.get("#dataTreeGridDataTitle").get("currentNode");
	if (!currentNode) {
		throw new dorado.Exception("请先选择一个节点！");
	}
	entityForTitle = view.get("#dataTreeGridDataTitle").get("currentNode").get("data").get("id");
	if(entityForTitle==undefined){
		entityForTitle = "undefined";
	}
	var parm={dataTitleId:entityForTitle}
	ds.set("parameter", parm).flushAsync();
	view.get("#lookMdDataTitleDialog").show();  
}

// @Bind #updateMdDataTitleDialog.onClose
!function(self, arg) {
view.get("#dataSetMdDataTitle").getData().cancel();
view.get("#updateMdDataTitleDialog").hide();
}

// @Bind #updateCommit.onClick
!function(self, arg) {
	var action = view.get("#saveMdDataTitlesAction");
	var actionForMapping = view.get("#saveMdDataTitleTableMappingsAction");
	var dialog = view.get("#updateMdDataTitleDialog");
	if(view.get("#dataSetMdDataTitle").getData("#").state == 0){
		var entityForTitle = view.get("#dataTreeGridDataTitle").get("currentNode").get("data").get("id");
		var entityForMappingList = view.get("#dataSetMdDataTitleTableMapping").getData();
		entityForMappingList.each(function(entity){
			if(entity.get("dataTitleId")==""){
				entity.set("dataTitleId",entityForTitle)
			}
		});
		actionForMapping.execute(function(){
			dialog.hide();
		});
	}else{
		action.execute(function(){
			var entityForTitle = view.get("#dataTreeGridDataTitle").get("currentNode").get("data").get("id");
			var entityForMappingList = view.get("#dataSetMdDataTitleTableMapping").getData();
			entityForMappingList.each(function(entity){
				if(entity.get("dataTitleId")==""){
					entity.set("dataTitleId",entityForTitle)
				}
			});
			actionForMapping.execute();
			dialog.hide();
		});
	}
}

// @Bind #updateCancel.onClick
!function(self, arg) {
	view.get("#dataSetMdDataTitle").getData("!CURRENT_DATATITLE").cancel();
	view.get("#dataSetMdDataTitleTableMapping").getData().cancel();
	view.get("#updateMdDataTitleDialog").hide();		  
}

// @Bind #addMdDataTitleDialog.onClose
!function(self, arg) {
	view.get("#dataSetMdDataTitle").getData("!CURRENT_DATATITLE").cancel();
	view.get("#addMdDataTitleDialog").hide();
}

// @Bind #addCommit.onClick
!function(self, arg) {
	var action = view.get("#saveMdDataTitlesAction");
	var actionForMapping = view.get("#saveMdDataTitleTableMappingsAction");
	var dialog = view.get("#addMdDataTitleDialog");
	action.execute(function(){
		var entityForTitle = view.get("#dataTreeGridDataTitle").get("currentNode").get("data").get("id");
		var entityForMappingList = view.get("#dataSetMdDataTitleTableMapping").getData();
		entityForMappingList.each(function(entity){
			if(entity.get("dataTitleId")==""){
				entity.set("dataTitleId",entityForTitle)
			}
		});
		actionForMapping.execute();
		dialog.hide();
	});
	
}

// @Bind #addCancel.onClick
!function(self, arg) {
	try{
		view.get("#dataSetMdDataTitle").getData("!CURRENT_DATATITLE").cancel();
		view.get("#dataSetMdDataTitleTableMapping").getData().cancel();
	}catch(e){
		
	}finally{
		view.get("#addMdDataTitleDialog").hide();	
	}		  
}

// @Bind #lookMdDataTitleDialog.onClose
!function(self, arg) {
view.get("#lookMdDataTitleDialog").hide();     
}

// @Bind #dataGridMdDataTitle.#dataTitleCode.onRenderCell
!function(self,arg){
	jQuery(arg.dom).empty();
	jQuery(arg.dom).xCreate({
	    tagName: "A",
	    href: "#",
	    content: arg.data.get("dataTitleCode"),
	    onclick: function() {
	        var dataSet = view.get("#dataSetMdDataTitle");
			var dialog = view.get("#lookMdDataTitleDialog");
			var entitys = dataSet.getData("#");
				dialog.show();
				dataSet.set("readOnly",true);
	    }
	});
}
// @Bind #lookClose.onClick
!function(self, arg) {
view.get("#lookMdDataTitleDialog").hide();
}


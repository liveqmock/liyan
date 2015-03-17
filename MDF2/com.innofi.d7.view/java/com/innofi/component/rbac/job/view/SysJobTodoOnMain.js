var linkCellRcaRenderer = $extend(dorado.widget.grid.SubControlCellRenderer,
		{
			createSubControl : function(arg) {
				if (arg.data.rowType)
					return null;

				return new dorado.widget.Link({
					toggleable : true,
					onClick : function(self) {
						var entity = arg.data;
						try{
							parent.parent.showJobTodo(entity);
						}catch(e){
							parent.showJobTodo(entity);
						}
					}
				});
			},
			refreshSubControl: function(link, arg) {
				var sum = arg.data.get("jobCount");

				link.set({
					text:sum,
					href:"#"
				});
			}
		});

//@Bind #dataGridSysJobTodo.onCreate
!function(self) {
	self.set("&jobCount.renderer", new linkCellRcaRenderer());
}

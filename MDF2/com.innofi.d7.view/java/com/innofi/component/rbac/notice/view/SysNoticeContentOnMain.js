var linkCellRcaRenderer = $extend(dorado.widget.grid.SubControlCellRenderer,
		{
			createSubControl : function(arg) {
				if (arg.data.rowType)
					return null;

				return new dorado.widget.Link({
					toggleable : true,
					onClick : function(self) {
						var entity = arg.data;
						parent.showNotice(entity.get("id"));
					}
				});
			},
			refreshSubControl: function(link, arg) {
				var sum = arg.data.get("title");

				link.set({
					text:sum,
					href:"#"
				});
			}
		});

//@Bind #dataGridSysNoticeContent.onCreate
!function(self) {
	self.set("&title.renderer", new linkCellRcaRenderer());
}

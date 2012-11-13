Dashboard = {
		escapeId : function(id) {
		    return "#" + id.replace(/:/g,"\\:");
		},
		
		getWidgets : function(id) {
			 
			var stringDiv = "{";
			$("#" + id.replace(/:/g,"\\:")).children(".column").each(function(colPosition) {
				var column = $(this);
				if (stringDiv.length > 1) {
					stringDiv += ",";
				}
				stringDiv += '"' + column.attr("data-layout-id") + '":{';
				var first = true;
				$($(this)).children(".widgetbox").each(function(widgetPosition) {
					var widget = $(this);
					if (!first) {
						stringDiv += ","
					}
					stringDiv +=  '"' + widget.attr("data-instance-id") + '":' + widgetPosition  ;
					first = false;
				});
				stringDiv += "}";
			});
			stringDiv += "}";
			
			return stringDiv;
		}, 
}
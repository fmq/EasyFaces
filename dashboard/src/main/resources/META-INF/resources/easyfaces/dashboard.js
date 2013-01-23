Dashboard = {
		escapeId : function(id) {
		    return "#" + id.replace(/:/g,"\\:");
		},
		
		getWidgets : function(id) {
			 
			var stringDiv = "{";
			jQuery("#" + id.replace(/:/g,"\\:")).children(".column").each(function(colPosition) {
				var column = jQuery(this);
				if (stringDiv.length > 1) {
					stringDiv += ",";
				}
				stringDiv += '"' + column.attr("data-layout-id") + '":{';
				var first = true;
				jQuery(jQuery(this)).children(".widgetbox").each(function(widgetPosition) {
					var widget = jQuery(this);
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
Widgets = {
	initDashboard : function(id) {
		jQuery(".column").sortable(
				{
					connectWith : ".column",
					handle : '.WidgetHeader',
					opacity : 0.6,
					update : function() {
						
						Dashboard.ajax(this, event, {
			                execute: "@form",
			                render: "@none",
			                event: 'update',
			                data: Widgets.getWidgetsFromDashboard(id)
			            });
						
					}
				});
		jQuery(".column").disableSelection();
	},

	//Data is an array of arrays with all the data
	// labels and colors are arrays that represent labels and color for each serie.
	// labels and color will be applied sequentialy to each data set
	// Options are generic 
	buildSeries : function(data, labels, colors, types, options) {
		series = new Array();
		for (var i=0; i < data.length; i++) {
			seriesData = new Object();
			seriesData.data = data[i];
			if (labels[i])
				seriesData.label = labels[i];
			
			if (colors[i])
				options.color = colors[i];
			
			if (types[i])
				options.type = types[i];
			
			seriesData.options = options;
			series.push(Widgets.buildSerie(seriesData));
		}
		
		return series;
		 
	},
	
	buildSerie : function(seriesData) {

		// seriesData should be an object with the following structure:
		// seriesData = {data : [1,2,3]
		//              ,label : "The Label for the series
		//              ,options: color the color
		//			type (defines the type of the series (lines, bars, dots).
		// 				defaults to lines)
		// 			steps (defaults to false)
		//			fill (defaults to false)

		serie = new Object();
		serie.data = seriesData.data;
		serie.label = seriesData.label;
		
		if (seriesData.options.steps)
			steps = seriesData.options.steps;
		else
			steps = false;
		
		if (seriesData.options.color)
			color = seriesData.options.color;
		
		if (seriesData.options.fill)
			fill = seriesData.options.fill;
		else
			fill = false;
		
		if (seriesData.options.type)
			type = seriesData.options.type;
		else
			type = "lines";
		
		if (type.indexOf("lines") != -1)
			serie.lines = {
				show : true,
				fill : fill,
				steps : steps
			};
		if (type.indexOf("bars") != -1)
			serie.bars = {
				show : true,
				fill : fill
			};
		if (type.indexOf("dots") != -1)
			serie.points = {
				show : true
			};

		return serie;
	}
}
EasyFaces = {
	escapeId : function(id) {
	    return "#" + id.replace(/:/g,"\\:");
	},
	
	styleErrors : function(data, styleClass) {
		//get ids as array
		var ids = data.split(",");
		// loop and add class
		for (var i = 0; i < ids.length; i++) {
			var obj = document.getElementById(ids[i]);
			$(obj).addClass(styleClass);
		}
	},
	
	setFocus : function(id) {
		var element = document.getElementById(id);
	} ,
		
	blockContent : function(data, options) {
        var css;

        if (!options)
                options = {};

        if (!options.css) {
                options.css = {
                                border : 'none',
                                padding : '1px',
                                backgroundColor : '#000',
                                '-webkit-border-radius' : '1px',
                                '-moz-border-radius' : '1px',
                                opacity : 1.0,
                                color : '#fff'
                        };
        }

        if (!options.what)
                options.what = '.blockable-content';

        if (!options.message) {
                options.message = $('#blockingDiv');
        }

        switch (data.status) {
        case "begin":
                jQuery(options.what).block({
                        css : options.css,
                        message : options.message
                });
                break;
        case "complete":
                jQuery(options.what).unblock();
                break;
        case "success":
                jQuery(options.what).unblock();
                break;
        	}
	},

	oncomplete : function(data, callBack) {

		if (callBack != null) {
			switch (data.status) {
			case "complete":
			case "begin":
				break;
			case "success":
				var callBackAsFunction = function() {
					eval(callBack)
				};
				if (typeof callBackAsFunction !== 'function')
					throw "You didn't pass me a function!"
				callBackAsFunction();
				break;
			}
		}

	}
}
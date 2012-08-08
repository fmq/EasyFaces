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

	},
	
	ajax : function(source, event, options) {
		//Sanity checks..
		if (typeof source === 'undefined' || source === null) {
            throw new Error("easyFaces.ajax: source not set");
        }
		
		if (typeof(options) === 'undefined' || options === null) {
            options = {};
        }
		
		var params = {};
		// We need to transform some of the options we are using for 
		// simplicity
		// If we have an event in the options add to faces queue as the behaviour event
		if (options.event) {
			params['javax.faces.behavior.event'] = options.event;
		}
		
		if (options.source)
			params['javax.faces.source'] = options.source;
		
		//now.. erase used options and simply process the rest..
		delete options.event;
		delete options.source;
		
		for (var option in options) {
            if (options.hasOwnProperty(option)) {
                params[option] = options[option];
            }
        }
		// Make call to java std library
		jsf.ajax.request(source,event,params);
		
	}
}

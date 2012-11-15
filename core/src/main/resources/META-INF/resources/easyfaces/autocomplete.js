var initialized = false;

EasyFaces.autocomplete = {
		
	inputOnFocus : function(clientId) {
		if (!initialized) {
			// Ajax events
			jsf.ajax.addOnError(EasyFaces.errorHandler);
			
			var onComplete = function onComplete(data) {
				if (data.status == 'success') {
					// Bind mouse hover event to highlight
					if (data.responseXML.getElementById(clientId)) {
						var list = data.responseXML.getElementById(clientId).firstChild.nodeValue;
						if (list) {
							EasyFaces.autocomplete.bindListActions(data.source, document.getElementById(clientId));
						}
					}
				}
			}
			
			jsf.ajax.addOnEvent(onComplete);
			initialized = true;
		}
	},
		
	updateItems : function(input, event, clientId, delay) {
		// If not 3 chars just return
		if (input.value.length < 3) {
			return;
		}
		// Get key pressed
		var elm = (input.setSelectionRange) ? event.which : event.keyCode;
		// Return if esc, etc.
		if ((elm < 32) || (elm >= 33 && elm <= 46) || (elm >= 112 && elm <= 123) || elm == 91) {
			return;
		}
		// If alls good process request
		var keystrokeTimeout;
		var focusLostTimeout;
		
		// Create ajax call
		var ajaxRequest = function() {
			var options = {
				execute : clientId,
				source : clientId,
				event : 'valueChange',
				render : clientId,
				x : input.offsetLeft,
				y : input.offsetTop + input.offsetHeight,
				width : input.offsetWidth
			};

			EasyFaces.ajax(input, event, options);

		}
		// Execute ajax request with delay
		window.clearTimeout(keystrokeTimeout)
		if (!delay)
			delay = 0;

		keystrokeTimeout = window.setTimeout(ajaxRequest, delay)

	},

	bindListActions : function(input, list) {
		// Bind mouse hover event
		$(".autocomplete-list-item").hover(function() {
			$(this).addClass("autocomplete-active-list-item");
			$(this).children("a").attr("id","ac-selected-item");
		}, function() {
			$(this).removeClass("autocomplete-active-list-item");
			$(this).children("a").removeClass("id");
		});
		// Bind key events to input
		if (input) {
			var lastKey = null;
			$(input).keydown(function(e) {
				lastKey = e.keyCode;
				switch (e.keyCode) {
				case 38:
					e.preventDefault();
					EasyFaces.autocomplete.changeSelectedItem(list, "up");
					break;
				case 40: 
					e.preventDefault();
					EasyFaces.autocomplete.changeSelectedItem(list, "down");
					break;
				case 9:  
				case 13: 
					if( EasyFaces.autocomplete.selectCurrent(input, list) ){
						$(input).get(0).blur();
						e.preventDefault();
					}
					break;
				}
			});

		}
	},

	selectCurrent : function(input, list) {
		var $listItems = $("li", list);
		if (!$listItems)
			return false;
		
		var $selected = $listItems.filter('.autocomplete-active-list-item');
		if ($selected)
			input.value = $selected.first().text();
		
		$listItems.removeClass('autocomplete-active-list-item');
		
		return true;
	},
	
	changeSelectedItem : function(list, direction) {
		
		var $listItems = $("li", list);
		
		if (!$listItems)
			return false;
		
		var $selected = $listItems.filter('.autocomplete-active-list-item'), $current;
        
		$listItems.removeClass('autocomplete-active-list-item');
	
	    if ( direction == 'down' ) {
	        if ( ! $selected.length || $selected.is(':last-child') ) {
	            $current = $listItems.eq(0);
	        } else {
	            $current = $selected.next();
	        }
	    } else if ( direction == 'up' ) {
	        if ( ! $selected.length || $selected.is(':first-child') ) {
	            $current = $listItems.last();
	        } else {
	            $current = $selected.prev();
	        }
	    }
	
	    $current.addClass('autocomplete-active-list-item');
	},
	
	inputLostFocus : function(clientId) {
		var list = document.getElementById(clientId);
		if (list) {
			var hideListbox = function() {
				$(list).hide(200);
			}

			focusLostTimeout = window.setTimeout(hideListbox, 200);
		}
	}

}
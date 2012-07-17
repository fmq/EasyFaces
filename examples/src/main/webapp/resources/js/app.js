/*
 * Block content. Called from onComplete..
 */
function blockContent(data) {
	switch (data.status) {
	case "begin":
		jQuery(".blockable-content").block({
			css : {
				border : 'none',
				padding : '15px',
				backgroundColor : '#000',
				'-webkit-border-radius' : '10px',
				'-moz-border-radius' : '10px',
				opacity : .5,
				color : '#fff'
			},
			message : $('#blockingDiv')
		});
		break;
	case "complete":
		jQuery(".blockable-content").unblock();
		break;
	case "success":
		break;
	}
}

function onComplete(data, doBlockContent, callBack) {

	if (doBlockContent == 'true') {
		blockContent(data);
	}

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

/*
 * Modals
 */
function showModal(modalId) {
	jQuery(modalId).modal('show')
}

function closeModal(modalId) {
	jQuery(modalId).modal('hide')
}
/*
 * Hide show
 */

function toggle(hideShowId) {
	jQuery(hideShowId).collapse('toggle');
}

function hideToggle(hideShowId) {
	jQuery(hideShowId).collapse('hide');
}

function showToggle(hideShowId) {
	jQuery(hideShowId).collapse('show');
}

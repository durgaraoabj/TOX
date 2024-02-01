jQuery(document).ready(function () {
	jQuery.struts2_jquery_ui.initDatepicker(true);
	$.each( itemDateTime, function( idval, nameval ) {
		  var options = {};
			options.showOn = "both";
			options.buttonImage = "/VMS/struts/js/calendar.gif";
			options.changeMonth = true;
			options.changeYear = true;
			options.displayformat = dateFormat;
			options.timepicker = true;
			options.showSecond = true;
			options.timeFormat = "hh:mm:ss";
			options.jqueryaction = "datepicker";
			options.id = idval;
			options.name = nameval;
		jQuery.struts2_jquery_ui.bind(jQuery('#'+idval),options);
	});
});
function vmsDateTimeCalener(idval, nameVal) {
	var options = {};
	options.showOn = "both";
	options.buttonImage = "/VMS/struts/js/calendar.gif";
	options.changeMonth = true;
	options.changeYear = true;
	options.displayformat = dateFormat;
	options.timepicker = true;
	options.showSecond = true;
	options.timeFormat = "hh:mm:ss";
	options.jqueryaction = "datepicker";
	options.id = idval;
	options.name = nameVal;
jQuery.struts2_jquery_ui.bind(jQuery('#'+idval),options);
};
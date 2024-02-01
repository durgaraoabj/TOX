jQuery(document).ready(function () {
	jQuery.struts2_jquery_ui.initDatepicker(true);
	$.each( itemDateOnly, function( idval, nameval ) {
		  var options = {};
			options.showOn = "both";
			options.buttonImage = "/BA/struts/js/calendar.gif";
			options.changeMonth = true;
			options.changeYear = true;
			options.displayformat = dateFormat;
			options.jqueryaction = "datepicker";
			options.id = idval;
			options.name = nameval;
			options.yearRange= '-60:-18';
		jQuery.struts2_jquery_ui.bind(jQuery('#'+idval),options);
	});
});
function vmsDateCalener(idval, nameVale) {
	var options = {};
	options.showOn = "both";
	options.buttonImage = "/BA/struts/js/calendar.gif";
	options.changeMonth = true;
	options.changeYear = true;
	options.displayformat = dateFormat;
	options.jqueryaction = "datepicker";
	options.id = idval;
	options.name = nameVale;
	options.yearRange= '-60:-18';
jQuery.struts2_jquery_ui.bind(jQuery('#'+idval),options);
};

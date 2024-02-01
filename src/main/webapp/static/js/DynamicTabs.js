/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {
    var arid=$("#getArDetailos").val();
	$("#content div").hide(); // Initially hide all content
	//$("#tabs li:first").attr("id",arid); // Activate first tab
	$("#content div:first").fadeIn(); // Show first tab content
    
    $('#tabs a').click(function(e) {
        e.preventDefault();     
        var arid=$("#getArDetailos").val();
       $("#content div").hide(); //Hide all content
        $("#tabs li").attr("id",""); //Reset id's
     //   $(this).parent().attr("id","current"); // Activate this
        $('#' + $(this).attr('title')).fadeIn(); // Show content for current tab
    });
})();
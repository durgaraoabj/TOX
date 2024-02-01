<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title><tiles:insertAttribute name="title" /></title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta name="viewport" content="width=device-width, initial-scale=1">
 
  <!-- Font Awesome -->
  <link rel="stylesheet" href='<c:url value="/static/admintltcss/plugins/fontawesome-free/css/all.min.css"/>'>
  <!-- Ionicons -->
  <link rel="stylesheet" href='<c:url value="/static/https/css/ionicons.min.css"/>'>
<!--   <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css"> -->
  <!-- Tempusdominus Bbootstrap 4 -->
  <link rel="stylesheet" href='<c:url value="/static/admintltcss/plugins/tempusdominus-bootstrap-4/css/tempusdominus-bootstrap-4.min.css"/>'>
  <!-- iCheck -->
  <link rel="stylesheet" href='<c:url value="/static/admintltcss/plugins/icheck-bootstrap/icheck-bootstrap.min.css"/>'>
  <!-- JQVMap -->
  <link rel="stylesheet" href='<c:url value="/static/admintltcss/plugins/jqvmap/jqvmap.min.css"/>'>
  <!-- Theme style -->
  <link rel="stylesheet" href='<c:url value="/static/admintltcss/dist/css/adminlte.min.css"/>'>
  <!-- overlayScrollbars -->
  <link rel="stylesheet" href='<c:url value="/static/admintltcss/plugins/overlayScrollbars/css/OverlayScrollbars.min.css"/>'>
  <!-- Daterange picker -->
<%--   <link rel="stylesheet" href='<c:url value="/static/admintltcss/plugins/daterangepicker/daterangepicker.css"/>'> --%>
  <!-- summernote -->
  <link rel="stylesheet" href='<c:url value="/static/admintltcss/plugins/summernote/summernote-bs4.css"/>'>
  <!-- DataTables -->
  <link rel="stylesheet" href='<c:url value="/static/admintltcss/plugins/datatables-bs4/css/dataTables.bootstrap4.css"/>'>
  
  <script src='<c:url value ="/static/sweetalert/sweetalert2.js"/>'></script>
  <link rel="stylesheet" href='<c:url value="/static/sweetalert/sweetalert2.css"/>'>
  <!-- Google Font: Source Sans Pro -->
  <link rel="stylesheet" href='<c:url value="/static/https/css/fonts.googleapis.com.css"/>'>
<!--   <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700" rel="stylesheet"> -->
  
   <!-- Date Picker Theam -->
  <link rel="stylesheet" href='<c:url value="/static/datePickerTheam.css"/>'>
<!--   clock links -->
<%--   <link rel="stylesheet" type="text/css" href='<c:url value="/static/clock/assets/css/bootstrap.min.css"/>'> --%>
  <link rel="stylesheet" type="text/css" href='<c:url value="/static/clock/head.css"/>'>
<link rel="stylesheet" type="text/css" href='<c:url value="/static/clock/dist/bootstrap-clockpicker.min.css"/>'>
<link rel="stylesheet" type="text/css" href='<c:url value="/static/clock/assets/css/github.min.css"/>'>
<!--   clock links -->
   <!-- jQuery -->
	<script src='<c:url value="/static/admintltcss/plugins/jquery/jquery.min.js"/>'></script>
	<!-- jQuery UI 1.11.4 -->
	<script src='<c:url value="/static/admintltcss/plugins/jquery-ui/jquery-ui.min.js"/>'></script> 
	<script type="text/javascript">
	    var mainUrl = "";
		function synchronousAjaxCall(input){
			var result;
			$.ajax({
		        type: "GET",
		        url: input,
		        async: true,
		        success : function(data) {
		        	result = data;
		        }
		    });
			return result;
		}
		function asynchronousAjaxCall(input){
			var result;
			$.ajax({
		        type: "GET",
		        url: input,
		        async: false,
		        success : function(data) {
		        	result = data;
		        }
		    });
			return result;
		}
		function asynchronousPostAjaxCall(input){
			var result;
			$.ajax({
		        type: "POST",
		        url: input,
		        async: false,
		        success : function(data) {
		        	result = data;
		        }
		    });
			return result;
		}  
	</script>
</head>
<body class="hold-transition sidebar-mini layout-fixed">
<input type="hidden" id="mainUrl" value="${mainUrl }"/>
<input type="hidden" id="dateFormatJsp" value="${dateFormatJsp }"/>
<div class="wrapper">
  <!-- Navbar -->
  <nav class="main-header navbar navbar-expand navbar-white navbar-light">
    <!--   	Main Menu -->
    <tiles:insertAttribute name="mainMenu" />
    <!--   	Main Menu Rigt Side-->
    <tiles:insertAttribute name="mainMenuRigtSide" />
  </nav>
  <!-- /.navbar -->
  
  <!--   Side menu -->
  <tiles:insertAttribute name="menu" />



  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
	<!-- 	message and navigation bar -->
	<tiles:insertAttribute name="messagePath" />
	
    <!-- Main content -->
    <section class="content">
      <tiles:insertAttribute name="body" />   
    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->


	<!--   Footer part -->
	<tiles:insertAttribute name="footerPart" />
	

  <!-- Control Sidebar -->
  <aside class="control-sidebar control-sidebar-dark">
    <!-- Control sidebar content goes here -->
  </aside>
  <!-- /.control-sidebar -->
</div>
<!-- ./wrapper -->

<!-- jQuery -->
<%-- <script src='<c:url value="/static/admintltcss/plugins/jquery/jquery.min.js"/>'></script> --%>
<!-- jQuery UI 1.11.4 -->
<%-- <script src='<c:url value="/static/admintltcss/plugins/jquery-ui/jquery-ui.min.js"/>'></script> --%>
<!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
<script>
  $.widget.bridge('uibutton', $.ui.button)
</script>
<!-- Bootstrap 4 -->
<script src='<c:url value="/static/admintltcss/plugins/bootstrap/js/bootstrap.bundle.min.js"/>'></script>
<!-- ChartJS -->
<script src='<c:url value="/static/admintltcss/plugins/chart.js/Chart.min.js"/>'></script>
<!-- Sparkline -->
<script src='<c:url value="/static/admintltcss/plugins/sparklines/sparkline.js"/>'></script>
<!-- JQVMap -->
<script src='<c:url value="/static/admintltcss/plugins/jqvmap/jquery.vmap.min.js"/>'></script>
<script src='<c:url value="/static/admintltcss/plugins/jqvmap/maps/jquery.vmap.usa.js"/>'></script>
<!-- jQuery Knob Chart -->
<script src='<c:url value="/static/admintltcss/plugins/jquery-knob/jquery.knob.min.js"/>'></script>
<!-- daterangepicker -->
<script src='<c:url value="/static/admintltcss/plugins/moment/moment.min.js"/>'></script>
<%-- <script src='<c:url value="/static/admintltcss/plugins/daterangepicker/daterangepicker.js"/>'></script> --%>
<!-- Tempusdominus Bootstrap 4 -->
<%-- <script src='<c:url value="/static/admintltcss/plugins/tempusdominus-bootstrap-4/js/tempusdominus-bootstrap-4.min.js"/>'></script> --%>
<!-- Summernote -->
<script src='<c:url value="/static/admintltcss/plugins/summernote/summernote-bs4.min.js"/>'></script>
<!-- overlayScrollbars -->
<script src='<c:url value="/static/admintltcss/plugins/overlayScrollbars/js/jquery.overlayScrollbars.min.js"/>'></script>
<!-- AdminLTE App -->
<script src='<c:url value="/static/admintltcss/dist/js/adminlte.js"/>'></script>
<!-- AdminLTE dashboard demo (This is only for demo purposes) -->
<script src='<c:url value="/static/admintltcss/dist/js/pages/dashboard.js"/>'></script>
<!-- DataTables -->
<script src='<c:url value="/static/admintltcss/plugins/datatables/jquery.dataTables.js"/>'></script>
<script src='<c:url value="/static/admintltcss/plugins/datatables-bs4/js/dataTables.bootstrap4.js"/>'></script>
<!-- AdminLTE for demo purposes -->
<script src='<c:url value="/static/admintltcss/dist/js/demo.js"/>'></script>
<script>
  $(function () {
    $("#example1").DataTable();
    $('#example2').DataTable({
      "paging": true,
      "lengthChange": false,
      "searching": false,
      "ordering": true,
      "info": true,
      "autoWidth": false,
    });
  });
  mainUrl = $("#mainUrl").val();
</script>


<!-- clock links -->
<%-- <script type="text/javascript" src='<c:url value="/static/clock/assets/js/bootstrap.min.js"/>'></script> --%>     
<%-- <script type="text/javascript" src='<c:url value="/static/clock/dist/bootstrap-clockpicker.min.js"/>'></script> --%>
<script type="text/javascript">
var input = $('#single-input').clockpicker({
	placement: 'bottom',
	align: 'left',
	autoclose: true,
	'default': 'now'
});

$('.clockpicker-with-callbacks').clockpicker({
		donetext: 'Done',
		init: function() { 
			console.log("colorpicker initiated");
		},
		beforeShow: function() {
			console.log("before show");
		},
		afterShow: function() {
			console.log("after show");
		},
		beforeHide: function() {
			console.log("before hide");
		},
		afterHide: function() {
			console.log("after hide");
		},
		beforeHourSelect: function() {
			console.log("before hour selected");
		},
		afterHourSelect: function() {
			console.log("after hour selected");
		},
		beforeDone: function() {
			console.log("before done");
		},
		afterDone: function() {
			console.log("after done");
		}
	})
	.find('input').change(function(){
		console.log(this.value);
	});

if (/mobile/i.test(navigator.userAgent)) {
	$('input').prop('readOnly', true);
}
</script>
<script type="text/javascript" src='<c:url value="/static/clock/assets/js/highlight.min.js"/>'></script>
<script type="text/javascript">
hljs.configure({tabReplace: '    '});
hljs.initHighlightingOnLoad();
</script>
<!-- clock links -->
</body>
</html>

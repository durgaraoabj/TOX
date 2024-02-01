<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title><tiles:getAsString name="title" /></title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.7 -->
  <link rel="stylesheet" href='<c:url value="/static/template/bower_components/bootstrap/dist/css/bootstrap.min.css"/>'>
  <!-- Font Awesome -->
  <link rel="stylesheet" href='<c:url value="/static/template/bower_components/font-awesome/css/font-awesome.min.css"/>'>
  <!-- Ionicons -->
  <link rel="stylesheet" href='<c:url value="/static/template/bower_components/Ionicons/css/ionicons.min.css"/>'>
  <!-- Theme style -->
  <link rel="stylesheet" href='<c:url value="/static/template/dist/css/AdminLTE.min.css"/>'>
  <!-- iCheck -->
  <link rel="stylesheet" href='<c:url value="/static/template/plugins/iCheck/square/blue.css"/>'>
  <!-- Google Font -->
  <link rel="stylesheet" href='<c:url value="/static/https/css/fonts.googleapis.com2.css"/>'>
<!--   <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic"> -->
</head>  
<body>
	<tiles:insertAttribute name="body" /> 
<!-- jQuery 3 -->
<script src='<c:url value="/static/template/bower_components/jquery/dist/jquery.min.js"/>'></script>
<!-- Bootstrap 3.3.7 -->
<script src='<c:url value="/static/template/bower_components/bootstrap/dist/js/bootstrap.min.js"/>'></script>
<!-- iCheck -->
<script src='<c:url value="/static/template/plugins/iCheck/icheck.min.js"/>'></script>
<script>
  $(function () {
    $('input').iCheck({
      checkboxClass: 'icheckbox_square-blue',
      radioClass: 'iradio_square-blue',
      increaseArea: '20%' /* optional */
    });
  });
</script>
</body>
</html>
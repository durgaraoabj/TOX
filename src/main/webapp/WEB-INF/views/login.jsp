<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>Covide | Log in</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <!-- Font Awesome -->
  <link rel="stylesheet" href='<c:url value="/static/admintltcss/plugins/fontawesome-free/css/all.min.css"/>'>
  <!-- Ionicons -->
  <link rel="stylesheet" href='<c:url value="/static/https/css/ionicons.min.css"/>'>
<!--   <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css"> -->
  <!-- icheck bootstrap -->
  <link rel="stylesheet" href='<c:url value="/static/admintltcss/plugins/icheck-bootstrap/icheck-bootstrap.min.css"/>'>
  <!-- Theme style -->
  <link rel="stylesheet" href='<c:url value="/static/admintltcss/dist/css/adminlte.min.css"/>'>
  <!-- Google Font: Source Sans Pro -->
  <link rel="stylesheet" href='<c:url value="/static/https/css/fonts.googleapis.com.css"/>'>
<!--   <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700" rel="stylesheet"> -->
</head>
<%-- background="<c:url value="/static/admintltcss/dist/img/loginimage.jpg"/>" --%>
<body  background="<c:url value="/static/images/new-hope_1920x720_v2.png"/>" style="background-size: cover;min-height: 0px;">
	<div class="login-box" style="margin-left: 16%;margin-top:12%;">
  
  <!-- /.login-logo -->
  <div class="card " >
    <div class="card-body login-card-body cntr" >
      <div class="login-box-msg " id="title" ><h5><b style="color:#09235ed9">NEO TOX</b></h5></div>
	  <c:url var="loginUrl" value="/login" />
    <form action="${loginUrl}" method="post" id="logform">
    <c:if test="${param.error != null}">
		<div class="alert alert-success" style="height: 45px;padding-top: 0px;" align="center">
			<p>Invalid User Name and password.</p>
		</div>
	</c:if>
	<c:if test="${param.logout != null}">
		<div class="alert alert-success" style="height: 45px;padding-top: 0px;" align="center">
			<p>You have been logged out successfully.</p>
		</div>
	</c:if>
      <div class="form-group has-feedback">
        <input type="text" name="username" id="username" class="form-control" placeholder="UserName">
        <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
      </div>
      <div class="form-group has-feedback">
        <input type="password" name="password" id="password" class="form-control" placeholder="Password">
        <span class="glyphicon glyphicon-lock form-control-feedback"></span>
      </div>
      
      <input type="hidden" name="barcode" id="barcode" >
<!--       <div class="form-group has-feedback"> -->
<!--         (OR) -->
<!--         <span class="glyphicon glyphicon-lock form-control-feedback"></span> -->
<!--       </div> -->
      
<!--       <div class="form-group has-feedback"> -->
<!--         Scan Barcode (For CPU Activity): <input type="text" name="barcode" id="barcode" class="form-control" placeholder="Barcode""> -->
<!--         <span class="glyphicon glyphicon-lock form-control-feedback"></span> -->
<!--       </div> -->
      
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
      <div class="row">
      <!--  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
        <div  style="align-items: center; width: 315px; margin-left: 10px;">
          <button type="submit" class="btn btn-primary btn-block btn-flat" id="signin">Sign In</button>
        </div>
        <!-- class="col-xs-7" -->
        <!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;
         <div class="col-xs-4" style="align-items: center;">
          <button type="submit" class="btn btn-primary btn-block btn-flat">Reset</button>
        </div> -->
        <!-- /.col -->
      </div>
    </form>
  </div>
</div>
<!-- /.login-box -->
</div>


<!-- jQuery -->	
<script src='<c:url value="/static/admintltcss/plugins/jquery/jquery.min.js"/>'></script>
<!-- Bootstrap 4 -->
<script src='<c:url value="/static/admintltcss/plugins/bootstrap/js/bootstrap.bundle.min.js"/>'></script>
<!-- AdminLTE App -->
<script src='<c:url value="/static/admintltcss/dist/js/adminlte.min.js"/>'></script>
<script type="text/javascript">
$("#barcode").on('input',function(e){
	var barcode = $("#barcode").val();
	var length = barcode.length;
	if(length == 21){
		var prefix = barcode.substr(0,2);
		if(prefix == "02"){//lab tech
			$("#username").val("CPU Activity");
			$("#password").val("login19860101");
			$("#logform").submit();	
		}
		$("#barcode").val("");
	}
});
	
</script>
</body>
</html>

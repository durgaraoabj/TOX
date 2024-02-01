<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<!-- <script src="https://cdn.jsdelivr.net/npm/vue"></script> -->
<script src='<c:url value="/static/Vuejs.js"/>'></script>
</head>
<body>
<div id="app">
  <h1>{{ message }}</h1>
</div>

<script>
var myObject = new Vue({
  el: '#app',
  data: {message: 'Hello Vue!'}
})
</script>


</body>
</html>
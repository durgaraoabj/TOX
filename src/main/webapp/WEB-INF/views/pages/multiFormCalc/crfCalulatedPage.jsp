<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title></title>
<script type="text/javascript">
  var eleIdsArr = [];
  var eleValArr = [];
  var msgArr = [];
</script>
</head>
<body>
<c:forEach items="${calcMap}" var="calMap">
	<script type="text/javascript">
		eleIdsArr.push('${calMap.key}');
		eleValArr.push('${calMap.value}');
	</script>
</c:forEach>
<c:choose>
	<c:when test="${calcMap.size() eq 0}">
		<script>
			msgArr.push('There is No Formulas Available for this CRF.');
		</script>
	</c:when>
	<c:otherwise>
		<script>
			msgArr = [];
		</script>
	</c:otherwise>
</c:choose>
</body>
</html>
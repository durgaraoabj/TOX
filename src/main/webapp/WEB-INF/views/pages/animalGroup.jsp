<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<div class="box-body">
		<table>
			<tr>
				<th>Group :</th>
				<td><select id="groupId" onchange="groupchange(this.value)">
				<option value="-1">--Select--</option>
				<c:forEach items="${groups}" var="g">
					<option value="${g.id}">${g.name}</option>
				</c:forEach>
				</select></td>
			</tr>
			<tr>
				<th>Group :</th>
				<td><select id="groupId" onchange="groupchange(this.value)">
				<option value="-1">--Select--</option>
				<c:forEach items="${groups}" var="g">
					<option value="${g.id}">${g.name}</option>
				</c:forEach>
				</select></td>
			</tr>
			<tr>
				<th>Animal No :</th>
				<td><input type="text" id="animalNo" value="" /></td>
			</tr>
			<tr>
				<th>Animal No :</th>
				<td><input type="text" id="animalNo" value="" /></td>
			</tr>
		</table>


		<div id="cagedList">
			<c:forEach items="${cagedAnimals}" var="animalCage">
				<table class="table table-bordered table-striped">
					<tr>
						<TH>Cage Id :</TH>
						<th colspan="9">${animalCage.key}</th>
					</tr>
					<c:forEach items="${animalCage.value}" var="ani" varStatus="st">
						<c:if test="${st.index+1 % 10 == 0}">
							<tr>
						</c:if>
						<td>${ani.animal.permanentNo}</td>
						<c:if test="${st.index+1 % 10 == 0}">
							</tr>
						</c:if>
					</c:forEach>
				</table>
			</c:forEach>
		</div>


		<c:url value="/cageDesign/saveCageDesign" var="savecageInfo"></c:url>
		<form:form action="${savecageInfo}" method="POST" id="savefrom">
			<input type="hidden" name="cageNameOrNo" id="cageNameOrNo">
			<div id="animalIdsDiv"></div>
		</form:form>
	</div>

</body>
<script type="text/javascript">
	function geteles() {
		$("#animalIdsDiv").html("");
		var animals = [];
		$("#lb ul li").each(
				function() {
					console.log($(this).attr("id"));
					animals.push($(this).attr("id"))
					$("#animalIdsDiv").append(
							'<input type="hidden" name="animalIds" id="animalIds" value="'
									+ $(this).attr("id") + '">');
				});
		$("#cageNameOrNo").val($("#cageNo").val());
		var flag = false;
		$.each(animals, function(k, v) {
			flag = true;
		});
		$("#savefrom").submit();
	}

	$(function() {
		$('.droptrue').on('click', 'li', function() {
			$(this).toggleClass('selected');
		});

		$("ul.droptrue").sortable(
				{
					connectWith : 'ul.droptrue',
					opacity : 0.6,
					revert : true,
					helper : function(e, item) {
						console.log('parent-helper');
						console.log(item);
						if (!item.hasClass('selected'))
							item.addClass('selected');
						var elements = $('.selected').not(
								'.ui-sortable-placeholder').clone();
						var helper = $('<ul/>');
						item.siblings('.selected').addClass('hidden');
						return helper.append(elements);
					},
					start : function(e, ui) {
						var elements = ui.item.siblings('.selected.hidden')
								.not('.ui-sortable-placeholder');
						ui.item.data('items', elements);
					},
					receive : function(e, ui) {
						ui.item.before(ui.item.data('items'));
					},
					stop : function(e, ui) {
						ui.item.siblings('.selected').removeClass('hidden');
						$('.selected').removeClass('selected');
					},
					update : function() {
						updatePostOrder();
						updateAdd();
					}
				});

		$("#sortable1, #sortable2, #sortable3").disableSelection();
		$("#sortable1, #sortable2, #sortable3").css('minHeight',
				$("#sortable1, #sortable2").height() + "px");
	});

	$('[data-search]').on(
			'keyup',
			function() {
				var searchVal = $(this).val();
				var filterItems = $('[data-filter-item]');

				if (searchVal != '') {
					filterItems.addClass('hidden');
					$(
							'[data-filter-item][data-filter-name*="'
									+ searchVal.toLowerCase() + '"]')
							.removeClass('hidden');
				} else {
					filterItems.removeClass('hidden');
				}
			});

	function updatePostOrder() {
		var arr = [];
		$("#sortable2 li").each(function() {
			arr.push($(this).attr('id'));
		});
		$('#postOrder').val(arr.join(','));
	}

	function updateAdd() {
		var arr = [];
		$("#sortable3 li").each(function() {
			arr.push($(this).attr('id'));
		});
		$('#add').val(arr.join(','));
	}
</script>
</html>
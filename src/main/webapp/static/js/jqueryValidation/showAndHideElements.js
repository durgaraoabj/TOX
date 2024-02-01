function hideOrEanbleElement(id, type){
	if(type == 'hide')	$('[name='+id+']').attr('disabled', true);
	else $('[name='+id+']').prop('disabled', false);
}
function compareResult(condition, r1, r2){
	if(condition == '||')	return r1 || r2;
	else return r1 && r2;
}
function conpareValues(condition, value1, value2){
	if(condition == 'eq')
		return value1 == value2;
	 else if(condition == 'ne')
			return value1 != value2;
	 else if(condition < 'lt')
			return value1 <= value2;
	 else if(condition == 'lte')
			return value1 <= value2;
	 else if(condition == 'gt')
			return value1 > value2;
	 else if(condition == 'gte')
			return value1 >= value2;
}

function conpareValuesForErrorMessage(condition, value1, value2){
	var flag = false;
	if(condition == 'eq' && value1 == value2)
		return true;
	 else if(condition == 'ne' && value1 != value2)
			return true;
	 else{
		if((condition == 'lt') && (Number(value1) < Number(value2))){
			flag = true;
	 	}else if((condition == 'lte') && (Number(value1) <= Number(value2))){
	 		flag = true;
	 	}else if((condition == 'gt') && (Number(value1) > Number(value2))){
	 		flag = true;
	 	}else if((condition == 'gte') && (Number(value1) >= Number(value2))){
	 		flag =  true;
	 	}
	}
	return flag;
}
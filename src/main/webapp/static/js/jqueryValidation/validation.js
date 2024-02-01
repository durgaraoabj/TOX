var numpatt=/^[0-9\b]+$/;
var regex = new RegExp("^[a-zA-Z]+$");
function checkCrfFieldPattcren(pattern, value, id){
	if(pattern != ''){
		if(value != ''){
			$('#'+id+'_Msg').html("");
			var flag = true;
			var psize = pattern.length;
			var csize = value.length;
			if(psize == csize){
				for(var i = 0; i< psize; i++){
					if(pattern.charAt(i) == 'A' || pattern.charAt(i) == 'L'  || pattern.charAt(i) == 'C' ){
						if (!regex.test(value.charAt(i)))	flag = false;
					}else if(pattern.charAt(i) == 'N' || pattern.charAt(i) == 'H'  || pattern.charAt(i) == 'M'  || pattern.charAt(i) == 'S' ){
						if(!numpatt.test(value.charAt(i)))	flag = false;
					}else{
						if(pattern.charAt(i) != value.charAt(i))	flag = false;
					}
				}
				
			}else{
				$('#'+id+'_Msg').html("<font color='red'>Required pattern : " + pattern +"</font>");
				return false;
			}
			if(!flag)	$('#'+id+'_Msg').html("<font color='red'>Required pattern : " + pattern +"</font>");
			return flag;
		}else return true;
	}else return true;

}
/*
 Purpose: Two Strings equality checking
 input: (id,MessageField Id, String1 Value, String2 Value)
 Output: it Returns true if Strings or Equal else Return false;
 */
 function stringEqualCheck(val1, val2){
//	 alert("Working:"+ ((val1.indexOf(" ") !== -1) == (val2.indexOf(" ") !== -1)));
	 if(val1 != "" && val2 != ""){
		 if((val1.indexOf(" ") !== -1) == (val2.indexOf(" ") !== -1)){
			 if(val1 == val2){
				 return true;
			 }else{
				 return false;
			 }
		 }else{
			 return false;
		 }
	 }
 }
 
 /*
 Purpose: textfield, selectt, textarea,.......are given below type is empty field checking process in this function
 input: (id,MessageField Id, type, defaultvalue)
 		defaultvalue means example1: textfield default value is ''. example2: selectox default Value is '-1' etc.....
 Output: it Returns true if field contain any value else field is empty return false with Required Fielld.
 */

 
//Empty Validation Checking
 function emptyCheck(id, messageId, type, defaluleVal){
		if(type == 'text' || type == 'textarea' || type == 'select' || type == 'date' || type == 'dateTime' || type == 'time' || type == 'radio'|| type == 'file' ){
			if(type == 'text' || type == 'textarea' || type == 'date' || type == 'dateTime' || type == 'time'){
				if($('#'+id).val().trim() == defaluleVal){
					$('#'+messageId).html("Required Field");
					return false;
				}else{
					$('#'+messageId).html(" ");
					value = $('#'+id).val().trim();
					$('#'+id).val(value); 
					return true;
				}
			}
		}		
		if(type == 'select' || type == 'list'){
			if($('#'+id).val().trim() == defaluleVal){
				$('#'+messageId).html("Required Field");
				return false;
			}else{
				$('#'+messageId).html(" ");
				return true;
			}
		}
		if(type == 'radio' || type == 'checkbox'){
			if($('#'+id).val().trim() == defaluleVal){
				$('#'+messageId).html("Select one");
				return false;
			}else{
				$('#'+messageId).html(" ");
				return true;
			}
		}
		
		if(type == 'file'){
			if($('#'+id).val().trim() == defaluleVal){
				$('#'+messageId).html("Select File");
				return false;
			}else{
				$('#'+messageId).html(" ");
				return true;
			}
		}
		
	}

 /*
 Purpose: textfield and textarea type fields are Numaric or not checking process done in this function
 input: (id,MessageField Id, type)
 		Output: it Returns true if field contain only Numbers else it displays the Msg Value Should be Numaric.
 */
//Number Validation Function
 function numbervalidton(id, messageId, type){
	 if($('#'+id).is("input")) {
			type = $('#'+id).attr('type');
		}else if($('#'+id).is("textarea")) {
			type = "textarea";
		}
	if(type == 'text' || type == 'textarea'){
			var value = $('#'+id).val().trim();
			var regex = /^[0-9][0-9]*$/;
			if(value != ""){
				if((value.indexOf(" ") == -1)){
					if(regex.test(value)){
						$('#'+messageId).html("");
						$('#'+id).val(value);
						return true;
					}else{
						$('#'+messageId).html("Value Should be Numeric");
						return false;
					}
				}else{
					$('#'+messageId).html("Space Not Allowed");
					return false;
				}
		    }else{
		    	$('#'+messageId).html(" ");
		    	return true;
		    }
		}else{
			$('#'+messageId).html("Wrong Validation");
			return false;
		}
		
	}
 function numbervalidton(id, messageId){
	 var type="";
	 if($('#'+id).is("input")) {
			type = $('#'+id).attr('type');
		}else if($('#'+id).is("textarea")) {
			type = "textarea";
		}
	if(type == 'text' || type == 'textarea'){
			var value = $('#'+id).val().trim();
			var regex = /^[0-9][0-9]*$/;
			if(value != ""){
				if((value.indexOf(" ") == -1)){
					if(regex.test(value)){
						$('#'+messageId).html("");
						$('#'+id).val(value);
						return true;
					}else{
						$('#'+messageId).html("Value Should be Numeric");
						return false;
					}
				}else{
					$('#'+messageId).html("Space Not Allowed");
					return false;
				}
		    }else{
		    	$('#'+messageId).html(" ");
		    	return true;
		    }
		}else{
			$('#'+messageId).html("Wrong Validation");
			return false;
		}
		
	}
 
//only Single Character Reading function
 /*
  * action : Fields should have single character 
  * input : field id, message div/element id
  * out put : if more than one chareter found returns false else true
  */
 function singlecharacter(id, messageId, type){
	 var value = $('#'+id).val().trim();
	 if(value !=""){
		 if($.isNumeric($('#'+id).val())){
				$('#'+messageId).html("Enter Single Character");
				return false;
		 }else{
				productCode = $('#'+id).val().trim();
				var signleLetter = productCode.charAt(0);
				var changedVal =signleLetter.toUpperCase();
				$('#'+messageId).html(" ");
				$('#'+id).val(changedVal);
		 }
	 }else{
		 $('#'+messageId).html(" ");
	 }
 }
 
 //Date Comparision
 /*
 Purpose: Date comparision Logic(< , >, <=, >=, ==) is done in this function
 input: (id,MessageField Id, type, comparingDate, conditiontype) are Required
 		Output: it Returns true if Date comparision is true else it returns false.
 */

 function dateValidation(id, messageId, type,presentDate, condition){
		 $('#'+messageId).html("");
		 var dateVal = $('#'+id).val().trim();
		 var equal = false, lessthan = false, graterthan = false, lessthanorequal = false, graterthanorequal = false;
		 var presentDate = asynchronousPostAjaxCall("vmsFormatDate.action?unforamtDate="+presentDate);
//	     var dateVal = asynchronousPostAjaxCall("vmsFormatDate.action?unforamtDate="+dateVal);
		 var  dateVal = dateVal.slice(0, 10);
	   if(dateVal != ""){
		   if(dateVal == presentDate){
		    	  equal = true;
			 }else if(dateVal < presentDate){
				  lessthan = true;
			 }else if(dateVal > presentDate){
				  graterthan = true;
		     }
		     if(condition == "=="){
		    	 return equal;
		     }else if(condition == "<="){
		    	 if(equal == true || lessthan == true){
		    		 lessthanorequal = true;
		    	 }
		    	 return lessthanorequal;
		     }else if(condition == ">="){
		    	 if(equal == true || graterthan == true){
		    		 graterthanorequal = true;
		    	 }
		    	 return graterthanorequal;
		     }else if(condition == "<"){
		    	 return lessthan;
		     }else if(condition == ">"){
		    	 return graterthan;
		     }
	   }else{
		   $('#'+messageId).html(" ");
	   }
   }
 
 
 /*
 Purpose: This function Checks the textfield value is in the format of HH:MM or not
 input: (id,MessageField Id, type, valueoftextfield)
 		Output: it Returns true if input value is HH:MM else return false
 */
 //Time validatin without only format HH:MM

 function timeValidationFormatCheck(id,messageId,type,value){
	 if(value != ""){
	 	if(value.indexOf(" ") == -1){
		 	if(/^\d{2,}:\d{2}$/.test(value)){
		 		$('#'+messageId).html(" ");
		 		$('#'+id).val(value);
		 		return true;
		 	}else{
		 		return false;
		 	}
	 	}else{
	 		$('#'+messageId).html("Spaces Not Allowed");
	 	}
    }else{
    	$('#'+messageId).html(" ");
    }
 }
 
	/*
	Purpose: This function Checks the textfield value is in the format of HH:MM or not 
				And 24 Hours and 60 Minutes condition checking process done in this function
	input: (id,MessageField Id, type, valueoftextfield)
	Output: it Returns true if  time is in  correct with formate
			else it Display msg if Minutes wrong and Hours Wronng it displays Hours wrong Message
*/

//Time Format HH:MM Validation Function
function timeValidation(id,messageId,type,value){
	if(value != ""){
		if(value.indexOf(" ") == -1){
			if(/^\d{2,}:\d{2}$/.test(value)){
				var timeArray = [];
				timeArray = value;
				var temp = [];
				temp = timeArray.split(':');
				 	for(var i=0; i<temp.length-1; i++){
						  if(temp[i]<24){
							  $('#'+messageId).html("");
							  if((i+1)==1){
								 if(temp[i+1]<=59){
									 $('#'+messageId).html("");
									 $('#'+id).val(value);
									 return true;
								  }else{
									 $('#'+messageId).html("Minutes Entered Wrong");
									 return false;
								  }
							   }
							}else{
								 $('#'+messageId).html("Hours Entered Wrong");
								 return false;
							}
				     }
				}else{
					$('#'+messageId).html("Required time format HH:MM");
					return false;
				}
		}else{
			$('#'+messageId).html("Space Not Allowed");
			return false;
		}
	}else{
		$('#'+messageId).html(" ");
	}
}


/*
Purpose: This function Checks the textfield value is Alphabets or not 
			
input: (id,MessageField Id, type)
Output: if  textbox contain alphabets ok 
		else it displays error messge
*/


//Allow only Alphabets 

function allowTextOnly(id, messageId, type){
	if(type == "text"){
			var regex = new RegExp("^[a-zA-Z\b]*$");
//		    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
			var str = $('#'+id).val().trim();
			if(str != ""){
				if(str.indexOf(" ") == -1){
				    if (regex.test(str)){
				    	$('#'+messageId).html(" ");
				    	$('#'+id).val(str);
				        return true;
				    }else
				    $('#'+messageId).html("Please Enter Valid Characters");
				    e.preventDefault();
				    return false;
				}else{
					$('#'+messageId).html("Spaces Not Allowed");
					return false
				}
			}else{
				$('#'+messageId).html(" ");
			}
		}
	}


/*
Purpose: This function Checks the textfield value is Alpha Numaric or not 
			
input: (id,MessageField Id, type)
Output: it Returns true if  textbox contain alphanumaric values
		else return false with error messge
*/

// Alphanumaric function 
function allowAlphaNumaric(id, messageId, type){
		var regex = new RegExp("^[a-zA-Z0-9\b]*$");
//	    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
		var str =  $('#'+id).val().trim();
		if(str != ""){
		    if (regex.test(str)){
	 			var res = str.match(/^\s+|\s+$/i);
		    	if(res!== null){
		    		$('#'+messageId).html("Spaces are not allowed at the begining and Endining");
		    		return false;
		    	}else{
		    		$('#'+messageId).html("");
		    		$('#'+id).val(str);
		    		return true;
		    	}
			}else{
			    $('#'+messageId).html("Please Enter Valid AlphaNumerics");
			    e.preventDefault();
			    return false;
			}
		}else{
			$('#'+messageId).html(" ");
		}
}

/*
Purpose: This function Checks the textfield value is Alpha Numaric with Specific Special characters are accepting 
			
input: (id,MessageField Id, type)
Output: it Returns true if  textbox contain Numbers or Numbers with Some Special Characters
		else return false with error messge
*/
//Numbers and Special Characters only
function numbersAndSpecilChars(id, messageId, type){
	if(type=="text"){
		value = $('#'+id).val().trim();
		var regex = new RegExp(("^[0-9?=.*!@#$%^&*]+$"));
		if(value != ""){
			if(value.indexOf(" ") == -1){
		        if (regex.test(value)) {
		        	$('#'+id).val(value);
		            $('#'+messageId).html(" ");
		            return true;
		        } else {
		            $('#'+messageId).html("Please Enter Valid Numbers And These Special Characters (.*!@#$%^&?) Are Allowed");
		            return false;
		        }
		    }else{
		    	$('#'+messageId).html("Spaces Not Allowed");
		    	return false;
		    }
	    }else{
	    	$('#'+messageId).html(" ");
	    }
	}
}
/* After text entering space not allowed */
function notAllowSpces(id, messageId, type){
	if(type = "text"){
		value = $('#'+id).val().trim();
		if(value != ""){
			$('#'+id).on('keypress', function(e) {
			    if (e.which == 32)
			    return false;
			});
		}else{
			$('#'+messageId).html(" ");
		}
	}
}
/*
Purpose: This function Checks the textfield contain numbers are provided Max Range or not  
			
input: (id,MessageField Id, type)
Output: it Returns true if  textbox contain Numbers Given range else return false 
*/
//Phone Number validation
function phoneNumberCheck(id, messageId, type,MaxNo){
	if(type = "text"){
		var Number = MaxNo;
		value = $('#'+id).val().trim();
		var result = value.split("");
		var array = [];
		array = array.concat(result);
		if(value !=""){
			if(value.indexOf(" ") == -1){
				if((array.length) == Number){
					$('#'+id).val(value);
					return true;
				}else return false;
			}else{
				$('#'+messageId).html("Spaces Not Allowed");
				return false;
			}
	    }else{
	    	$('#'+messageId).html(" ");
	    }
	}
}

/*
Purpose: This function Checks the textfield contain String@String.com format or not  
			
input: (id,MessageField Id, type)
Output: it Returns true if  textbox contain Correct Format else return false with error message
*/
// Email Id Vaidation
function emailAddreesCheck(id, messageId, type){
	value = $('#'+id).val().trim();
	var validEmailRegEx = /^[A-Z0-9_'%=+!`#~$*?^{}&|-]+([\.][A-Z0-9_'%=+!`#~$*?^{}&|-]+)*@[A-Z0-9-]+(\.[A-Z0-9-]+)+$/i;
	if(value != ""){
	  if(value.indexOf(" ") == -1){
			alert(validEmailRegEx.test(value));
			if(validEmailRegEx.test(value)){
				$('#'+messageId).html(" ");
				$('#'+id).val(value);
				return true;
			}else{
				$('#'+messageId).html("Please Enter Valid Email Id");
				return false;
			}
		}else{
			$('#'+messageId).html("Spaces Not Allowed");
			return false;
		}
    }else{
    	$('#'+messageId).html(" ");
    }
}

/*
Purpose: This function Checks the textfield value start with www. or not  
			
input: (id,MessageField Id, type)
Output: it Returns true if  textbox contain Correct Format else return false with error message
*/
// Email Id Vaidation
function urlCheck(id, messageId, type){
	value = $('#'+id).val().trim();
	if(value != ""){
		if(value.indexOf(" ") == -1){
			if (value.indexOf("www.") !== 0) {
				$('#'+messageId).html("URL Should be Start with www.");
				return false;
				
			}else {
				$('#'+messageId).html(" ");
				$('#'+id).val(value);
				return true;
			}
		}else{
			$('#'+messageId).html("Spaces Not Allowed");
			return false;
		}
	}else{
		$('#'+messageId).html(" ");
	}
}

/*
Purpose: This function Checks the textfield contain ginven upload format is there or not 
			
input: (id,MessageField Id, type)
Output: it Returns true if  textbox contain Correct Format else return false with error message
*/
// Upload format Id Vaidation
function uploadFileCheck(id, messageId, type){
	value = $('#'+id).val().trim();
	if(value != ""){
		if(value.indexOf(" ") == -1){
			if(!/(\.bmp|\.gif|\.jpg|\.jpe|\.jpeg)$/i.test(value)){
				$('#'+messageId).html("Select File ");
				return false;   
		    }else {
		    	$('#'+messageId).html(" ");
		    	$('#'+id).val(value);
		    	return true;
		    }
		}else{
			$('#'+messageId).html("Spaces Not Allowed");
			return false;
		}
	}else{
		$('#'+messageId).html(" ");
	}
}

/*
Purpose: This function Checks the textfield value is Alphabets with Spaces are accepting 
			
input: (id,MessageField Id, type)
Output: it Returns true if  textbox contain Alphabets with Spaces
		else return false with error messge
*/
//Alphabets with Spaces only
function alphabetsWithSpaces(id, messageId, type) {
	value = $('#'+id).val().trim();
    var regex = /^[A-Za-z\s]+$/;
    if(value != ""){
	    if(regex.test(value)){
		    	if(value.indexOf(" ") !== -1){
					$('#'+messageId).html("Space Not Allowed");
					return false;
				}else{
					$('#'+messageId).html("");
					$('#'+id).val(value);
					return true;
				}
		    }else{
		    	$('#'+messageId).html("Please Enter Valid Alphabets");
		    	return false;
		    }
     }else{
    	 $('#'+messageId).html(" ");
     }
   
}

/*
Purpose: This function Checks the textfield value is Integer or Decimal Number
			
input: (id,MessageField Id, type)
Output: it Returns true if  textbox contain Integer or Decimal Number
		else return false with error messge
*/
//Integer with double value
function intergerWithDoubleCheck(id, messageId, type) {
	value = $('#'+id).val();
	if(value != ""){
    var regex = /^[1-9]\d*(\.\d+)?$/;
    if(value.indexOf(" ") == -1){
	    if(regex.test(value)){
	    	$('#'+id).val(value);
	    	return true;
	    }else{
	    	return false;
	    }
    }else{
    	$('#'+messageId).html("Spaces Not Allowed");
		return false;
    }
  }else{
	  $('#'+messageId).html(" ");
  }
   
}
/*
Purpose: This function Checks the textfield value accepts only Integer or not
			
input: (id,MessageField Id, type)
Output: it Returns true if  textbox contain Integer
		else return false
*/
//Intege value or not
function integerCheck(id, messageId, type){
	value = $('#'+id).val().trim();
	var regex = /^[0-9][0-9]*$/;
   if(value != ""){
		if(value.indexOf(" ") == -1){
		    if(regex.test(value)){
		    	$('#'+id).val(value);
		    	return true;
		    }else{
		    	return false;
		    }
		}else{
			$('#'+messageId).html("Spaces Not Allowed");
			return false;
		}
   }else{
	   $('#'+messageId).html(" ");
   }
}

/*
Purpose: This function Checks the textfield value is Alphabets with all Special Charactors
			
input: (id,MessageField Id, type)
Output: it Returns true if  textbox contain Alphabets with all Special Charactors
		else return false with error messge
*/
//Alphabets with all Special Charactors allowed
function alphaNumaricWithAllSpecialCharacters(id, messageId, type) {
	var value = $('#'+id).val().trim();
    var regex = /^[0-9a-zA-Z\s\r\n@!#\$\^%&*()+=\-\[\]\\\';,\.\/\{\}\|\":<>\?]+$/;
    if(value != ""){
  	if(regex.test(value)){
  		var res = value.match(/^\s+|\s+$/i);
    	if(res!== null){
    		$('#'+messageId).html("Spaces are not allowed at the begining and Endining");
    		return false;
    	}else{
    		$('#'+messageId).html("");
    		$('#'+id).val(value);
    		return true;
    	}
	}
  }else{
	  $('#'+messageId).html(" ");
  }
}

//Allows With Spaces

function alphaNumaricWithAllSpecialCharactersWithSpaces(id, messageId, type) {
	var value = $('#'+id).val().trim();
//    var regex = /^[0-9a-zA-Z\s\r\n@!#\$\^%&*()+=\-\[\]\\\';,\.\/\{\}\|\":<>\?]+$/;
    if(value != ""){
    	 return true;
    }else return false;
 
}
   
/*
Purpose: This function Checks the textfield value is accept only even numbers
			
input: (id,MessageField Id, type)
Output: it Returns true if  textbox contain only even numbers return true
		else return false with error messge
*/
//Only Even numbers
function evenNumbersCheck(id, messageId, type) {
	var value = $('#'+id).val().trim();
    var regex = /^[0-9]*[02468]$/;
    if(value != ""){
	    if(value.indexOf(" ") == -1){
		    if(regex.test(value)){
		    	$('#'+messageId).html(" ");
		    	$('#'+id).val(value);
		    	return true;
		    }else{
		    	$('#'+messageId).html("Please Enter only Even Numbers");
		    	return false;
		    }
	    }else{
	    	$('#'+messageId).html("Spaces Not Allowed");
			return false;
	    }
    }else{
    	$('#'+messageId).html(" ");
    }
}
/*
Purpose: This function Checks the textfield value is accept Integers with negitive integer numbers
			
input: (id,MessageField Id, type)
Output: it Returns true if  textbox contain only even numbers return true
		else return false with 
*/
//Only Even numbers
function IntegerNumbersWithNegitivevaluesCheck(id, messageId, type){
	value = $('#'+id).val().trim();
	var regex = /^-?\d?\d+$/;
	if(value != ""){
	if(value.indexOf(" ") == -1){
	    if(regex.test(value)){
		     $('#'+messageId).html("");
		     $('#'+id).val(value);
		     return true;
	     }else{
		     $('#'+messageId).html("Value Should be Integer");
		     return false;
	     }
	}else{
		$('#'+messageId).html("Spaces Not Allowed");
		return false;
	}
  }else{
	  $('#'+messageId).html(" ");
  }
}

/*
Purpose: This function Checks the textfield value is Numaric or not
			
input: (id,MessageField Id, type)
Output: it Returns true if  textbox contain only even numbers return true
		else return false 
*/
//Only Even numbers
function numbervalidtonForTextTextArea(id, messageId, type){
	
	if($('#'+id).is("input")) {
		type = $('#'+id).attr('type');
	}else if($('#'+id).is("textarea")) {
		type = "textarea";
	}
	
	value = $('#'+id).val().trim();
	if(type == 'text' || type == 'textarea'){
		if(value != ""){
			if(value.indexOf(" ") == -1){
				if($.isNumeric($('#'+id).val())){
					$('#'+id).val(value);
				     return true;
				}else{
				   $('#'+messageId).html("<font color='red'>Value Should be Numeric</font>");
				    return false;
				}
			}else{
				$('#'+messageId).html("<font color='red'>Spaces Not Allowed</font>");
				return false;
			}
	    }else{
	    	$('#'+messageId).html("<font color='red'> Should not empty</font> ");
	    	return false;
	    }
    }
}
//optional Field 
function optionalFieldsCheck(id, messageId, type){
	value = $('#'+id).val().trim();
	if(value ==""){
		$('#'+messageId).html("Empty");
		return false;
	}else{
		if(value.indexOf(" ") == -1){
			$('#'+messageId).html("");
			$('#'+id).val(value);
			return true;
		}else{
			$('#'+messageId).html("Spaces Not Allowed");
			return false;
		}
	}
}

//optional Field With Spaces
function optionalFieldsCheckAllowSpaces(id, messageId, type){
	value = $('#'+id).val().trim();
	if(value ==""){
		$('#'+messageId).html("Empty");
		return false;
	}else{
		$('#'+messageId).html("");
		return true;
	}
}

/*
Purpose: This function Checks the textfield value begining and ending spaces exists and it contains alphabets or not
			
input: (id,MessageField Id, type)
Output: it Returns true if  textbox contain value begining and ending without spaces and its alphabets.
		else return false 
*/
//Only Begining and Ending Spaces With Alphabets or not Checking 
function beginingAndEndingSpacesCheckForAlphabets(id, messageId, type){
	
	value = $('#'+id).val().trim();
	var regex =  /^[A-Za-z\s]+$/;
	if(value != ""){
		    if(regex.test(value)){
		    	var res = value.match(/^\s+|\s+$/i);
		    	if(res!== null){
		    		$('#'+messageId).html("Spaces are not allowed at the begining and Ending");
		    		return false;
		    	}else{
		    		$('#'+messageId).html("");
		    		$('#'+id).val(value);
		    		return true;
		    	}
		    }else{
		    	$('#'+messageId).html("Please Enter Valid Characters.");
		    	return false;
		    }
     }else{
    	 $('#'+messageId).html(" ");
     }
}

// Allow With Spaces
function beginingAndEndingSpacesCheckForAlphabetsWithSpaces(id, messageId, type){
	
	value = $('#'+id).val().trim();
	var regex =  /^[A-Za-z\s]+$/;
    if(regex.test(value)){
    	$('#'+messageId).html(" ");
    	return true;
    }else{
    	$('#'+messageId).html("Please Enter Valid Characters.");
    	return false;
    }
     
}

/*
Purpose: This function Checks the textfield value begining and ending spaces exists or not
			
input: (id,MessageField Id, type)
Output: it Returns true if  textbox contain value begining and ending without spaces.
		else return false 
*/
//Only Begining and Ending Spaces Checking only
function bengingAndEndiningSpacesCheck(id, messageId, type){
	var value = $('#'+id).val().trim();
	if(value != ""){
	var res = value.match(/^\s+|\s+$/i);
		if(res!== null){
			$('#'+messageId).html("Spaces are not allowed at the begining and Ending");
			return false;
		}else{
			$('#'+messageId).html("");
			$('#'+id).val(value);
			return true;
		}
	}else{
		$('#'+messageId).html(" ");
	}
}

function uploadfileValidation(val, id, messageId, extension, size){
	var addrProof = false;
	var value = $('#'+id).val().trim();
	if(extension == ".jpeg" || extension == ".jpg" || extension == ".pdf"){
		var fileSize = val.files[0].size / 1024 / 1024; // in MB
    	fileSize = +(Math.round(fileSize + "e+2")  + "e-2");
    	  regex = new RegExp("(.*?)\.(jpg|jpeg|pdf)$");
//		  var extension = value.substring(value.lastIndexOf('.')+1);
    	  if (!(regex.test(value))) {
        	  $('#'+messageId).html("Uploaded File Format : "+extension+ "<br> Required Formats Are : .jpeg, .jpg and .pdf");
        	  addrProof = false;
          }else{
	        	if(fileSize <= size){
	    	    	$('#'+messageId).html("");
	    	    	addrProof = true;
	    	    }else{
	    	    	$('#'+messageId).html("Uploaded File Exceeded the Size:"+size+"MB <br> Present Uploaded File Size is : "+fileSize +"MB");
	    	    	addrProof = false;
	    	    }
          }
	}else{
		$('#'+messageId).html("Uploaded File Format : "+extension+ "<br> Required Formats Are : .jpeg, .jpg and .pdf");
		addrProof = false;
	}
	return addrProof;
	
}

// Method for Excedd limt of Characters in the given text fields.
//It was return if the length is lessthan given limt it will return true else return false
//you have to pass texfield id and message id and type  and which limit not exceeding.
function filedLengthChecking(id, messageId, type,size){
	var value = $('#'+id).val().trim();
	if(value != ""){
	 if(parseInt(value.length) <= size){
		   $('#'+messageId).html("");
		   return true;
	   }else{
		   $('#'+messageId).html("Exceeded (Max length : "+size+")");
			return false;
		}
	}
}

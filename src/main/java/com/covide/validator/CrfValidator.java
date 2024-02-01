package com.covide.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfMetaData;



public class CrfValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return CrfMetaData.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		try {
			CrfMetaData product = (CrfMetaData) target;
			if (product.getFile() == null || product.getFile().getOriginalFilename().equals("")) {
				errors.rejectValue("file", null, "Please select a file to upload!");
				return;
			}
			System.out.println("type:" + product.getFile().getContentType());
			if (!product.getFile().getContentType().equals("application/vnd.ms-excel")) {
				errors.rejectValue("file", null, "Please select an Excel file to upload!");
				return;
			}
		}catch (java.lang.ClassCastException e) {
			Crf product = (Crf) target;
			if (product.getFile() == null || product.getFile().getOriginalFilename().equals("")) {
				errors.rejectValue("file", null, "Please select a file to upload!");
				return;
			}
			System.out.println("type:" + product.getFile().getContentType());
			if (!product.getFile().getContentType().equals("application/vnd.ms-excel")) {
				errors.rejectValue("file", null, "Please select an Excel file to upload!");
				return;
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}

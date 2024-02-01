package com.springmvc.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.springmvc.model.RoleMaster;
import com.springmvc.service.RoleMasterService;

@Component
public class RoleToUserProfileConverter implements Converter<Object, RoleMaster>{
 
    static final Logger logger = LoggerFactory.getLogger(RoleToUserProfileConverter.class);
    
    @Autowired
    RoleMasterService roleMasterService;
    
    @Override
    public RoleMaster convert(Object element) {
        Long id = Long.parseLong((String)element);
        RoleMaster profile= roleMasterService.findById(id);
        logger.info("Profile : {}",profile);
        return profile;
    }

}

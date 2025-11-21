package org.cityu.service.impl;

import org.cityu.dao.CodeMapper;
import org.cityu.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CodeServiceImpl implements CodeService {

    @Autowired
    private CodeMapper codeMapper;

    @Override
    public List<Object> getUserRoles() {
        return codeMapper.getUserRoles();
    }
}

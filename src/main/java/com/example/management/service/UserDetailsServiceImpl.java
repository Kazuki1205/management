package com.example.management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.management.model.Employee;
import com.example.management.mapper.EmployeeMapper;

/**
 * ユーザー認証に使用するサービスクラス
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private EmployeeMapper employeeMapper;
    
    //　ログ出力
    protected static Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    /**
     * ログイン画面で入力された社員IDを、社員テーブルから取得する。
     * 無ければ例外(UsernameNotFoundException)を投げる。
     * 
     * @return employee 社員クラス
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	
    	log.debug("username={}", username);

        if (username == null || "".equals(username)) {
            throw new UsernameNotFoundException("Username is empty");
        }
        Employee employee = employeeMapper.findByUsername(username);

        return employee;
    }

}
package com.sign.signApi.user.service;

import com.sign.signApi.user.dao.UserDAO;
import com.sign.signApi.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
    private final UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User with username '" + username + "' not found"));

        return org.springframework.security.core.userdetails.User.withUsername(username).password(user.getPassword()).roles("USER").build();
    }
}

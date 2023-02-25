package com.my.selfimprovement.security;

import com.my.selfimprovement.service.UserService;
import com.my.selfimprovement.util.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            return new UserDetailsImpl(userService.findByEmail(email));
        } catch (UserNotFoundException ex) {
            throw new UsernameNotFoundException("User with email '" + email + "' not found.");
        }
    }

}

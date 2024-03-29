package rkostiuk.selfimprovement.security;

import rkostiuk.selfimprovement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class SpringUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new SpringUserDetails(userService.getByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email '" + email + "' not found.")));
    }

}

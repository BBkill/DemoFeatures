package org.aibles.failwall.authentication.payload;

import org.aibles.failwall.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserPrincipalService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> new UserPrincipal(user.getEmail(), user.getPassword()))
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException(email);
                });
    }

}

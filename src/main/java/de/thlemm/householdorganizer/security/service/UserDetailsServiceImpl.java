package de.thlemm.householdorganizer.security.service;

import de.thlemm.householdorganizer.model.User;
import de.thlemm.householdorganizer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
            if (user == null) throw new UsernameNotFoundException("User Not Found with username: " + username);
            return UserDetailsImpl.build(user);
    }
}

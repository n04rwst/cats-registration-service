package com.example.securityconfig;

import com.example.entity.Human;
import com.example.repository.HumanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private HumanRepository humanRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Human> human = humanRepository.findByUsername(username);
        return human.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found: " + username));
    }
}

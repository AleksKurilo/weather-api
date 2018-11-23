package com.akurilo.weatherapi.security.config;

import com.akurilo.weatherapi.security.PBKDF2Encoder;
import dto.UserDto;
import enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private PBKDF2Encoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //TODO integrate with weather-station service
        //ask actor
        //...
        UserDto userDto = new UserDto();
        userDto.setRole(UserRole.USER.getText());// from weather-station service
        userDto.setEmail("test@email.com");
        String pas = passwordEncoder.encode("user");
        userDto.setPassword(pas);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(userDto.getRole()));
        return new User(userDto.getEmail(), userDto.getPassword(), grantedAuthorities);
    }
}

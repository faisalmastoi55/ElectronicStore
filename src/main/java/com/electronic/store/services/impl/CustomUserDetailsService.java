package com.electronic.store.services.impl;

import com.electronic.store.entities.User;
import com.electronic.store.exceptions.ResourceNoteFoundException;
import com.electronic.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //fetching user from database
        User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNoteFoundException("User with given email not found !!"));
        return user;
    }
}

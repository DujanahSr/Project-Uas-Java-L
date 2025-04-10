package com.uas.java1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uas.java1.dto.DaftarUserDto;
import com.uas.java1.dto.UserDetailDto;
import com.uas.java1.model.Role;
import com.uas.java1.model.User;
import com.uas.java1.repository.RoleRepository;
import com.uas.java1.repository.UserRepository;
import com.uas.java1.util.PasswordUtil;

import java.time.LocalDate;
import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDetailDto daftar(DaftarUserDto dto) {

        if (!emailService.cekValidEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Format Email Tidak Valid");
        }

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username yang anda buat sudah digunakan");
        }

        Role userRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Peran Default User Tidak Ditemukan"));

        User user = User.builder()
                .username(dto.getUsername())
                .password(PasswordUtil.hash(dto.getPassword()))
                .status(true)
                .tanggalDibuat(LocalDate.now())
                .tanggalPembaruan(LocalDate.now())
                .email(dto.getEmail())
                .roles(Collections.singletonList(userRole))
                .build();

        User simpanUser = userRepository.save(user);

        return UserDetailDto.builder()
                .username(simpanUser.getUsername())
                .role(userRole.getRoleName())
                .build();
    }
}
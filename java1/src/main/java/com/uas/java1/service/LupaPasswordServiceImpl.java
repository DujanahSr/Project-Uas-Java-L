package com.uas.java1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.uas.java1.dto.LupaPasswordRequestDto;
import com.uas.java1.dto.OtpResetPasswordDto;
import com.uas.java1.dto.ResetPasswordDto;
import com.uas.java1.model.TokenResetPassword;
import com.uas.java1.model.User;
import com.uas.java1.repository.TokenResetPasswordRepository;
import com.uas.java1.repository.UserRepository;
import com.uas.java1.util.OtpUtil;
import com.uas.java1.util.PasswordUtil;

import java.time.LocalDateTime;
import java.util.Random;
import java.time.format.DateTimeFormatter;

@Service
public class LupaPasswordServiceImpl implements LupaPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenResetPasswordRepository TokenResetPasswordRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public void requestPasswordReset(LupaPasswordRequestDto request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pengguna Tidak Ditemukan"));

        TokenResetPasswordRepository.findByUsername(user.getUsername()).ifPresent(existingToken -> {
            if (existingToken.getTanggalKadaluarsa().isAfter(LocalDateTime.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "OTP Masih Berlaku, Silakan Periksa Kembali Email Anda");
            } else {
                TokenResetPasswordRepository.deleteByUsername(user.getUsername());
            }
        });

        String otp = String.format("%06d", new Random().nextInt(999999));
        String hashedOtp = OtpUtil.hashOtp(otp);

        TokenResetPassword tokenBaru = TokenResetPassword.builder()
                .username(user.getUsername())
                .token(hashedOtp)
                .tanggalKadaluarsa(LocalDateTime.now().plusMinutes(5))
                .build();

        TokenResetPasswordRepository.save(tokenBaru);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
        String formatTanggalKadaluarsa = tokenBaru.getTanggalKadaluarsa().format(formatter);

        emailService.kirimOtpEmail(user.getEmail(), "OTP Reset Password", otp, formatTanggalKadaluarsa,
                user.getUsername());
    }

    @Override
    public void resetPassword(ResetPasswordDto request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Tidak Ditemukan"));

        if (!PasswordUtil.check(request.getPasswordLama(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password Lama Yang Anda Masukkan Salah");
        }
        if (PasswordUtil.check(request.getPasswordBaru(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Password Yang Baru Tidak Boleh Sama Dengan Password Yang Lama");
        }

        user.setPassword(PasswordUtil.hash(request.getPasswordBaru()));
        userRepository.save(user);
    }

    @Override
    public void validasiOtpDanResetPassword(OtpResetPasswordDto dto) {

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Tidak Ditemukan"));

        TokenResetPassword token = TokenResetPasswordRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP Tidak Ditemukan"));

        if (token.getTanggalKadaluarsa().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP Sudah Kadaluarsa");
        }

        if (!OtpUtil.checkOtp(dto.getOtp(), token.getToken())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP Yang Anda Masukkan Tidak Valid");
        }

        if (PasswordUtil.check(dto.getPasswordBaru(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Password Yang Baru Tidak Boleh Sama Dengan Password Yang Lama");
        }

        user.setPassword(PasswordUtil.hash(dto.getPasswordBaru()));
        userRepository.save(user);

        TokenResetPasswordRepository.deleteByUsername(dto.getUsername());
    }
}
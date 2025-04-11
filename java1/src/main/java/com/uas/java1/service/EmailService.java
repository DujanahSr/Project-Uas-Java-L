package com.uas.java1.service;

public interface EmailService {
    void kirimEmail(String to, String subject, String body);
    boolean cekValidEmail(String email); 
    void kirimOtpEmail(String to, String subject, String otp, String expiredTime,String username);
}

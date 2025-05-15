package com.cg.siptracker.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private final Map<String, String> otpStorage = new HashMap<>();
    private final Map<String, LocalDateTime> otpExpiry = new HashMap<>();

    private static final int EXPIRE_MINUTES = 5;

    public String generateOtp(String email){
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        otpStorage.put(email, otp);
        otpExpiry.put(email, LocalDateTime.now().plusMinutes(EXPIRE_MINUTES));
        return otp;
    }

    public boolean validateOtp(String email, String otp){
        if(!otpStorage.containsKey(email)){
            return false;
        }
        String storedOtp = otpStorage.get(email);
        LocalDateTime expiryTime = otpExpiry.get(email);

        // Check otp is right and not expired yet
        if(storedOtp.equals(otp) && LocalDateTime.now().isBefore(expiryTime)){
            otpStorage.remove(email);
            otpExpiry.remove(email);
            return true;
        }
        return false;
    }

    public void clearOtp(String email){
        otpStorage.remove(email);
        otpExpiry.remove(email);
    }

}

package com.lovemesomecoding.entity.email;

import org.springframework.stereotype.Service;

import com.lovemesomecoding.entity.user.User;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImp implements EmailService {

    @Override
    public boolean sendMonthlyPaymentReminder(User user) {
        log.info("sendMonthlyPaymentReminder to {}", user.getEmail());
        return false;

    }

}

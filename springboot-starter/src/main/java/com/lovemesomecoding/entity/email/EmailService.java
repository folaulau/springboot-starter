package com.lovemesomecoding.entity.email;

import com.lovemesomecoding.entity.user.User;

public interface EmailService {

    boolean sendMonthlyPaymentReminder(User user);

}

package com.lovemesomecoding.batch.log;

public enum JobType {

    /**
     * Send (5 days before)reminder for monthly subscription
     */
    SEND_PAYMENT_REMINDER_EMAIL,

    /**
     * Charge member for monthly subscription
     */
    CHARGE_MONTHLY_SUBSCRIPTION;

    public static final String SEND_PAYMENT_REMINDER_EMAIL_BEAN = SEND_PAYMENT_REMINDER_EMAIL.name();

    public static final String CHARGE_MONTHLY_SUBSCRIPTION_BEAN = CHARGE_MONTHLY_SUBSCRIPTION.name();
}

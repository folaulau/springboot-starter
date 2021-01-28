package com.lovemesomecoding.entity.listener;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import com.lovemesomecoding.entity.user.User;

@Slf4j
@Component
public class UserListener {

    public void insert(User user) {
        // log.debug("insert(..)");
    }

    public void update(User user) {
        // log.debug("update(..)");
    }

    public void delete(User user) {
        // log.debug("delete(..)");
    }

}

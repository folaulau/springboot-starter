package com.lovemesomecoding.entity.user.session;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lovemesomecoding.dto.CustomPage;

@Service
public class UserSessionServiceImp implements UserSessionService {
    private Logger                log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserSessionRepository userSessionRepository;

    private UserSession save(UserSession userSession) {
        return this.userSessionRepository.saveAndFlush(userSession);
    }

    @Override
    public boolean signIn(UserSession memberSession) {
        log.debug("signIn(..)");
        memberSession.setActive(true);
        memberSession.setLoginTime(new Date());

        memberSession = this.save(memberSession);
        return true;
    }

    @Override
    public boolean signOut(String authToken) {
        log.debug("signOut(..)");
        UserSession memberSession = userSessionRepository.findByAuthToken(authToken);

        if (memberSession != null && memberSession.getId() != null && memberSession.getId() > 0) {
            memberSession.setLogoutTime(new Date());
            memberSession.setActive(false);
            this.save(memberSession);
            return true;
        }
        return false;

    }

    @Override
    public void expire(String authToken) {
        UserSession memberSession = userSessionRepository.findByAuthToken(authToken);
        if (memberSession != null && memberSession.getId() != null && memberSession.getId() > 0) {
            memberSession.setExpired(new Date());
            memberSession.setActive(false);
            this.save(memberSession);
        }
    }

    @Override
    public Page<UserSession> getSessionsByUserId(Long userId, Pageable pageable) {
        // TODO Auto-generated method stub
        return userSessionRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<UserSession> getActiveSessionsByUserId(Long userId, Pageable pageable) {
        // TODO Auto-generated method stub
        return userSessionRepository.findByUserIdAndActive(userId, true, pageable);
    }

    @Override
    public Page<UserSession> getSessionsByUserUuid(String userUuid, Pageable pageable) {
        // TODO Auto-generated method stub
        return userSessionRepository.findByUserUuid(userUuid, pageable);
    }

    @Override
    public Page<UserSession> getActiveSessionsByUserUuid(String userUuid, Pageable pageable) {
        // TODO Auto-generated method stub
        return userSessionRepository.findByUserUuidAndActive(userUuid, true, pageable);
    }
}

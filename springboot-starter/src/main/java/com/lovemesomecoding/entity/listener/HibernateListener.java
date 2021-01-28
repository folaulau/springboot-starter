package com.lovemesomecoding.entity.listener;

import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lovemesomecoding.enitity.user.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HibernateListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

    private static final long serialVersionUID = 1L;

    @Autowired
    private UserListener      userListener;

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        // log.info("requiresPostCommitHanding(..)");
        // log.info("ENTITY NAME: {}", persister.getEntityName());
        return false;
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        Object entity = event.getEntity();
        if (entity.getClass().equals(User.class)) {
            this.userListener.insert((User) event.getEntity());
        } else {
            // log.debug("class not found");
        }
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        Object entity = event.getEntity();
        if (entity.getClass().equals(User.class)) {
            this.userListener.update((User) event.getEntity());
        } else {
            // log.debug("class not found");
        }
    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        Object entity = event.getEntity();
        if (entity.getClass().equals(User.class)) {
            this.userListener.delete((User) event.getEntity());
        } else {
            // log.debug("class not found");
        }
    }

}

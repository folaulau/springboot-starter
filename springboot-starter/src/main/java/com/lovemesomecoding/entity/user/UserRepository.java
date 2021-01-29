package com.lovemesomecoding.entity.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaRepository<User, Long> {

    User findByUuid(String uuid);

    User findByEmail(String email);

    Page<User> findByStatus(UserStatus status, Pageable pageable);

}

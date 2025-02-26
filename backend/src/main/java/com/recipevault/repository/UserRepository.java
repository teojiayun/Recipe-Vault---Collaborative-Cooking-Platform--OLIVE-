package com.recipevault.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipevault.model.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByUsername(String username);
}

package com.vlados.repository;

import com.vlados.entity.Role;
import com.vlados.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findAll();

    @Transactional
    @Modifying
    @Query("update users u set u.locked = true where u.username = :username")
    void lockUser(@Param(value = "username") String username);

    @Transactional
    @Modifying
    @Query("update users u set u.locked = false where u.username = :username")
    void unlockUser(@Param(value = "username") String username);
}

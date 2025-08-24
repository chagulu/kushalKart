package com.kushalkart.repository;

import com.kushalkart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByMobile(String mobile);

    @Query("SELECT a.pincode FROM User u JOIN u.address a WHERE u.id = :userId")
    Optional<String> findPincodeByUserId(@Param("userId") Long userId);

}


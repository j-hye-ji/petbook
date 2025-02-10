package com.hyeji.petbook.repository;

import com.hyeji.petbook.entity.User;
import com.hyeji.petbook.type.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUserRole(UserRole userRole);
}

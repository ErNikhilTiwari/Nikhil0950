package com.cg.siptracker.repository;

import com.cg.siptracker.model.Role;
import com.cg.siptracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    Optional<User> findByEmailAndRole(String email, Role role);

}

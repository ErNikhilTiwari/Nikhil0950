package com.cg.siptracker.repository;

import com.cg.siptracker.model.SIP;
import com.cg.siptracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SIPRepository extends JpaRepository<SIP,Long> {
    List<SIP> findByUser(User user);

    List<SIP> findByUserEmail(String email);

}

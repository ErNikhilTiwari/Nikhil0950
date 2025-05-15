package com.cg.siptracker.service;

import com.cg.siptracker.dto.ResponseDTO;
import com.cg.siptracker.dto.SipDTO;
import com.cg.siptracker.exception.ResourceNotFoundException;
import com.cg.siptracker.model.SIP;
import com.cg.siptracker.model.User;
import com.cg.siptracker.repository.SIPRepository;
import com.cg.siptracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class SIPServiceImpl implements SIPService {

    @Autowired
    private SIPRepository sipRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseDTO addSIP(SipDTO dto, String email) {
        log.info("Adding SIP for user {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        SIP sip = new SIP();
        sip.setFundName(dto.getFundName());
        sip.setAmount(dto.getAmount());
        sip.setFrequency(dto.getFrequency());
        sip.setStartDate(dto.getStartDate());
        sip.setUser(user);

        SIP saved = sipRepository.save(sip);

        Map<String, Object> response = new HashMap<>();
        response.put("sip_unique_id", saved.getSip_id());
        response.put("fundName", saved.getFundName());
        response.put("frequency", saved.getFrequency());
        response.put("amount", saved.getAmount());
        response.put("startDate", saved.getStartDate());

        return new ResponseDTO("SIP added successfully", response);
    }

    @Override
    public ResponseDTO updateSIP(Long sipId, SipDTO dto, String email) {
        log.info("Updating SIP {} for user {}", sipId, email);
        SIP sip = sipRepository.findById(sipId)
                .orElseThrow(() -> new ResourceNotFoundException("SIP not found with ID: " + sipId));

        if (!sip.getUser().getEmail().equals(email)) {
            throw new ResourceNotFoundException("You do not have permission to update this SIP");
        }

        sip.setFundName(dto.getFundName());
        sip.setAmount(dto.getAmount());
        sip.setFrequency(dto.getFrequency());
        sip.setStartDate(dto.getStartDate());

        SIP updated = sipRepository.save(sip);

        Map<String, Object> response = new HashMap<>();
        response.put("sip_unique_id", updated.getSip_id());
        response.put("fundName", updated.getFundName());
        response.put("frequency", updated.getFrequency());
        response.put("amount", updated.getAmount());
        response.put("startDate", updated.getStartDate());

        return new ResponseDTO("SIP updated successfully", response);
    }

    @Override
    public ResponseDTO deleteSIP(Long sipId, String email) {
        log.info("Deleting SIP {} for user {}", sipId, email);
        SIP sip = sipRepository.findById(sipId)
                .orElseThrow(() -> new ResourceNotFoundException("SIP not found with ID: " + sipId));

        if (!sip.getUser().getEmail().equals(email)) {
            throw new ResourceNotFoundException("You do not have permission to delete this SIP");
        }

        sipRepository.deleteById(sipId);
        return new ResponseDTO("SIP deleted successfully", sipId);
    }

    @Override
    public ResponseDTO getSIPById(Long sipId, String email) {
        log.info("Fetching SIP {} for user {}", sipId, email);
        SIP sip = sipRepository.findById(sipId)
                .orElseThrow(() -> new ResourceNotFoundException("SIP not found with ID: " + sipId));

        if (!sip.getUser().getEmail().equals(email)) {
            throw new ResourceNotFoundException("You do not have permission to view this SIP");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("sip_unique_id", sip.getSip_id());
        response.put("fundName", sip.getFundName());
        response.put("frequency", sip.getFrequency());
        response.put("amount", sip.getAmount());
        response.put("startDate", sip.getStartDate());

        return new ResponseDTO("SIP fetched successfully", response);
    }

    @Override
    public ResponseDTO getSIPsByUser(String email) {
        log.info("Fetching all SIPs for user {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        List<SIP> sips = sipRepository.findByUser(user);
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (SIP sip : sips) {
            Map<String, Object> map = new HashMap<>();
            map.put("sip_unique_id", sip.getSip_id());
            map.put("fundName", sip.getFundName());
            map.put("frequency", sip.getFrequency());
            map.put("amount", sip.getAmount());
            map.put("startDate", sip.getStartDate());
            responseList.add(map);
        }

        return new ResponseDTO("All SIPs for user", responseList);
    }
}

package com.cg.siptracker.service;

import com.cg.siptracker.dto.ResponseDTO;
import com.cg.siptracker.dto.SipDTO;

public interface SIPService {

    ResponseDTO addSIP(SipDTO dto, String email);
    ResponseDTO updateSIP(Long sipId, SipDTO dto, String email);
    ResponseDTO deleteSIP(Long sipId, String email);
    ResponseDTO getSIPById(Long sipId, String email);
    ResponseDTO getSIPsByUser(String email);

}

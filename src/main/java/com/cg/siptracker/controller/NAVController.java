package com.cg.siptracker.controller;

import com.cg.siptracker.dto.ResponseDTO;
import com.cg.siptracker.service.INAVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nav")
public class NAVController {

    @Autowired
    private INAVService navService;

    @GetMapping("/get")
    public ResponseEntity<ResponseDTO> getBySchemeName(@RequestParam String fundName, @RequestHeader("Authorization") String token) {
        ResponseDTO response = navService.getNAVHistory(fundName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fetch")
    public ResponseEntity<ResponseDTO> fetchNAVs(@RequestHeader("Authorization") String token) {
        try {
            ResponseDTO response = navService.fetchAndStoreNAVs();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ResponseDTO("Error fetching NAVs", e.getMessage()));
        }
    }
}

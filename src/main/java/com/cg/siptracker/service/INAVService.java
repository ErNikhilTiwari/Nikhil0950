package com.cg.siptracker.service;

import com.cg.siptracker.dto.ResponseDTO;

import java.io.IOException;

public interface INAVService {
    ResponseDTO fetchAndStoreNAVs() throws IOException;

    ResponseDTO getNAVHistory(String fundName);
}

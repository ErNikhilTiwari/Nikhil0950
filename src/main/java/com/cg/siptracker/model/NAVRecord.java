package com.cg.siptracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nav_records")
public class NAVRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nav_id;

    private String fundName;

    private Double navValue;

    private LocalDate date;

    private LocalDateTime timestampFetched;


    NAVRecord(String fundName, Double navValue, LocalDate date) {
    this.fundName =fundName;
    this.navValue = navValue;
    this.date=date;
    }
}
package com.cg.siptracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "sips")
@Getter
@Setter
@NoArgsConstructor
public class SIP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sip_id;

    private String fundName;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private double amount;

    private LocalDate startDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public SIP(Long sip_id, String fundName, Frequency frequency, double amount, LocalDate startDate) {
        this.sip_id = sip_id;
        this.fundName = fundName;
        this.frequency = frequency;
        this.amount = amount;
        this.startDate = startDate;
    }



}
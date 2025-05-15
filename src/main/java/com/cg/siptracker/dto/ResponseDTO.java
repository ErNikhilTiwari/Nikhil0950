package com.cg.siptracker.dto;
import lombok.AllArgsConstructor;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseDTO {

    private String message;
    private Object data;

}

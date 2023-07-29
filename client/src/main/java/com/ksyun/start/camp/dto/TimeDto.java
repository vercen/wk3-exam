package com.ksyun.start.camp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author vercen
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeDto {
    private Date result;
    private String serviceId;
}

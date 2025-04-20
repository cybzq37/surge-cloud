package com.surge.device.domain.timerules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeRange {

    @NotBlank(message = "start time cannot be null")
    private String start;

    @NotBlank(message = "end time cannot be null")
    private String end;
}
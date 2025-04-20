package com.surge.device.domain.timerules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeRangeRule {

    @NotBlank(message = "type cannot be null")
    private String type;

    private List<Integer> daysOfWeek = new ArrayList<>();

    private List<Integer> months = new ArrayList<>();
    private List<Integer> daysOfMonth = new ArrayList<>();

    private List<TimeRange> timeRanges = new ArrayList<>();

}
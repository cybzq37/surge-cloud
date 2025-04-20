package com.surge.device.domain.ljyard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RowData {
    private Integer rowNo;

    private List<BayData> bays = new ArrayList<>();
    @JsonIgnore
    private transient Map<Integer, BayData> bayDataMap = new HashMap<>();
}

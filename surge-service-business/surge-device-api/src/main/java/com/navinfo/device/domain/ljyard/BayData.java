package com.surge.device.domain.ljyard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class BayData {
    private Integer bayNo;

    private Integer tierSize;
    private List<TierData> tiers = new ArrayList<>();
    @JsonIgnore
    private transient Map<Integer, TierData> tierDataMap = new HashMap<>();

}

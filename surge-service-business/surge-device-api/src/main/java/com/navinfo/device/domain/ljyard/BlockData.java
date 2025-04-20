package com.surge.device.domain.ljyard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class BlockData {
    private String blockNo;
    private Integer bayQty;
    private Integer rowQty;
    private Integer tierQty;
    private Integer firstBayNo;
    private Integer lastBayNo;
    private Integer firstRowNo;
    private Integer lastRowNo;
    private Integer firstTierNo;
    private Integer lastTierNo;

    private List<RowData> rows = new ArrayList<>();
    @JsonIgnore
    private transient Map<Integer, RowData> rowDataMap = new HashMap<>();
}

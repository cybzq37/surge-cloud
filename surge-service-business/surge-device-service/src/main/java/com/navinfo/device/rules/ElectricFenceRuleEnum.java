package com.surge.device.rules;

public enum ElectricFenceRuleEnum {

    NO_ENTRY("1"), // 禁止进入
    NO_EXIT("2"),  // 禁止离开
    ;

    private String type;

    ElectricFenceRuleEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

package com.surge.device.rules;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ElectricFenceRuleContext {

    private IElectricFenceRule electricFenceRule;

    private static HashMap<String, IElectricFenceRule> RULE_MAP = new HashMap<>();

    static {
        RULE_MAP.put(ElectricFenceRuleEnum.NO_ENTRY.getType(), new NoEntryRule());
        RULE_MAP.put(ElectricFenceRuleEnum.NO_EXIT.getType(), new NoExitRule());
    }

    public static IElectricFenceRule getRuleByType(ElectricFenceRuleEnum ruleType) {
        IElectricFenceRule rule =  RULE_MAP.get(ruleType.getType());
        return rule;
    }

}

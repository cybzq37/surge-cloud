package com.surge.device.domain.timerules;

import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.utils.JsonUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TimeRuleChecker {

    private static final String TIME_FORMAT = "HH:mm";

    public static boolean validateJsonRules(String jsonRules) throws Exception {
            List<TimeRangeRule> rule = JsonUtils.parseArray(jsonRules, TimeRangeRule.class);;
            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            Set<ConstraintViolation<List<TimeRangeRule>>> violations = validator.validate(rule);

            if (violations.isEmpty()) {
                return true;
            } else {
                StringBuilder sb = new StringBuilder("Validation errors:");
                for (ConstraintViolation<List<TimeRangeRule>> violation : violations) {
                    sb.append(violation.getPropertyPath() + ": " + violation.getMessage());
                }
                throw new ServiceException(sb.toString());
            }
    }

    public static boolean isWithinRules(String jsonRules, Date date) {
        List<TimeRangeRule> rules = JsonUtils.parseArray(jsonRules, TimeRangeRule.class);

        // 转换 Date 到 LocalDateTime
        LocalDateTime dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDate localDate = dateTime.toLocalDate();
        LocalTime localTime = dateTime.toLocalTime();
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        Month month = dateTime.getMonth();

        for (TimeRangeRule rule : rules) {
            String type = rule.getType();
            List<TimeRange> timeRanges = rule.getTimeRanges();

            if ("daily".equalsIgnoreCase(type)) {
                if (isWithinTimeRanges(localTime, timeRanges)) {
                    return true;
                }
            } else if ("week".equalsIgnoreCase(type)) {
                List<Integer> daysOfWeek = rule.getDaysOfWeek();
                if (daysOfWeek.contains(dayOfWeek.getValue()) && isWithinTimeRanges(localTime, timeRanges)) {
                    return true;
                }
            } else if ("month".equalsIgnoreCase(type)) {
                List<Integer> months = rule.getMonths();
                List<Integer> daysOfMonth = rule.getDaysOfMonth();
                if (months.contains(month.getValue()) && daysOfMonth.contains(localDate.getDayOfMonth())
                        && isWithinTimeRanges(localTime, timeRanges)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isWithinTimeRanges(LocalTime time, List<TimeRange> timeRanges) {
        for (TimeRange range : timeRanges) {
            LocalTime start = LocalTime.parse(range.getStart(), DateTimeFormatter.ofPattern(TIME_FORMAT));
            LocalTime end = LocalTime.parse(range.getEnd(), DateTimeFormatter.ofPattern(TIME_FORMAT));
            if (!time.isBefore(start) && !time.isAfter(end)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        String jsonRules = "[\n" +
                "    {\n" +
                "        \"type\": \"daily\",\n" +
                "        \"timeRanges\": [\n" +
                "            {\n" +
                "                \"start\": \"09:00\",\n" +
                "                \"end\": \"18:00\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"type\": \"week\",\n" +
                "        \"daysOfWeek\": [\n" +
                "            1,\n" +
                "            2,\n" +
                "            3,\n" +
                "            4,\n" +
                "            5\n" +
                "        ],\n" +
                "        \"timeRanges\": [\n" +
                "            {\n" +
                "                \"start\": \"08:00\",\n" +
                "                \"end\": \"12:00\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"start\": \"14:00\",\n" +
                "                \"end\": \"17:30\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"type\": \"month\",\n" +
                "        \"months\": [\n" +
                "            1,\n" +
                "            12\n" +
                "        ],\n" +
                "        \"daysOfMonth\": [\n" +
                "            1,\n" +
                "            15\n" +
                "        ],\n" +
                "        \"timeRanges\": [\n" +
                "            {\n" +
                "                \"start\": \"10:00\",\n" +
                "                \"end\": \"16:00\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "]";

        // 测试日期
        Date testDate = new Date(); // 当前时间
        System.out.println("当前时间是否符合规则: " + isWithinRules(jsonRules, testDate));
    }
}

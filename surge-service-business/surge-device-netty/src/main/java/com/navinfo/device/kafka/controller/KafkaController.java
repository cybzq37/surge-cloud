package com.surge.device.kafka.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.surge.common.core.result.R;
import com.surge.device.api.RemoteDeviceInstanceService;
import com.surge.device.api.RemoteTraceDataProcessorApi;
import com.surge.device.domain.kafka.KafkaRtpd;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/kafka")
public class KafkaController {
    @DubboReference
    public RemoteTraceDataProcessorApi remoteTraceDataProcessorApi;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/send")
    public R sendMessage(@RequestBody JsonNode payload) {
        kafkaTemplate.send(payload.get("topic").asText(),
                payload.get("message").toString());
        return R.ok();
    }

}

CREATE TABLE public.iot_device (
    id VARCHAR(50) PRIMARY KEY,                     -- 设备唯一ID (例如：IMEI, MAC地址, UUID)
    device_name VARCHAR(100) NOT NULL,              -- 设备名称 (用户自定义)
    device_model VARCHAR(50),                       -- 设备型号
    manufacturer VARCHAR(100),                      -- 制造商
    serial_number VARCHAR(100) UNIQUE,             -- 设备序列号
    device_type VARCHAR(50) NOT NULL,               -- 设备类型 (例如：sensor, actuator, gateway)
    protocol_type VARCHAR(50) NOT NULL,            -- 通信协议类型 (例如：MQTT, HTTP, CoAP)
    status VARCHAR(20) DEFAULT 'offline',           -- 设备状态 (online, offline, active, inactive, error)
    firmware_version VARCHAR(50),                   -- 固件版本
    software_version VARCHAR(50),                   -- 软件版本
    registration_time TIMESTAMP(6) DEFAULT NOW(),   -- 注册时间
    last_online_time TIMESTAMP(6),                  -- 最后上线时间
    last_offline_time TIMESTAMP(6),                 -- 最后下线时间
    location_longitude DOUBLE PRECISION,            -- 经度
    location_latitude DOUBLE PRECISION,             -- 纬度
    time_zone VARCHAR(50),                          -- 时区
    description TEXT,                               -- 设备描述
    gateway_id VARCHAR(50) REFERENCES public.iot_device(id) ON DELETE SET NULL, -- 所属网关ID (如果适用)
    org_id VARCHAR(50) REFERENCES public.sys_org(id) ON DELETE SET NULL,        -- 所属组织机构ID (如果需要)
    user_id VARCHAR(50) REFERENCES public.sys_user(id) ON DELETE SET NULL,      -- 设备所有者ID (如果需要)
    created_time TIMESTAMP(6) DEFAULT NOW(),
    updated_time TIMESTAMP(6) DEFAULT NOW(),
    remark VARCHAR(256)
);

COMMENT ON TABLE public.iot_device IS '物联网设备主表';
COMMENT ON COLUMN public.iot_device.id IS '设备唯一ID';
COMMENT ON COLUMN public.iot_device.device_name IS '设备名称';
COMMENT ON COLUMN public.iot_device.device_model IS '设备型号';
COMMENT ON COLUMN public.iot_device.manufacturer IS '制造商';
COMMENT ON COLUMN public.iot_device.serial_number IS '设备序列号';
COMMENT ON COLUMN public.iot_device.device_type IS '设备类型';
COMMENT ON COLUMN public.iot_device.protocol_type IS '通信协议类型';
COMMENT ON COLUMN public.iot_device.status IS '设备状态';
COMMENT ON COLUMN public.iot_device.firmware_version IS '固件版本';
COMMENT ON COLUMN public.iot_device.software_version IS '软件版本';
COMMENT ON COLUMN public.iot_device.registration_time IS '注册时间';
COMMENT ON COLUMN public.iot_device.last_online_time IS '最后上线时间';
COMMENT ON COLUMN public.iot_device.last_offline_time IS '最后下线时间';
COMMENT ON COLUMN public.iot_device.location_longitude IS '经度';
COMMENT ON COLUMN public.iot_device.location_latitude IS '纬度';
COMMENT ON COLUMN public.iot_device.time_zone IS '时区';
COMMENT ON COLUMN public.iot_device.description IS '设备描述';
COMMENT ON COLUMN public.iot_device.gateway_id IS '所属网关ID';
COMMENT ON COLUMN public.iot_device.org_id IS '所属组织机构ID';
COMMENT ON COLUMN public.iot_device.user_id IS '设备所有者ID';
COMMENT ON COLUMN public.iot_device.created_time IS '创建时间';
COMMENT ON COLUMN public.iot_device.updated_time IS '更新时间';
COMMENT ON COLUMN public.iot_device.remark IS '备注';

CREATE INDEX idx_iot_device_device_type ON public.iot_device(device_type);
CREATE INDEX idx_iot_device_protocol_type ON public.iot_device(protocol_type);
CREATE INDEX idx_iot_device_status ON public.iot_device(status);
CREATE INDEX idx_iot_device_serial_number ON public.iot_device(serial_number);
CREATE INDEX idx_iot_device_gateway_id ON public.iot_device(gateway_id);
CREATE INDEX idx_iot_device_org_id ON public.iot_device(org_id);
CREATE INDEX idx_iot_device_user_id ON public.iot_device(user_id);



CREATE TABLE public.iot_device_property (
    id VARCHAR(50) PRIMARY KEY,
    device_model VARCHAR(50) NOT NULL,           -- 设备型号 (用于定义该型号设备的通用属性)
    property_name VARCHAR(100) NOT NULL,         -- 属性名称 (例如：temperature, humidity)
    property_data_type VARCHAR(50) NOT NULL,    -- 属性数据类型 (例如：integer, float, boolean, string)
    property_unit VARCHAR(50),                   -- 属性单位 (例如：°C, %, Pa)
    read_only BOOLEAN DEFAULT FALSE,             -- 是否只读
    write_only BOOLEAN DEFAULT FALSE,            -- 是否只写
    description TEXT,                           -- 属性描述
    created_time TIMESTAMP(6) DEFAULT NOW(),
    updated_time TIMESTAMP(6) DEFAULT NOW(),
    UNIQUE (device_model, property_name)
);

COMMENT ON TABLE public.iot_device_property IS '设备型号属性定义表';
COMMENT ON COLUMN public.iot_device_property.id IS '主键';
COMMENT ON COLUMN public.iot_device_property.device_model IS '设备型号';
COMMENT ON COLUMN public.iot_device_property.property_name IS '属性名称';
COMMENT ON COLUMN public.iot_device_property.property_data_type IS '属性数据类型';
COMMENT ON COLUMN public.iot_device_property.property_unit IS '属性单位';
COMMENT ON COLUMN public.iot_device_property.read_only IS '是否只读';
COMMENT ON COLUMN public.iot_device_property.write_only IS '是否只写';
COMMENT ON COLUMN public.iot_device_property.description IS '属性描述';
COMMENT ON COLUMN public.iot_device_property.created_time IS '创建时间';
COMMENT ON COLUMN public.iot_device_property.updated_time IS '更新时间';

CREATE INDEX idx_iot_device_property_device_model ON public.iot_device_property(device_model);
CREATE INDEX idx_iot_device_property_property_name ON public.iot_device_property(property_name);


CREATE TABLE public.iot_device_config (
    id VARCHAR(50) PRIMARY KEY,
    device_id VARCHAR(50) NOT NULL REFERENCES public.iot_device(id) ON DELETE CASCADE,
    config_key VARCHAR(100) NOT NULL,             -- 配置项键
    config_value TEXT,                            -- 配置项值 (JSON 格式或其他)
    effective_time TIMESTAMP(6) DEFAULT NOW(),    -- 配置生效时间
    created_time TIMESTAMP(6) DEFAULT NOW(),
    updated_time TIMESTAMP(6) DEFAULT NOW(),
    UNIQUE (device_id, config_key)
);

COMMENT ON TABLE public.iot_device_config IS '设备配置表';
COMMENT ON COLUMN public.iot_device_config.id IS '主键';
COMMENT ON COLUMN public.iot_device_config.device_id IS '设备ID';
COMMENT ON COLUMN public.iot_device_config.config_key IS '配置项键';
COMMENT ON COLUMN public.iot_device_config.config_value IS '配置项值';
COMMENT ON COLUMN public.iot_device_config.effective_time IS '配置生效时间';
COMMENT ON COLUMN public.iot_device_config.created_time IS '创建时间';
COMMENT ON COLUMN public.iot_device_config.updated_time IS '更新时间';

CREATE INDEX idx_iot_device_config_device_id ON public.iot_device_config(device_id);
CREATE INDEX idx_iot_device_config_config_key ON public.iot_device_config(config_key);


CREATE TABLE public.iot_device_data (
    id BIGSERIAL PRIMARY KEY,                     -- 数据ID (自增)
    device_id VARCHAR(50) NOT NULL REFERENCES public.iot_device(id) ON DELETE CASCADE,
    timestamp TIMESTAMP(6) NOT NULL,              -- 数据上报时间 (设备本地时间或平台接收时间)
    payload JSONB NOT NULL,                       -- 原始数据载荷 (JSON 格式，包含各种属性值)
    -- 可以考虑将常用属性单独提取为列，以提高查询效率 (例如：temperature FLOAT, humidity FLOAT)
    created_time TIMESTAMP(6) DEFAULT NOW()       -- 平台接收时间
);

COMMENT ON TABLE public.iot_device_data IS '设备上报数据表';
COMMENT ON COLUMN public.iot_device_data.id IS '数据ID';
COMMENT ON COLUMN public.iot_device_data.device_id IS '设备ID';
COMMENT ON COLUMN public.iot_device_data.timestamp IS '数据上报时间';
COMMENT ON COLUMN public.iot_device_data.payload IS '原始数据载荷 (JSON)';
COMMENT ON COLUMN public.iot_device_data.created_time IS '平台接收时间';

CREATE INDEX idx_iot_device_data_device_id_timestamp ON public.iot_device_data(device_id, timestamp DESC);
CREATE INDEX idx_iot_device_data_timestamp ON public.iot_device_data(timestamp DESC);
-- 如果提取了常用属性，也为这些属性创建索引
-- CREATE INDEX idx_iot_device_data_temperature ON public.iot_device_data(temperature);
-- CREATE INDEX idx_iot_device_data_humidity ON public.iot_device_data(humidity);


CREATE TABLE public.iot_rule (
    id VARCHAR(50) PRIMARY KEY,
    rule_name VARCHAR(100) NOT NULL UNIQUE,      -- 规则名称
    description TEXT,                           -- 规则描述
    rule_expression TEXT NOT NULL,              -- 规则表达式 (例如：SQL, Drools 语法)
    trigger_event VARCHAR(50) NOT NULL,         -- 触发事件 (例如：device_data, device_status_change)
    status VARCHAR(20) DEFAULT 'active',        -- 规则状态 (active, inactive, draft)
    created_time TIMESTAMP(6) DEFAULT NOW(),
    updated_time TIMESTAMP(6) DEFAULT NOW()
);

COMMENT ON TABLE public.iot_rule IS '规则引擎规则定义表';
COMMENT ON COLUMN public.iot_rule.id IS '规则ID';
COMMENT ON COLUMN public.iot_rule.rule_name IS '规则名称';
COMMENT ON COLUMN public.iot_rule.description IS '规则描述';
COMMENT ON COLUMN public.iot_rule.rule_expression IS '规则表达式';
COMMENT ON COLUMN public.iot_rule.trigger_event IS '触发事件';
COMMENT ON COLUMN public.iot_rule.status IS '规则状态';
COMMENT ON COLUMN public.iot_rule.created_time IS '创建时间';
COMMENT ON COLUMN public.iot_rule.updated_time IS '更新时间';

CREATE INDEX idx_iot_rule_rule_name ON public.iot_rule(rule_name);
CREATE INDEX idx_iot_rule_status ON public.iot_rule(status);
CREATE INDEX idx_iot_rule_trigger_event ON public.iot_rule(trigger_event);


CREATE TABLE public.iot_rule_action (
    id VARCHAR(50) PRIMARY KEY,
    rule_id VARCHAR(50) NOT NULL REFERENCES public.iot_rule(id) ON DELETE CASCADE,
    action_type VARCHAR(50) NOT NULL,            -- 动作类型 (例如：send_email, send_sms, trigger_webhook, update_device_config)
    action_config JSONB,                         -- 动作配置 (例如：邮件接收人, 短信模板, Webhook URL)
    execution_order INTEGER DEFAULT 1,          -- 动作执行顺序
    created_time TIMESTAMP(6) DEFAULT NOW(),
    updated_time TIMESTAMP(6) DEFAULT NOW(),
    UNIQUE (rule_id, action_type, execution_order)
);

COMMENT ON TABLE public.iot_rule_action IS '规则引擎动作定义表';
COMMENT ON COLUMN public.iot_rule_action.id IS '动作ID';
COMMENT ON COLUMN public.iot_rule_action.rule_id IS '规则ID';
COMMENT ON COLUMN public.iot_rule_action.action_type IS '动作类型';
COMMENT ON COLUMN public.iot_rule_action.action_config IS '动作配置 (JSON)';
COMMENT ON COLUMN public.iot_rule_action.execution_order IS '动作执行顺序';
COMMENT ON COLUMN public.iot_rule_action.created_time IS '创建时间';
COMMENT ON COLUMN public.iot_rule_action.updated_time IS '更新时间';

CREATE INDEX idx_iot_rule_action_rule_id ON public.iot_rule_action(rule_id);
CREATE INDEX idx_iot_rule_action_action_type ON public.iot_rule_action(action_type);


CREATE TABLE public.iot_alert_rule (
    id VARCHAR(50) PRIMARY KEY,
    rule_name VARCHAR(100) NOT NULL UNIQUE,      -- 告警规则名称
    description TEXT,                           -- 告警规则描述
    device_id VARCHAR(50) REFERENCES public.iot_device(id) ON DELETE SET NULL, -- 关联设备ID (可以针对特定设备或所有设备)
    property_name VARCHAR(100),                 -- 监控的属性名称 (如果针对特定属性)
    condition_expression TEXT NOT NULL,         -- 告警触发条件表达式 (例如：temperature > 50)
    severity VARCHAR(20) NOT NULL,              -- 告警级别 (critical, major, minor, warning, info)
    enabled BOOLEAN DEFAULT TRUE,               -- 是否启用
    created_time TIMESTAMP(6) DEFAULT NOW(),
    updated_time TIMESTAMP(6) DEFAULT NOW()
);

COMMENT ON TABLE public.iot_alert_rule IS '告警规则定义表';
COMMENT ON COLUMN public.iot_alert_rule.id IS '告警规则ID';
COMMENT ON COLUMN public.iot_alert_rule.rule_name IS '告警规则名称';
COMMENT ON COLUMN public.iot_alert_rule.description IS '告警规则描述';
COMMENT ON COLUMN public.iot_alert_rule.device_id IS '关联设备ID';
COMMENT ON COLUMN public.iot_alert_rule.property_name IS '监控的属性名称';
COMMENT ON COLUMN public.iot_alert_rule.condition_expression IS '告警触发条件表达式';
COMMENT ON COLUMN public.iot_alert_rule.severity IS '告警级别';
COMMENT ON COLUMN public.iot_alert_rule.enabled IS '是否启用';
COMMENT ON COLUMN public.iot_alert_rule.created_time IS '创建时间';
COMMENT ON COLUMN public.iot_alert_rule.updated_time IS '更新时间';

CREATE INDEX idx_iot_alert_rule_rule_name ON public.iot_alert_rule(rule_name);
CREATE INDEX idx_iot_alert_rule_device_id ON public.iot_alert_rule(device_id);
CREATE INDEX idx_iot_alert_rule_severity ON public.iot_alert_rule(severity);
CREATE INDEX idx_iot_alert_rule_enabled ON public.iot_alert_rule(enabled);


CREATE TABLE public.iot_alert_record (
    id BIGSERIAL PRIMARY KEY,                     -- 告警记录ID (自增)
    alert_rule_id VARCHAR(50) NOT NULL REFERENCES public.iot_alert_rule(id) ON DELETE CASCADE,
    device_id VARCHAR(50) NOT NULL REFERENCES public.iot_device(id) ON DELETE CASCADE,
    severity VARCHAR(20) NOT NULL,              -- 告警级别
    trigger_time TIMESTAMP(6) DEFAULT NOW(),    -- 告警触发时间
    resolved BOOLEAN DEFAULT FALSE,             -- 是否已解决
    created_time TIMESTAMP(6) DEFAULT NOW(),
    updated_time TIMESTAMP(6) DEFAULT NOW()
);

COMMENT ON TABLE public.iot_alert_record IS '告警记录表';
COMMENT ON COLUMN public.iot_alert_record.id IS '告警记录ID';
COMMENT ON COLUMN public.iot_alert_record.alert_rule_id IS '告警规则ID';
COMMENT ON COLUMN public.iot_alert_record.device_id IS '关联设备ID';
COMMENT ON COLUMN public.iot_alert_record.severity IS '告警级别';
COMMENT ON COLUMN public.iot_alert_record.trigger_time IS '告警触发时间';
COMMENT ON COLUMN public.iot_alert_record.resolved IS '是否已解决';
COMMENT ON COLUMN public.iot_alert_record.created_time IS '创建时间';


CREATE TABLE public.geofence (
    id VARCHAR(50) PRIMARY KEY,
    fence_name VARCHAR(100) NOT NULL,         -- 围栏名称 (用户自定义)
    description TEXT,                         -- 围栏描述
    fence_type VARCHAR(20) NOT NULL,          -- 围栏类型 (例如：polygon, circle)
    coordinates JSONB NOT NULL,               -- 围栏坐标数据 (JSON 格式，根据 fence_type 不同而不同)
                                              -- 例如：
                                              -- polygon: [[lng1, lat1], [lng2, lat2], ...]
                                              -- circle: {"center": [lng, lat], "radius": number}
    radius DOUBLE PRECISION,                  -- 圆形围栏的半径 (如果 fence_type 是 circle)
    center_longitude DOUBLE PRECISION,        -- 圆形围栏的中心点经度 (如果 fence_type 是 circle)
    center_latitude DOUBLE PRECISION,         -- 圆形围栏的中心点纬度 (如果 fence_type 是 circle)
    polygon_points TEXT[],                    -- 多边形围栏的坐标点数组 (存储格式可根据需求调整，也可直接使用 coordinates JSONB)
    related_object_type VARCHAR(50),          -- 关联对象类型 (例如：device, user, vehicle)
    related_object_id VARCHAR(50),            -- 关联对象ID (指向相关表的ID)
    status VARCHAR(20) DEFAULT 'active',      -- 围栏状态 (active, inactive)
    trigger_in BOOLEAN DEFAULT TRUE,          -- 是否触发进入围栏事件
    trigger_out BOOLEAN DEFAULT TRUE,         -- 是否触发离开围栏事件
    created_by VARCHAR(64),
    created_time TIMESTAMP(6) DEFAULT NOW(),
    updated_by VARCHAR(64),
    updated_time TIMESTAMP(6) DEFAULT NOW(),
    remark VARCHAR(256)
);

COMMENT ON TABLE public.geo_fence IS '电子围栏表';
COMMENT ON COLUMN public.geo_fence.id IS '主键';
COMMENT ON COLUMN public.geo_fence.fence_name IS '围栏名称';
COMMENT ON COLUMN public.geo_fence.description IS '围栏描述';
COMMENT ON COLUMN public.geo_fence.fence_type IS '围栏类型 (polygon: 多边形, circle: 圆形)';
COMMENT ON COLUMN public.geo_fence.coordinates IS '围栏坐标数据 (JSON 格式)';
COMMENT ON COLUMN public.geo_fence.radius IS '圆形围栏半径 (米)';
COMMENT ON COLUMN public.geo_fence.center_longitude IS '圆形围栏中心点经度';
COMMENT ON COLUMN public.geo_fence.center_latitude IS '圆形围栏中心点纬度';
COMMENT ON COLUMN public.geo_fence.polygon_points IS '多边形围栏坐标点数组 (经纬度对)';
COMMENT ON COLUMN public.geo_fence.related_object_type IS '关联对象类型';
COMMENT ON COLUMN public.geo_fence.related_object_id IS '关联对象ID';
COMMENT ON COLUMN public.geo_fence.status IS '围栏状态 (active: 启用, inactive: 禁用)';
COMMENT ON COLUMN public.geo_fence.trigger_in IS '是否触发进入围栏事件';
COMMENT ON COLUMN public.geo_fence.trigger_out IS '是否触发离开围栏事件';
COMMENT ON COLUMN public.geo_fence.created_by IS '创建者';
COMMENT ON COLUMN public.geo_fence.created_time IS '创建时间';
COMMENT ON COLUMN public.geo_fence.updated_by IS '更新者';
COMMENT ON COLUMN public.geo_fence.updated_time IS '更新时间';
COMMENT ON COLUMN public.geo_fence.remark IS '备注';

CREATE INDEX idx_geo_fence_fence_type ON public.geo_fence(fence_type);
CREATE INDEX idx_geo_fence_related_object ON public.geo_fence(related_object_type, related_object_id);
CREATE INDEX idx_geo_fence_status ON public.geo_fence(status);


CREATE TABLE public.geofence_device (
    id VARCHAR(50) PRIMARY KEY,
    fence_id VARCHAR(50) NOT NULL REFERENCES public.geo_fence(id) ON DELETE CASCADE,
    device_id VARCHAR(50) NOT NULL REFERENCES public.iot_device(id) ON DELETE CASCADE,
    created_time TIMESTAMP(6) DEFAULT NOW(),
    UNIQUE (fence_id, device_id) -- 确保一个设备在一个围栏中只有一条记录
);

COMMENT ON TABLE public.geo_fence_device_relation IS '电子围栏和设备关联表';
COMMENT ON COLUMN public.geo_fence_device_relation.id IS '主键';
COMMENT ON COLUMN public.geo_fence_device_relation.fence_id IS '围栏ID';
COMMENT ON COLUMN public.geo_fence_device_relation.device_id IS '设备ID';
COMMENT ON COLUMN public.geo_fence_device_relation.created_time IS '创建时间';

CREATE INDEX idx_geo_fence_device_relation_fence_id ON public.geo_fence_device_relation(fence_id);
CREATE INDEX idx_geo_fence_device_relation_device_id ON public.geo_fence_device_relation(device_id);


CREATE TABLE geofence_event (
    event_id SERIAL PRIMARY KEY,           -- 事件ID
    device_id INT REFERENCES iot_device(device_id) ON DELETE CASCADE, -- 设备ID
    geofence_id INT REFERENCES iot_geofence(geofence_id) ON DELETE CASCADE, -- 围栏ID
    event_type VARCHAR(50),                -- 事件类型 (如进入、离开)
    event_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 事件时间戳
    event_description TEXT                 -- 事件描述
);

# surge-cloud
一个spring cloud微服务。

# 表设计规范
- sys模式下的表，主键为uuid，为varchar类型，可以兼容第三方平台。
- 业务表数据全部使用雪花id。
- 所有业务数据必须要跟 org_id 关联，created_by 不能为空。

DROP TABLE IF EXISTS public.sys_role_datascope;
CREATE TABLE public.sys_role_datascope (
  id             VARCHAR(50) PRIMARY KEY,
  role_id        VARCHAR(50) NOT NULL,                  -- 角色 ID
  scope_type     VARCHAR(20) NOT NULL,                  -- 权限类型（all、self、org、org_and_sub、custom）
  scope_orgs     TEXT,                                  -- scope_type=custom 时，指定的组织 ID 列表
  assigned_by VARCHAR(64),                                  -- 分配人
  assigned_time TIMESTAMP DEFAULT NOW(),                    -- 分配时间
  remark         VARCHAR(256)
);

COMMENT ON TABLE public.sys_role_datascope IS '角色数据权限设置';
COMMENT ON COLUMN public.sys_role_datascope.scope_type IS '数据权限类型：all全部/self本人/org本机构/org_and_sub本机构及下级/custom自定义';


DROP TABLE IF EXISTS public.sys_user_org_role;
CREATE TABLE public.sys_user_org_role (
  id VARCHAR(50) PRIMARY KEY,
  user_id VARCHAR(50) NOT NULL REFERENCES public.sys_user(id) ON DELETE CASCADE,
  org_id VARCHAR(50) NOT NULL REFERENCES public.sys_org(id) ON DELETE CASCADE,
  role_id VARCHAR(50) NOT NULL REFERENCES public.sys_role(id) ON DELETE CASCADE,
  assigned_by VARCHAR(64),                                   -- 分配人
  assigned_time TIMESTAMP DEFAULT NOW(),                     -- 分配时间
  UNIQUE (user_id, org_id, role_id)                         -- 防止用户在同一组织拥有相同的角色
);


DROP TABLE IF EXISTS public.sys_role_resource;
CREATE TABLE public.sys_role_resource (
    id VARCHAR(50) PRIMARY KEY,
    role_id VARCHAR(50) NOT NULL REFERENCES public.sys_role(id) ON DELETE CASCADE,
    resource_id VARCHAR(50) NOT NULL REFERENCES public.sys_resource(id) ON DELETE CASCADE,
    granted_time TIMESTAMP(6) DEFAULT NOW(),
    granted_by VARCHAR(64),
    remark VARCHAR(256),
    UNIQUE (role_id, resource_id)
);

COMMENT ON TABLE public.sys_role_resource IS '角色资源关联表';
COMMENT ON COLUMN public.sys_role_resource.role_id IS '角色ID，关联 sys_role 表';
COMMENT ON COLUMN public.sys_role_resource.resource_id IS '资源ID，关联 sys_resource 表';
COMMENT ON COLUMN public.sys_role_resource.granted_time IS '授权时间';
COMMENT ON COLUMN public.sys_role_resource.granted_by IS '授权人';
COMMENT ON COLUMN public.sys_role_resource.remark IS '备注';


DROP TABLE IF EXISTS public.sys_resource;
CREATE TABLE public.sys_resource (
    id VARCHAR(50) PRIMARY KEY,
    parent_id VARCHAR(50),                                   -- 菜单树结构父级（API 无需）
    name VARCHAR(100) NOT NULL,                             -- 资源名称
    type VARCHAR(20) NOT NULL,                              -- menu | button | api
    path TEXT,                                              -- 前端路由 / API 路径
    method VARCHAR(10),                                     -- 仅 API 用：GET / POST / PUT ...
    visible BOOLEAN DEFAULT TRUE,                           -- 是否可见（菜单专用）
    icon VARCHAR(50),                                       -- 菜单图标
    seq BIGINT DEFAULT 0,                                   -- 排序
    status BOOLEAN DEFAULT FALSE,                           -- 状态 (FALSE 禁用, TRUE 启用)
    created_by     VARCHAR(64) DEFAULT '',
    created_time   TIMESTAMP(6) DEFAULT NOW(),
    updated_by     VARCHAR(64) DEFAULT '',
    updated_time   TIMESTAMP(6) DEFAULT NOW(),
    remark TEXT
);

COMMENT ON TABLE public.sys_resource IS '系统资源（菜单/API/按钮）';
COMMENT ON COLUMN public.sys_resource.parent_id IS '菜单树结构父级（API 无需），关联 sys_resource 表';
COMMENT ON COLUMN public.sys_resource.name IS '资源名称';
COMMENT ON COLUMN public.sys_resource.type IS '资源类型：menu（菜单） | button（按钮） | api（API）';
COMMENT ON COLUMN public.sys_resource.path IS '前端路由 / API 路径';
COMMENT ON COLUMN public.sys_resource.method IS '仅 API 用：HTTP 方法 (GET / POST / PUT ...)';
COMMENT ON COLUMN public.sys_resource.visible IS '是否可见（菜单专用）';
COMMENT ON COLUMN public.sys_resource.icon IS '菜单图标';
COMMENT ON COLUMN public.sys_resource.seq IS '排序';
COMMENT ON COLUMN public.sys_resource.status IS '状态：false（禁用） | true（启用）';
COMMENT ON COLUMN public.sys_resource.created_time IS '创建时间';
COMMENT ON COLUMN public.sys_resource.remark IS '备注';

CREATE INDEX idx_sys_resource_type ON public.sys_resource(type);

COMMENT ON TABLE public.sys_resource IS '系统资源';






DROP TABLE IF EXISTS public.sys_user;
CREATE TABLE public.sys_user (
  id             VARCHAR(50) PRIMARY KEY,                 -- 主键（UUID）
  username       VARCHAR(50) NOT NULL UNIQUE,             -- 用户名（唯一）
  password       VARCHAR(255) NOT NULL,                   -- 密码（加密存储）
  nickname       VARCHAR(50),                             -- 昵称
  realname       VARCHAR(50),                             -- 真实姓名
  idcard         VARCHAR(50),                             -- 身份证号
  email          VARCHAR(100),                            -- 邮箱
  phone          VARCHAR(20),                             -- 手机号
  avatar         TEXT,                                    -- 头像地址
  gender         CHAR(1) DEFAULT 'U',                     -- 性别：M男 F女 U未知
  status         CHAR(1) DEFAULT '0',                     -- 状态：0正常 1禁用
  is_super       BOOLEAN DEFAULT FALSE,                   -- 是否超级管理员
  created_by     VARCHAR(64) DEFAULT '',                  -- 创建者
  created_time   TIMESTAMP(6) DEFAULT NOW(),              -- 创建时间
  updated_by     VARCHAR(64) DEFAULT '',                  -- 更新者
  updated_time   TIMESTAMP(6) DEFAULT NOW(),              -- 更新时间
  deleted_time   TIMESTAMP(6),                            -- 删除时间（逻辑删除）
  remark         VARCHAR(256)                             -- 备注
);

-- 索引
CREATE INDEX idx_sys_user_username      ON public.sys_user(username);
CREATE INDEX idx_sys_user_phone         ON public.sys_user(phone);
CREATE INDEX idx_sys_user_email         ON public.sys_user(email);
CREATE INDEX idx_sys_user_status        ON public.sys_user(status);

-- 注释
COMMENT ON TABLE public.sys_user IS '系统用户表';

COMMENT ON COLUMN public.sys_user.id             IS '主键';
COMMENT ON COLUMN public.sys_user.username       IS '用户名';
COMMENT ON COLUMN public.sys_user.password       IS '用户密码（加密）';
COMMENT ON COLUMN public.sys_user.nickname       IS '用户昵称';
COMMENT ON COLUMN public.sys_user.email          IS '邮箱';
COMMENT ON COLUMN public.sys_user.phone          IS '手机号';
COMMENT ON COLUMN public.sys_user.avatar         IS '头像 URL';
COMMENT ON COLUMN public.sys_user.gender         IS '性别：M男 F女 U未知';
COMMENT ON COLUMN public.sys_user.status         IS '用户状态：0正常 1禁用';
COMMENT ON COLUMN public.sys_user.is_super       IS '是否超级管理员';
COMMENT ON COLUMN public.sys_user.created_by     IS '创建者';
COMMENT ON COLUMN public.sys_user.created_time   IS '创建时间';
COMMENT ON COLUMN public.sys_user.updated_by     IS '更新者';
COMMENT ON COLUMN public.sys_user.updated_time   IS '更新时间';
COMMENT ON COLUMN public.sys_user.deleted_time   IS '删除时间（逻辑删除）';
COMMENT ON COLUMN public.sys_user.remark         IS '备注';


DROP TABLE IF EXISTS public.sys_org;
CREATE TABLE public.sys_org (
  id             VARCHAR(50) PRIMARY KEY,
  parent_id      VARCHAR(50),                             -- 支持组织树结构
  name           VARCHAR(100) NOT NULL,                   -- 部门名称
  code           VARCHAR(50) NOT NULL,                    -- 编码
  type           VARCHAR(20),                             -- 类型：总部/分公司/部门等
  level          VARCHAR(20),                             -- 级别：一级/二级/三级等
  status         CHAR(1) DEFAULT '0',                     -- 状态：0正常 1禁用
  seq            BIGINT DEFAULT 0,                        -- 排序字段
  created_by     VARCHAR(64) DEFAULT '',
  created_time   TIMESTAMP(6) DEFAULT NOW(),
  updated_by     VARCHAR(64) DEFAULT '',
  updated_time   TIMESTAMP(6) DEFAULT NOW(),
  deleted_time   TIMESTAMP(6),
  remark         VARCHAR(256)
);

-- 索引
CREATE INDEX idx_sys_org_parent_id      ON public.sys_org(parent_id);
CREATE INDEX idx_sys_org_code           ON public.sys_org(code);
CREATE INDEX idx_sys_org_status         ON public.sys_org(status);
CREATE INDEX idx_sys_org_deleted_time   ON public.sys_org(deleted_time);
CREATE INDEX idx_sys_org_seq            ON public.sys_org(seq);

-- 注释
COMMENT ON TABLE public.sys_org IS '组织表';

COMMENT ON COLUMN public.sys_org.id            IS '主键';
COMMENT ON COLUMN public.sys_org.name          IS '部门名称';
COMMENT ON COLUMN public.sys_org.code          IS '部门编码';
COMMENT ON COLUMN public.sys_org.status        IS '状态：0正常 1禁用';
COMMENT ON COLUMN public.sys_org.seq           IS '排序字段';
COMMENT ON COLUMN public.sys_org.parent_id     IS '上级组织 ID';
COMMENT ON COLUMN public.sys_org.created_by    IS '创建者';
COMMENT ON COLUMN public.sys_org.created_time  IS '创建时间';
COMMENT ON COLUMN public.sys_org.updated_by    IS '更新者';
COMMENT ON COLUMN public.sys_org.updated_time  IS '更新时间';
COMMENT ON COLUMN public.sys_org.deleted_time  IS '删除时间（逻辑删除）';
COMMENT ON COLUMN public.sys_org.remark        IS '备注';


DROP TABLE IF EXISTS public.sys_role;
CREATE TABLE public.sys_role (
  id             VARCHAR(50) PRIMARY KEY,
  name           VARCHAR(50) NOT NULL UNIQUE,
  code           VARCHAR(50) NOT NULL UNIQUE,
  seq            BIGINT DEFAULT 0,
  created_by     VARCHAR(64) DEFAULT '',
  created_time   TIMESTAMP(6) DEFAULT NOW(),
  updated_by     VARCHAR(64) DEFAULT '',
  updated_time   TIMESTAMP(6) DEFAULT NOW(),
  remark         VARCHAR(256)
);

-- 索引
CREATE INDEX idx_sys_role_seq            ON public.sys_role(seq);

-- 注释
COMMENT ON TABLE public.sys_role IS '角色表';

COMMENT ON COLUMN public.sys_role.id            IS '主键';
COMMENT ON COLUMN public.sys_role.name          IS '角色名称';
COMMENT ON COLUMN public.sys_role.code          IS '角色编码（唯一）';
COMMENT ON COLUMN public.sys_role.seq           IS '排序字段';
COMMENT ON COLUMN public.sys_role.created_by    IS '创建者';
COMMENT ON COLUMN public.sys_role.created_time  IS '创建时间';
COMMENT ON COLUMN public.sys_role.updated_by    IS '更新者';
COMMENT ON COLUMN public.sys_role.updated_time  IS '更新时间';
COMMENT ON COLUMN public.sys_role.remark        IS '备注';

DROP TABLE IF EXISTS public.sys_dict;
CREATE TABLE public.sys_dict (
  id             VARCHAR(50) PRIMARY KEY,                  -- 主键
  parent_id      VARCHAR(50),                              -- 父级 ID（支持树结构）
  type           VARCHAR(50) NOT NULL,                     -- 字典类型
  label          VARCHAR(50) NOT NULL,                     -- 字典编码
  code           VARCHAR(50) NOT NULL,                     -- 字典标签
  value          TEXT NOT NULL,                            -- 字典值
  seq            BIGINT DEFAULT 0,                         -- 排序（非负）
  status         CHAR(1) DEFAULT '0',
  is_default     BOOLEAN DEFAULT FALSE,                    -- 是否默认：FALSE 否，TRUE 是
  extra          JSONB DEFAULT '{}'::jsonb,                -- 附加内容（支持灵活扩展）
  created_by     VARCHAR(64) DEFAULT '',                   -- 创建者
  created_time   TIMESTAMP(6) DEFAULT NOW(),               -- 创建时间
  updated_by     VARCHAR(64) DEFAULT '',                   -- 更新者
  updated_time   TIMESTAMP(6) DEFAULT NOW(),               -- 更新时间
  deleted_time   TIMESTAMP(6),                             -- 删除时间（逻辑删除用，可选）
  remark         VARCHAR(256)                              -- 备注
);

-- 唯一约束
ALTER TABLE public.sys_dict
  ADD CONSTRAINT uq_type_code UNIQUE(type, code);

-- 索引
CREATE INDEX idx_sys_dict_type         ON public.sys_dict(type);
CREATE INDEX idx_sys_dict_code         ON public.sys_dict(code);
CREATE INDEX idx_sys_dict_status       ON public.sys_dict(status);
CREATE INDEX idx_sys_dict_parent       ON public.sys_dict(parent_id);
CREATE INDEX idx_sys_dict_type_code    ON public.sys_dict(type, code);

-- 注释
COMMENT ON TABLE public.sys_dict IS '字典类型表';
COMMENT ON COLUMN public.sys_dict.id             IS '主键';
COMMENT ON COLUMN public.sys_dict.parent_id      IS '父级字典 ID';
COMMENT ON COLUMN public.sys_dict.type           IS '字典类型';
COMMENT ON COLUMN public.sys_dict.label          IS '字典编码';
COMMENT ON COLUMN public.sys_dict.code           IS '字典标签';
COMMENT ON COLUMN public.sys_dict.value          IS '字典值';
COMMENT ON COLUMN public.sys_dict.is_default     IS '是否默认，true 表示默认';
COMMENT ON COLUMN public.sys_dict.seq            IS '字典排序';
COMMENT ON COLUMN public.sys_dict.extra          IS '附加内容（JSON 格式）';
COMMENT ON COLUMN public.sys_dict.status         IS '字典状态：normal 正常，disabled 禁用';
COMMENT ON COLUMN public.sys_dict.created_by     IS '创建者';
COMMENT ON COLUMN public.sys_dict.created_time   IS '创建时间';
COMMENT ON COLUMN public.sys_dict.updated_by     IS '更新者';
COMMENT ON COLUMN public.sys_dict.updated_time   IS '更新时间';
COMMENT ON COLUMN public.sys_dict.deleted_time   IS '删除时间（逻辑删除）';
COMMENT ON COLUMN public.sys_dict.remark         IS '备注';


DROP TABLE IF EXISTS public.sys_file;
CREATE TABLE public.sys_file (
  id              VARCHAR(50) PRIMARY KEY,            -- 文件唯一标识符
  file_name       VARCHAR(255) NOT NULL,              -- 文件名
  file_size       BIGINT NOT NULL,                    -- 文件大小，单位：字节
  file_type       VARCHAR(50),                        -- 文件类型（如：image, pdf, video 等）
  file_path       VARCHAR(255) NOT NULL,              -- 文件存储路径（OSS URL 或本地路径）
  storage_type    VARCHAR(20) NOT NULL,               -- 存储类型（local 或 oss）
  file_md5        CHAR(32),                           -- 文件的 MD5 校验值，用于验证文件完整性
  file_group      VARCHAR(50),                        -- 文件分组（如用户头像、商品图片、文章图片等）
  uploaded_by     VARCHAR(64),                        -- 上传者
  uploaded_time   TIMESTAMP(6) DEFAULT NOW(),         -- 上传时间
  status          VARCHAR(20) DEFAULT 'uploaded',     -- 文件状态（如 uploaded, processing, failed）
  tags            VARCHAR(255),                       -- 标签，方便分类检索
  metadata        JSONB,                              -- 文件的元数据，灵活存储额外信息（如文件的描述、权限等）
  relate_table    VARCHAR(100),                       -- 关联的表名
  relate_id       VARCHAR(50)                         -- 关联的记录 ID
);

-- 索引
CREATE INDEX idx_sys_file_file_name      ON public.sys_file(file_name);
CREATE INDEX idx_sys_file_relate_table   ON public.sys_file(relate_table);
CREATE INDEX idx_sys_file_relate_id      ON public.sys_file(relate_id);
CREATE INDEX idx_sys_file_file_group     ON public.sys_file(file_group);

-- 表注释
COMMENT ON TABLE public.sys_file IS '文件表，用于存储本地或OSS存储的文件信息';
COMMENT ON COLUMN public.sys_file.id IS '文件唯一标识符';
COMMENT ON COLUMN public.sys_file.file_name IS '文件名';
COMMENT ON COLUMN public.sys_file.file_size IS '文件大小，单位：字节';
COMMENT ON COLUMN public.sys_file.file_type IS '文件类型（如：image, pdf, video 等）';
COMMENT ON COLUMN public.sys_file.file_path IS '文件存储路径（OSS URL 或本地路径）';
COMMENT ON COLUMN public.sys_file.storage_type IS '存储类型（local 或 oss）';
COMMENT ON COLUMN public.sys_file.file_md5 IS '文件的 MD5 校验值，用于验证文件完整性';
COMMENT ON COLUMN public.sys_file.uploaded_by IS '上传者';
COMMENT ON COLUMN public.sys_file.uploaded_time IS '上传时间';
COMMENT ON COLUMN public.sys_file.status IS '文件状态（如 uploaded, processing, failed）';
COMMENT ON COLUMN public.sys_file.remark IS '备注';
COMMENT ON COLUMN public.sys_file.tags IS '标签，方便分类检索';
COMMENT ON COLUMN public.sys_file.metadata IS '文件的元数据，灵活存储额外信息（如文件的描述、权限等）';
COMMENT ON COLUMN public.sys_file.relate_table IS '关联的表名';
COMMENT ON COLUMN public.sys_file.relate_id IS '关联的记录 ID';
COMMENT ON COLUMN public.sys_file.file_group IS '文件分组，用于区分不同类型的文件';
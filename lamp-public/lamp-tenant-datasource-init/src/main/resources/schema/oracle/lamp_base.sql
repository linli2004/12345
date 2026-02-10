CREATE TABLE com_appendix(
                             id NUMBER(19) NOT NULL,
                             biz_id NUMBER(19) NOT NULL,
                             biz_type VARCHAR2(255) DEFAULT  '' NOT NULL,
                             file_type VARCHAR2(10) DEFAULT  '',
                             bucket VARCHAR2(255) DEFAULT  '',
                             path VARCHAR2(255) DEFAULT  '',
                             original_file_name VARCHAR2(255) DEFAULT  '',
                             content_type VARCHAR2(255) DEFAULT  '',
                             size_ NUMBER(19) DEFAULT  0,
                             created_time DATE NOT NULL,
                             created_by NUMBER(19),
                             updated_time DATE NOT NULL,
                             updated_by NUMBER(19),
                             created_org_id NUMBER(19),
                             PRIMARY KEY (id)
);

COMMENT ON TABLE com_appendix IS '业务附件';
COMMENT ON COLUMN com_appendix.id IS 'ID';
COMMENT ON COLUMN com_appendix.biz_id IS '业务id';
COMMENT ON COLUMN com_appendix.biz_type IS '业务类型;同一个业务，不同的字段，需要分别设置不同的业务类型';
COMMENT ON COLUMN com_appendix.file_type IS '文件类型;#FileType{IMAGE:图片;VIDEO:视频;AUDIO:音频;DOC:文档;OTHER:其他;}';
COMMENT ON COLUMN com_appendix.bucket IS '桶';
COMMENT ON COLUMN com_appendix.path IS '文件相对地址';
COMMENT ON COLUMN com_appendix.original_file_name IS '原始文件名';
COMMENT ON COLUMN com_appendix.content_type IS '文件类型';
COMMENT ON COLUMN com_appendix.size_ IS '大小';
COMMENT ON COLUMN com_appendix.created_time IS '创建时间';
COMMENT ON COLUMN com_appendix.created_by IS '创建人';
COMMENT ON COLUMN com_appendix.updated_time IS '最后修改时间';
COMMENT ON COLUMN com_appendix.updated_by IS '最后修改人';
COMMENT ON COLUMN com_appendix.created_org_id IS '创建人组织';

CREATE TABLE com_file(
                         id NUMBER(19) NOT NULL,
                         biz_type VARCHAR2(255) DEFAULT  '' NOT NULL,
                         file_type VARCHAR2(10) DEFAULT  'OTHER',
                         storage_type VARCHAR2(30) DEFAULT  'LOCAL',
                         bucket VARCHAR2(255) DEFAULT  '',
                         path VARCHAR2(255) DEFAULT  '',
                         url VARCHAR2(255) DEFAULT  '',
                         unique_file_name VARCHAR2(255) DEFAULT  '',
                         file_md5 VARCHAR2(255) DEFAULT  '',
                         original_file_name VARCHAR2(255) DEFAULT  '',
                         content_type VARCHAR2(255) DEFAULT  '',
                         suffix VARCHAR2(255) DEFAULT  '',
                         size_ NUMBER(19) DEFAULT  0,
                         created_time DATE NOT NULL,
                         created_by NUMBER(19),
                         updated_time DATE NOT NULL,
                         updated_by NUMBER(19),
                         created_org_id NUMBER(19),
                         PRIMARY KEY (id)
);

COMMENT ON TABLE com_file IS '增量文件上传日志';
COMMENT ON COLUMN com_file.id IS 'ID';
COMMENT ON COLUMN com_file.biz_type IS '业务类型;同一个业务，不同的字段，需要分别设置不同的业务类型';
COMMENT ON COLUMN com_file.file_type IS '文件类型;#FileType{IMAGE:图片;VIDEO:视频;AUDIO:音频;DOC:文档;OTHER:其他;}';
COMMENT ON COLUMN com_file.storage_type IS '存储类型;#FileStorageType{LOCAL:本地;FAST_DFS:FastDFS;MIN_IO:MinIO;ALI_OSS:阿里云OSS;QINIU_OSS:七牛云OSS;HUAWEI_OSS:华为云OSS;}';
COMMENT ON COLUMN com_file.bucket IS '桶';
COMMENT ON COLUMN com_file.path IS '文件相对地址';
COMMENT ON COLUMN com_file.url IS '文件访问地址';
COMMENT ON COLUMN com_file.unique_file_name IS '唯一文件名';
COMMENT ON COLUMN com_file.file_md5 IS '文件md5';
COMMENT ON COLUMN com_file.original_file_name IS '原始文件名';
COMMENT ON COLUMN com_file.content_type IS '文件类型';
COMMENT ON COLUMN com_file.suffix IS '后缀';
COMMENT ON COLUMN com_file.size_ IS '大小';
COMMENT ON COLUMN com_file.created_time IS '创建时间';
COMMENT ON COLUMN com_file.created_by IS '创建人';
COMMENT ON COLUMN com_file.updated_time IS '最后修改时间';
COMMENT ON COLUMN com_file.updated_by IS '最后修改人';
COMMENT ON COLUMN com_file.created_org_id IS '创建人组织';

CREATE TABLE base_operation_log(
                                   id NUMBER(19) NOT NULL,
                                   request_ip VARCHAR2(50) DEFAULT  '',
                                   type VARCHAR2(5) DEFAULT  'OPT',
                                   user_name VARCHAR2(50) DEFAULT  '',
                                   description VARCHAR2(255) DEFAULT  '',
                                   class_path VARCHAR2(255) DEFAULT  '',
                                   action_method VARCHAR2(50) DEFAULT  '',
                                   request_uri VARCHAR2(500) DEFAULT  '',
                                   http_method VARCHAR2(10) DEFAULT  'GET',
                                   start_time DATE,
                                   finish_time DATE,
                                   consuming_time NUMBER(19) DEFAULT  0,
                                   ua VARCHAR2(500) DEFAULT  '',
                                   created_time DATE,
                                   created_by NUMBER(19),
                                   updated_time DATE,
                                   updated_by NUMBER(19),
                                   created_org_id NUMBER(19),
                                   PRIMARY KEY (id)
);

COMMENT ON TABLE base_operation_log IS '操作日志';
COMMENT ON COLUMN base_operation_log.id IS '主键';
COMMENT ON COLUMN base_operation_log.request_ip IS '操作IP';
COMMENT ON COLUMN base_operation_log.type IS '日志类型;#LogType{OPT:操作类型;EX:异常类型}';
COMMENT ON COLUMN base_operation_log.user_name IS '操作人';
COMMENT ON COLUMN base_operation_log.description IS '操作描述';
COMMENT ON COLUMN base_operation_log.class_path IS '类路径';
COMMENT ON COLUMN base_operation_log.action_method IS '请求方法';
COMMENT ON COLUMN base_operation_log.request_uri IS '请求地址';
COMMENT ON COLUMN base_operation_log.http_method IS '请求类型;#HttpMethod{GET:GET请求;POST:POST请求;PUT:PUT请求;DELETE:DELETE请求;PATCH:PATCH请求;TRACE:TRACE请求;HEAD:HEAD请求;OPTIONS:OPTIONS请求;}';
COMMENT ON COLUMN base_operation_log.start_time IS '开始时间';
COMMENT ON COLUMN base_operation_log.finish_time IS '完成时间';
COMMENT ON COLUMN base_operation_log.consuming_time IS '消耗时间';
COMMENT ON COLUMN base_operation_log.ua IS '浏览器';
COMMENT ON COLUMN base_operation_log.created_time IS '创建时间';
COMMENT ON COLUMN base_operation_log.created_by IS '创建人';
COMMENT ON COLUMN base_operation_log.updated_time IS '最后更新时间';
COMMENT ON COLUMN base_operation_log.updated_by IS '最后更新人';
COMMENT ON COLUMN base_operation_log.created_org_id IS '创建人组织';

CREATE TABLE base_operation_log_ext(
                                       id NUMBER(19) NOT NULL,
                                       params CLOB,
                                       result CLOB,
                                       ex_detail CLOB,
                                       created_time DATE,
                                       created_by NUMBER(19),
                                       updated_time DATE,
                                       updated_by NUMBER(19),
                                       created_org_id NUMBER(19),
                                       PRIMARY KEY (id)
);

COMMENT ON TABLE base_operation_log_ext IS '操作日志扩展';
COMMENT ON COLUMN base_operation_log_ext.id IS '主键';
COMMENT ON COLUMN base_operation_log_ext.params IS '请求参数';
COMMENT ON COLUMN base_operation_log_ext.result IS '返回值';
COMMENT ON COLUMN base_operation_log_ext.ex_detail IS '异常描述';
COMMENT ON COLUMN base_operation_log_ext.created_time IS '创建时间';
COMMENT ON COLUMN base_operation_log_ext.created_by IS '创建人';
COMMENT ON COLUMN base_operation_log_ext.updated_time IS '最后更新时间';
COMMENT ON COLUMN base_operation_log_ext.updated_by IS '最后更新人ID';
COMMENT ON COLUMN base_operation_log_ext.created_org_id IS '创建人组织';

CREATE TABLE base_org(
                         id NUMBER(19) NOT NULL,
                         name VARCHAR2(255) NOT NULL,
                         type_ CHAR(2) DEFAULT  '10',
                         short_name VARCHAR2(255),
                         parent_id NUMBER(19),
                         tree_grade NUMBER(10) DEFAULT  0,
                         tree_path VARCHAR2(255),
                         sort_value NUMBER(10) DEFAULT  1,
                         state NUMBER(1) DEFAULT  1,
                         remarks VARCHAR2(255),
                         created_time DATE,
                         created_by NUMBER(19),
                         updated_time DATE,
                         updated_by NUMBER(19),
                         created_org_id NUMBER(19),
                         PRIMARY KEY (id)
);

COMMENT ON TABLE base_org IS '组织';
COMMENT ON COLUMN base_org.id IS 'ID';
COMMENT ON COLUMN base_org.name IS '名称';
COMMENT ON COLUMN base_org.type_ IS '类型;[10-单位 20-部门]@Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.Base.ORG_TYPE)';
COMMENT ON COLUMN base_org.short_name IS '简称';
COMMENT ON COLUMN base_org.parent_id IS '父组织';
COMMENT ON COLUMN base_org.tree_grade IS '树层级';
COMMENT ON COLUMN base_org.tree_path IS '树路径;用id拼接树结构';
COMMENT ON COLUMN base_org.sort_value IS '排序';
COMMENT ON COLUMN base_org.state IS '状态;[0-禁用 1-启用]';
COMMENT ON COLUMN base_org.remarks IS '备注';
COMMENT ON COLUMN base_org.created_time IS '创建时间';
COMMENT ON COLUMN base_org.created_by IS '创建人';
COMMENT ON COLUMN base_org.updated_time IS '修改时间';
COMMENT ON COLUMN base_org.updated_by IS '修改人';
COMMENT ON COLUMN base_org.created_org_id IS '创建人组织';


CREATE UNIQUE INDEX uk_org_name ON base_org(name);

CREATE TABLE base_role(
                          id NUMBER(19) NOT NULL,
                          category CHAR(2) DEFAULT  '10' NOT NULL,
                          type_ CHAR(2) DEFAULT  '20' NOT NULL,
                          name VARCHAR2(50) NOT NULL,
                          code VARCHAR2(20) NOT NULL,
                          remarks VARCHAR2(255),
                          state NUMBER(1) DEFAULT  1,
                          readonly_ NUMBER(1) DEFAULT  0,
                          created_by NUMBER(19),
                          created_time DATE,
                          updated_by NUMBER(19),
                          updated_time DATE,
                          created_org_id NUMBER(19),
                          PRIMARY KEY (id)
);

COMMENT ON TABLE base_role IS '角色';
COMMENT ON COLUMN base_role.id IS 'ID';
COMMENT ON COLUMN base_role.category IS '角色类别;[10-功能角色 20-桌面角色 30-数据角色]@Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.Base.ROLE_CATEGORY)';
COMMENT ON COLUMN base_role.type_ IS '角色类型;[10-系统角色 20-自定义角色];
@Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.Global.DATA_TYPE)';
COMMENT ON COLUMN base_role.name IS '名称';
COMMENT ON COLUMN base_role.code IS '编码';
COMMENT ON COLUMN base_role.remarks IS '备注';
COMMENT ON COLUMN base_role.state IS '状态';
COMMENT ON COLUMN base_role.readonly_ IS '内置角色';
COMMENT ON COLUMN base_role.created_by IS '创建人';
COMMENT ON COLUMN base_role.created_time IS '创建时间';
COMMENT ON COLUMN base_role.updated_by IS '更新人';
COMMENT ON COLUMN base_role.updated_time IS '更新时间';
COMMENT ON COLUMN base_role.created_org_id IS '创建人组织';


CREATE UNIQUE INDEX uk_code ON base_role(code);

CREATE TABLE base_role_resource_rel(
                                       id NUMBER(19) NOT NULL,
                                       resource_id NUMBER(19) NOT NULL,
                                       application_id NUMBER(19) NOT NULL,
                                       role_id NUMBER(19) NOT NULL,
                                       created_time DATE,
                                       created_by NUMBER(19),
                                       updated_time DATE,
                                       updated_by NUMBER(19),
                                       created_org_id NUMBER(19),
                                       PRIMARY KEY (id)
);

COMMENT ON TABLE base_role_resource_rel IS '角色的资源';
COMMENT ON COLUMN base_role_resource_rel.id IS '主键';
COMMENT ON COLUMN base_role_resource_rel.resource_id IS '拥有资源;#def_resource';
COMMENT ON COLUMN base_role_resource_rel.application_id IS '所属应用;#def_application';
COMMENT ON COLUMN base_role_resource_rel.role_id IS '所属角色;#base_role';
COMMENT ON COLUMN base_role_resource_rel.created_time IS '创建时间';
COMMENT ON COLUMN base_role_resource_rel.created_by IS '创建人';
COMMENT ON COLUMN base_role_resource_rel.updated_time IS '最后更新时间';
COMMENT ON COLUMN base_role_resource_rel.updated_by IS '最后更新人';
COMMENT ON COLUMN base_role_resource_rel.created_org_id IS '创建人组织';


CREATE UNIQUE INDEX uk_role_resource ON base_role_resource_rel(resource_id,role_id);

CREATE TABLE base_org_role_rel(
                                  id NUMBER(19) NOT NULL,
                                  org_id NUMBER(19) NOT NULL,
                                  role_id NUMBER(19) NOT NULL,
                                  created_time DATE,
                                  created_by NUMBER(19),
                                  updated_time DATE,
                                  updated_by NUMBER(19),
                                  created_org_id NUMBER(19),
                                  PRIMARY KEY (id)
);

COMMENT ON TABLE base_org_role_rel IS '组织的角色';
COMMENT ON COLUMN base_org_role_rel.id IS 'ID';
COMMENT ON COLUMN base_org_role_rel.org_id IS '所属部门;#base_org';
COMMENT ON COLUMN base_org_role_rel.role_id IS '拥有角色;#base_role';
COMMENT ON COLUMN base_org_role_rel.created_time IS '创建时间';
COMMENT ON COLUMN base_org_role_rel.created_by IS '创建人';
COMMENT ON COLUMN base_org_role_rel.updated_time IS '最后更新时间';
COMMENT ON COLUMN base_org_role_rel.updated_by IS '最后更新人';
COMMENT ON COLUMN base_org_role_rel.created_org_id IS '创建人组织';


CREATE UNIQUE INDEX uk_org_role ON base_org_role_rel(org_id,role_id);

CREATE TABLE base_position(
                              id NUMBER(19) NOT NULL,
                              name VARCHAR2(255) DEFAULT  '' NOT NULL,
                              org_id NUMBER(19),
                              state NUMBER(1) DEFAULT  1,
                              remarks VARCHAR2(255) DEFAULT  '',
                              created_time DATE,
                              created_by NUMBER(19),
                              updated_time DATE,
                              updated_by NUMBER(19),
                              created_org_id NUMBER(19),
                              PRIMARY KEY (id)
);

COMMENT ON TABLE base_position IS '岗位';
COMMENT ON COLUMN base_position.id IS 'ID';
COMMENT ON COLUMN base_position.name IS '名称';
COMMENT ON COLUMN base_position.org_id IS '所属组织;#base_org@Echo(api = EchoApi.ORG_ID_CLASS)';
COMMENT ON COLUMN base_position.state IS '状态;0-禁用 1-启用';
COMMENT ON COLUMN base_position.remarks IS '备注';
COMMENT ON COLUMN base_position.created_time IS '创建时间';
COMMENT ON COLUMN base_position.created_by IS '创建人';
COMMENT ON COLUMN base_position.updated_time IS '修改时间';
COMMENT ON COLUMN base_position.updated_by IS '修改人';
COMMENT ON COLUMN base_position.created_org_id IS '创建人组织';


CREATE UNIQUE INDEX uk_name ON base_position(name);

CREATE TABLE base_employee_role_rel(
                                       id NUMBER(19) NOT NULL,
                                       role_id NUMBER(19) NOT NULL,
                                       employee_id NUMBER(19) NOT NULL,
                                       created_by NUMBER(19),
                                       created_time DATE,
                                       updated_by NUMBER(19),
                                       updated_time DATE,
                                       created_org_id NUMBER(19),
                                       PRIMARY KEY (id)
);

COMMENT ON TABLE base_employee_role_rel IS '员工的角色';
COMMENT ON COLUMN base_employee_role_rel.id IS 'ID';
COMMENT ON COLUMN base_employee_role_rel.role_id IS '拥有角色;#base_role';
COMMENT ON COLUMN base_employee_role_rel.employee_id IS '所属员工;#base_employee';
COMMENT ON COLUMN base_employee_role_rel.created_by IS '创建人';
COMMENT ON COLUMN base_employee_role_rel.created_time IS '创建时间';
COMMENT ON COLUMN base_employee_role_rel.updated_by IS '最后更新人';
COMMENT ON COLUMN base_employee_role_rel.updated_time IS '最后更新时间';
COMMENT ON COLUMN base_employee_role_rel.created_org_id IS '创建人组织';


CREATE UNIQUE INDEX uk_err_role_employee ON base_employee_role_rel(role_id,employee_id);

CREATE TABLE base_employee(
                              id NUMBER(19) NOT NULL,
                              is_default NUMBER(1) DEFAULT  0 NOT NULL,
                              position_id NUMBER(19),
                              user_id NUMBER(19) NOT NULL,
                              last_company_id NUMBER(19),
                              last_dept_id NUMBER(19),
                              real_name VARCHAR2(255) DEFAULT  '' NOT NULL,
                              active_status CHAR(2) DEFAULT  '20',
                              position_status CHAR(2) DEFAULT  '10',
                              state NUMBER(1) DEFAULT  1,
                              created_by NUMBER(19),
                              created_time DATE,
                              updated_by NUMBER(19),
                              updated_time DATE,
                              created_org_id NUMBER(19),
                              PRIMARY KEY (id)
);

COMMENT ON TABLE base_employee IS '员工';
COMMENT ON COLUMN base_employee.id IS 'ID';
COMMENT ON COLUMN base_employee.is_default IS '是否默认员工;[0-否 1-是]';
COMMENT ON COLUMN base_employee.position_id IS '所属岗位';
COMMENT ON COLUMN base_employee.user_id IS '用户';
COMMENT ON COLUMN base_employee.last_company_id IS '上次登录公司ID';
COMMENT ON COLUMN base_employee.last_dept_id IS '上次登录部门ID';
COMMENT ON COLUMN base_employee.real_name IS '真实姓名';
COMMENT ON COLUMN base_employee.active_status IS '激活状态;[10-未激活 20-已激活]@Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.Global.ACTIVE_STATUS)';
COMMENT ON COLUMN base_employee.position_status IS '职位状态;[10-在职 20-离职]@Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.Base.POSITION_STATUS)';
COMMENT ON COLUMN base_employee.state IS '状态;[0-禁用 1-启用]';
COMMENT ON COLUMN base_employee.created_by IS '创建人';
COMMENT ON COLUMN base_employee.created_time IS '创建时间';
COMMENT ON COLUMN base_employee.updated_by IS '最后更新人';
COMMENT ON COLUMN base_employee.updated_time IS '最后更新时间';
COMMENT ON COLUMN base_employee.created_org_id IS '创建人组织';


CREATE UNIQUE INDEX uk_emp_user_id ON base_employee(user_id);

CREATE TABLE base_employee_org_rel(
                                      id NUMBER(19) NOT NULL,
                                      org_id NUMBER(19) NOT NULL,
                                      employee_id NUMBER(19) NOT NULL,
                                      created_by NUMBER(19),
                                      created_time DATE,
                                      updated_by NUMBER(19),
                                      updated_time DATE,
                                      created_org_id NUMBER(19),
                                      PRIMARY KEY (id)
);

COMMENT ON TABLE base_employee_org_rel IS '员工所在部门';
COMMENT ON COLUMN base_employee_org_rel.id IS 'ID';
COMMENT ON COLUMN base_employee_org_rel.org_id IS '关联机构';
COMMENT ON COLUMN base_employee_org_rel.employee_id IS '关联员工';
COMMENT ON COLUMN base_employee_org_rel.created_by IS '创建人';
COMMENT ON COLUMN base_employee_org_rel.created_time IS '创建时间';
COMMENT ON COLUMN base_employee_org_rel.updated_by IS '最后更新人';
COMMENT ON COLUMN base_employee_org_rel.updated_time IS '最后更新时间';
COMMENT ON COLUMN base_employee_org_rel.created_org_id IS '创建人组织';


CREATE UNIQUE INDEX uk_employee_org ON base_employee_org_rel(org_id,employee_id);

CREATE TABLE base_dict(
                          id NUMBER(19) NOT NULL,
                          parent_id NUMBER(19) NOT NULL,
                          parent_key VARCHAR2(255) DEFAULT  '' NOT NULL,
                          key_ VARCHAR2(255) DEFAULT  '' NOT NULL,
                          classify CHAR(2) DEFAULT  '20' NOT NULL,
                          name VARCHAR2(255) DEFAULT  '' NOT NULL,
                          state NUMBER(1) DEFAULT  1,
                          remark VARCHAR2(255) DEFAULT  '',
                          sort_value NUMBER(10) DEFAULT  1,
                          icon VARCHAR2(255) DEFAULT  '',
                          css_style VARCHAR2(255) DEFAULT  '',
                          css_class VARCHAR2(255) DEFAULT  '',
                          created_by NUMBER(19),
                          created_time DATE,
                          updated_by NUMBER(19),
                          updated_time DATE,
                          created_org_id NUMBER(19),
                          PRIMARY KEY (id)
);

COMMENT ON TABLE base_dict IS '字典';
COMMENT ON COLUMN base_dict.id IS 'ID';
COMMENT ON COLUMN base_dict.parent_id IS '所属字典';
COMMENT ON COLUMN base_dict.parent_key IS '所属字典标识';
COMMENT ON COLUMN base_dict.key_ IS '标识';
COMMENT ON COLUMN base_dict.classify IS '分类;[10-系统字典 20-业务字典]@Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.System.DICT_CLASSIFY)';
COMMENT ON COLUMN base_dict.name IS '名称';
COMMENT ON COLUMN base_dict.state IS '状态';
COMMENT ON COLUMN base_dict.remark IS '备注';
COMMENT ON COLUMN base_dict.sort_value IS '排序';
COMMENT ON COLUMN base_dict.icon IS '图标';
COMMENT ON COLUMN base_dict.css_style IS 'css样式';
COMMENT ON COLUMN base_dict.css_class IS 'css类元素';
COMMENT ON COLUMN base_dict.created_by IS '创建人';
COMMENT ON COLUMN base_dict.created_time IS '创建时间';
COMMENT ON COLUMN base_dict.updated_by IS '更新人';
COMMENT ON COLUMN base_dict.updated_time IS '更新时间';
COMMENT ON COLUMN base_dict.created_org_id IS '创建人组织';

CREATE TABLE base_parameter(
                               id NUMBER(19) NOT NULL,
                               key_ VARCHAR2(255) DEFAULT  '' NOT NULL,
                               value VARCHAR2(255) DEFAULT  '' NOT NULL,
                               name VARCHAR2(255) DEFAULT  '' NOT NULL,
                               remarks VARCHAR2(255) DEFAULT  '',
                               state NUMBER(1) DEFAULT  1,
                               param_type CHAR(2) DEFAULT  '20',
                               created_by NUMBER(19),
                               created_time DATE,
                               updated_by NUMBER(19),
                               updated_time DATE,
                               created_org_id NUMBER(19),
                               PRIMARY KEY (id)
);

COMMENT ON TABLE base_parameter IS '个性参数';
COMMENT ON COLUMN base_parameter.id IS 'ID';
COMMENT ON COLUMN base_parameter.key_ IS '参数键';
COMMENT ON COLUMN base_parameter.value IS '参数值';
COMMENT ON COLUMN base_parameter.name IS '参数名称';
COMMENT ON COLUMN base_parameter.remarks IS '备注';
COMMENT ON COLUMN base_parameter.state IS '状态';
COMMENT ON COLUMN base_parameter.param_type IS '类型;[10-系统参数 20-业务参数]@Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.System.PARAMETER_TYPE)';
COMMENT ON COLUMN base_parameter.created_by IS '创建人id';
COMMENT ON COLUMN base_parameter.created_time IS '创建时间';
COMMENT ON COLUMN base_parameter.updated_by IS '更新人id';
COMMENT ON COLUMN base_parameter.updated_time IS '更新时间';
COMMENT ON COLUMN base_parameter.created_org_id IS '创建人组织';

CREATE TABLE extend_interface_log(
                                     id NUMBER(19) NOT NULL,
                                     interface_id NUMBER(19) NOT NULL,
                                     name VARCHAR2(255) NOT NULL,
                                     success_count NUMBER(10) DEFAULT  0,
                                     fail_count NUMBER(10) DEFAULT  0,
                                     last_exec_time DATE,
                                     created_time DATE,
                                     created_by NUMBER(19),
                                     updated_time DATE,
                                     updated_by NUMBER(19),
                                     PRIMARY KEY (id)
);

COMMENT ON TABLE extend_interface_log IS '接口执行日志';
COMMENT ON COLUMN extend_interface_log.interface_id IS '接口ID;
#extend_interface';
COMMENT ON COLUMN extend_interface_log.name IS '接口名称';
COMMENT ON COLUMN extend_interface_log.success_count IS '成功次数';
COMMENT ON COLUMN extend_interface_log.fail_count IS '失败次数';
COMMENT ON COLUMN extend_interface_log.last_exec_time IS '最后执行时间';
COMMENT ON COLUMN extend_interface_log.created_time IS '创建时间';
COMMENT ON COLUMN extend_interface_log.created_by IS '创建人';
COMMENT ON COLUMN extend_interface_log.updated_time IS '修改时间';
COMMENT ON COLUMN extend_interface_log.updated_by IS '修改人';


CREATE UNIQUE INDEX UK_EIL_INTERFACE_ID ON extend_interface_log(interface_id);

CREATE TABLE extend_interface_logging(
                                         id NUMBER(19) NOT NULL,
                                         log_id NUMBER(19) NOT NULL,
                                         exec_time DATE NOT NULL,
                                         status CHAR(2) DEFAULT  '01',
                                         params CLOB,
                                         result CLOB,
                                         error_msg CLOB,
                                         biz_id NUMBER(19),
                                         created_time DATE,
                                         created_by NUMBER(19),
                                         updated_time DATE,
                                         updated_by NUMBER(19),
                                         PRIMARY KEY (id)
);

COMMENT ON TABLE extend_interface_logging IS '接口执行日志记录';
COMMENT ON COLUMN extend_interface_logging.log_id IS '接口日志ID;
#extend_interface_log';
COMMENT ON COLUMN extend_interface_logging.exec_time IS '执行时间';
COMMENT ON COLUMN extend_interface_logging.status IS '执行状态;[01-初始化 02-成功 03-失败]@Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.Base.MSG_INTERFACE_LOGGING_STATUS)';
COMMENT ON COLUMN extend_interface_logging.params IS '请求参数';
COMMENT ON COLUMN extend_interface_logging.result IS '接口返回';
COMMENT ON COLUMN extend_interface_logging.error_msg IS '异常信息';
COMMENT ON COLUMN extend_interface_logging.biz_id IS '业务ID';
COMMENT ON COLUMN extend_interface_logging.created_time IS '创建时间';
COMMENT ON COLUMN extend_interface_logging.created_by IS '创建人';
COMMENT ON COLUMN extend_interface_logging.updated_time IS '修改时间';
COMMENT ON COLUMN extend_interface_logging.updated_by IS '修改人';

CREATE TABLE extend_msg(
                           id NUMBER(19) NOT NULL,
                           template_code VARCHAR2(255),
                           type CHAR(2),
                           status VARCHAR2(10),
                           channel VARCHAR2(255),
                           param CLOB,
                           title VARCHAR2(255),
                           content CLOB,
                           send_time DATE,
                           biz_id NUMBER(19),
                           biz_type VARCHAR2(255),
                           remind_mode CHAR(2),
                           author VARCHAR2(255),
                           created_by NUMBER(19),
                           created_time DATE,
                           updated_by NUMBER(19),
                           updated_time DATE,
                           created_org_id NUMBER(19),
                           PRIMARY KEY (id)
);

COMMENT ON TABLE extend_msg IS '消息';
COMMENT ON COLUMN extend_msg.id IS '短信记录ID';
COMMENT ON COLUMN extend_msg.template_code IS '消息模板;
#extend_msg_template';
COMMENT ON COLUMN extend_msg.type IS '消息类型;[01-短信 02-邮件 03-站内信];@Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.Base.MSG_TEMPLATE_TYPE)';
COMMENT ON COLUMN extend_msg.status IS '执行状态;
#TaskStatus{DRAFT:草稿;WAITING:等待执行;SUCCESS:执行成功;FAIL:执行失败}';
COMMENT ON COLUMN extend_msg.channel IS '发送渠道;
#SourceType{APP:应用;SERVICE:服务}';
COMMENT ON COLUMN extend_msg.param IS '参数;需要封装为[{‘key’:‘‘,;’value’:‘‘}, {’key2’:‘‘, ’value2’:‘‘}]格式';
COMMENT ON COLUMN extend_msg.title IS '标题';
COMMENT ON COLUMN extend_msg.content IS '发送内容';
COMMENT ON COLUMN extend_msg.send_time IS '发送时间';
COMMENT ON COLUMN extend_msg.biz_id IS '业务ID';
COMMENT ON COLUMN extend_msg.biz_type IS '业务类型';
COMMENT ON COLUMN extend_msg.remind_mode IS '提醒方式;@Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.Base.NOTICE_REMIND_MODE)[01-待办 02-预警 03-提醒]';
COMMENT ON COLUMN extend_msg.author IS '发布人姓名';
COMMENT ON COLUMN extend_msg.created_by IS '创建人ID';
COMMENT ON COLUMN extend_msg.created_time IS '创建时间';
COMMENT ON COLUMN extend_msg.updated_by IS '最后修改人';
COMMENT ON COLUMN extend_msg.updated_time IS '最后修改时间';
COMMENT ON COLUMN extend_msg.created_org_id IS '创建人所属机构';


CREATE INDEX tempate_id_topic_content ON extend_msg(template_code,title);

CREATE TABLE extend_msg_recipient(
                                     id NUMBER(19) NOT NULL,
                                     msg_id NUMBER(19) NOT NULL,
                                     recipient VARCHAR2(255) NOT NULL,
                                     ext VARCHAR2(255),
                                     created_by NUMBER(19),
                                     created_time DATE,
                                     updated_by NUMBER(19),
                                     updated_time DATE,
                                     PRIMARY KEY (id)
);

COMMENT ON TABLE extend_msg_recipient IS '消息接收人';
COMMENT ON COLUMN extend_msg_recipient.id IS 'ID';
COMMENT ON COLUMN extend_msg_recipient.msg_id IS '任务ID;
#extend_msg';
COMMENT ON COLUMN extend_msg_recipient.recipient IS '接收人;
可能是手机号、邮箱、用户ID等';
COMMENT ON COLUMN extend_msg_recipient.ext IS '扩展信息';
COMMENT ON COLUMN extend_msg_recipient.created_by IS '创建人';
COMMENT ON COLUMN extend_msg_recipient.created_time IS '创建时间';
COMMENT ON COLUMN extend_msg_recipient.updated_by IS '最后修改人';
COMMENT ON COLUMN extend_msg_recipient.updated_time IS '最后修改时间';


CREATE INDEX task_id_tel_num ON extend_msg_recipient(msg_id,recipient);

CREATE TABLE extend_msg_template(
                                    id NUMBER(19) NOT NULL,
                                    interface_id NUMBER(19) NOT NULL,
                                    type CHAR(2) NOT NULL,
                                    code VARCHAR2(255) NOT NULL,
                                    name VARCHAR2(255),
                                    state NUMBER(1),
                                    template_code VARCHAR2(255),
                                    sign VARCHAR2(255),
                                    title VARCHAR2(255),
                                    content CLOB NOT NULL,
                                    script VARCHAR2(255),
                                    param VARCHAR2(255),
                                    remarks VARCHAR2(255),
                                    target_ CHAR(2),
                                    auto_read NUMBER(1) DEFAULT  1,
                                    remind_mode CHAR(2),
                                    url VARCHAR2(255),
                                    created_by NUMBER(19),
                                    created_time DATE,
                                    updated_by NUMBER(19),
                                    updated_time DATE,
                                    PRIMARY KEY (id)
);

COMMENT ON TABLE extend_msg_template IS '消息模板';
COMMENT ON COLUMN extend_msg_template.id IS '模板ID';
COMMENT ON COLUMN extend_msg_template.interface_id IS '接口ID';
COMMENT ON COLUMN extend_msg_template.type IS '消息类型;[01-短信 02-邮件 03-站内信];@Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.Base.MSG_TEMPLATE_TYPE)';
COMMENT ON COLUMN extend_msg_template.code IS '模板标识';
COMMENT ON COLUMN extend_msg_template.name IS '模板名称';
COMMENT ON COLUMN extend_msg_template.state IS '状态';
COMMENT ON COLUMN extend_msg_template.template_code IS '模板编码';
COMMENT ON COLUMN extend_msg_template.sign IS '签名';
COMMENT ON COLUMN extend_msg_template.title IS '标题';
COMMENT ON COLUMN extend_msg_template.content IS '模板内容';
COMMENT ON COLUMN extend_msg_template.script IS '脚本';
COMMENT ON COLUMN extend_msg_template.param IS '模板参数';
COMMENT ON COLUMN extend_msg_template.remarks IS '备注';
COMMENT ON COLUMN extend_msg_template.target_ IS '打开方式;@Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.Base.NOTICE_TARGET)[01-页面 02-弹窗 03-新开窗口]';
COMMENT ON COLUMN extend_msg_template.auto_read IS '自动已读';
COMMENT ON COLUMN extend_msg_template.remind_mode IS '提醒方式;@Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.Base.NOTICE_REMIND_MODE)[01-待办 02-预警 03-提醒]';
COMMENT ON COLUMN extend_msg_template.url IS '跳转地址';
COMMENT ON COLUMN extend_msg_template.created_by IS '创建人ID';
COMMENT ON COLUMN extend_msg_template.created_time IS '创建时间';
COMMENT ON COLUMN extend_msg_template.updated_by IS '最后修改人';
COMMENT ON COLUMN extend_msg_template.updated_time IS '最后修改时间';


CREATE UNIQUE INDEX UK_EX_MSG_TEMPLATE_CODE ON extend_msg_template(code);

CREATE TABLE extend_notice(
                              id NUMBER(19) NOT NULL,
                              msg_id NUMBER(19),
                              biz_id VARCHAR2(64),
                              biz_type VARCHAR2(64),
                              recipient_id NUMBER(19) NOT NULL,
                              remind_mode CHAR(2) NOT NULL,
                              title VARCHAR2(255),
                              content CLOB,
                              author VARCHAR2(255),
                              url VARCHAR2(255),
                              target_ CHAR(2),
                              auto_read NUMBER(1),
                              handle_time DATE,
                              read_time DATE,
                              is_read NUMBER(1) DEFAULT  0,
                              is_handle NUMBER(1) DEFAULT  0,
                              created_time DATE,
                              created_by NUMBER(19),
                              updated_time DATE,
                              updated_by NUMBER(19),
                              created_org_id NUMBER(19),
                              PRIMARY KEY (id)
);

COMMENT ON TABLE extend_notice IS '通知表';
COMMENT ON COLUMN extend_notice.id IS 'ID';
COMMENT ON COLUMN extend_notice.msg_id IS '消息ID';
COMMENT ON COLUMN extend_notice.biz_id IS '业务ID';
COMMENT ON COLUMN extend_notice.biz_type IS '业务类型';
COMMENT ON COLUMN extend_notice.recipient_id IS '接收人';
COMMENT ON COLUMN extend_notice.remind_mode IS '提醒方式;@Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.Base.NOTICE_REMIND_MODE)[01-待办 02-预警 03-提醒]';
COMMENT ON COLUMN extend_notice.title IS '标题';
COMMENT ON COLUMN extend_notice.content IS '内容';
COMMENT ON COLUMN extend_notice.author IS '发布人';
COMMENT ON COLUMN extend_notice.url IS '处理地址';
COMMENT ON COLUMN extend_notice.target_ IS '打开方式;@Echo(api = EchoApi.DICTIONARY_ITEM_FEIGN_CLASS, dictType = EchoDictType.Base.NOTICE_TARGET)[01-页面 02-弹窗 03-新开窗口]';
COMMENT ON COLUMN extend_notice.auto_read IS '自动已读';
COMMENT ON COLUMN extend_notice.handle_time IS '处理时间';
COMMENT ON COLUMN extend_notice.read_time IS '读取时间';
COMMENT ON COLUMN extend_notice.is_read IS '是否已读';
COMMENT ON COLUMN extend_notice.is_handle IS '是否处理';
COMMENT ON COLUMN extend_notice.created_time IS '创建时间';
COMMENT ON COLUMN extend_notice.created_by IS '创建人id';
COMMENT ON COLUMN extend_notice.updated_time IS '最后修改时间';
COMMENT ON COLUMN extend_notice.updated_by IS '最后修改人';
COMMENT ON COLUMN extend_notice.created_org_id IS '所属组织';


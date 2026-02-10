package top.tangyh.lamp.sop.vo.result;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author zuihou
 */
@Data
public class TornaDocInfoViewDTO {
    private Long id;

    /**
     * 文档名称
     */
    private String name;

    /**
     * 文档概述
     */

    private String description;

    /**
     * 0:http,1:dubbo,2:富文本,3:Markdown
     */
    private Byte type;

    /**
     * 访问URL
     */

    private String url;

    /**
     * 版本号
     */
    private String version = "";

    private String docKey;

    /**
     * http方法
     */

    private String httpMethod;

    /**
     * contentType
     */

    private String contentType;


    /**
     * 是否是分类，0：不是，1：是
     */
    private Byte isFolder;

    /**
     * 父节点
     */

    private Long parentId;

    /**
     * 模块id，module.id
     */

    private Long moduleId;

    /**
     * 项目id
     */

    private Long projectId;

    /**
     * 是否使用全局请求参数
     */

    private Byte isUseGlobalHeaders;

    /**
     * 是否使用全局请求参数
     */

    private Byte isUseGlobalParams;

    /**
     * 是否使用全局返回参数
     */

    private Byte isUseGlobalReturns;

    /**
     * 是否请求数组
     */

    private Byte isRequestArray;

    /**
     * 是否返回数组
     */

    private Byte isResponseArray;

    /**
     * 请求数组时元素类型
     */

    private String requestArrayType;

    /**
     * 返回数组时元素类型
     */

    private String responseArrayType;

    /**
     * 文档状态
     */

    private Byte status;

    private String remark;

    private Integer orderIndex;

    /**
     * 数据库字段：gmt_create
     */
    private LocalDateTime gmtCreate;

    /**
     * 数据库字段：gmt_modified
     */
    private LocalDateTime gmtModified;


    private List<TornaDocParamDTO> pathParams = Collections.emptyList();


    private List<TornaDocParamDTO> headerParams = Collections.emptyList();

    private List<TornaDocParamDTO> headerParamsRaw = Collections.emptyList();


    private List<TornaDocParamDTO> queryParams = Collections.emptyList();


    private List<TornaDocParamDTO> requestParams = Collections.emptyList();


    private List<TornaDocParamDTO> responseParams = Collections.emptyList();

    private List<TornaDocParamDTO> errorCodeParams = Collections.emptyList();

    private List<TornaDocParamDTO> globalHeaders = Collections.emptyList();
    private List<TornaDocParamDTO> globalParams = Collections.emptyList();
    private List<TornaDocParamDTO> globalReturns = Collections.emptyList();

    private String errorCodeInfo;

    private List<TornaDocInfoViewDTO> children = Collections.emptyList();

    public String getDocName() {
        return name;
    }

    public String getDocTitle() {
        return name;
    }

}

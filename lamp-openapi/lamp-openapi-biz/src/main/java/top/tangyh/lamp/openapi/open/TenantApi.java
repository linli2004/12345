package top.tangyh.lamp.openapi.open;

import com.gitee.sop.support.annotation.Open;
import com.gitee.sop.support.context.OpenContext;
import com.gitee.sop.support.dto.FileData;
import top.tangyh.lamp.base.entity.user.BaseEmployee;
import top.tangyh.lamp.openapi.open.req.ProductSaveRequest;
import top.tangyh.lamp.openapi.open.resp.ProductResponse;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 演示 - 查询租户下的数据
 *
 * @author zuihou
 */
public interface TenantApi {

    /**
     * 查询员工
     *
     * @apiNote 根据id，查询本租户下的员工数据
     *
     *
     * @param id 员工id
     * @param name 名称
     * @param context 上下文参数
     */
    @Open("openapi.employee.get")
    BaseEmployee getEmployeeById(Integer id, String name, OpenContext context);

    /**
     * 演示单文件上传
     * @param request 业务参数
     * @param file 文件对象
     * @return 返回参数
     */
    @Open("openapi.upload")
    ProductResponse upload(ProductSaveRequest request, FileData file);

    /**
     * 演示多文件上传
     * @param request 业务参数
     * @param idCardFront 文件对象
     * @param idCardBack 文件对象
     * @return 返回参数
     */
    @Open("openapi.upload.more")
    ProductResponse upload2(
            ProductSaveRequest request,
            @NotNull(message = "身份证正面必填") FileData idCardFront,
            @NotNull(message = "身份证背面必填") FileData idCardBack
    );


    /**
     * 演示多文件上传2
     * @param request 业务参数
     * @param files 文件对象
     * @return 返回参数
     */
    @Open("openapi.upload.list")
    ProductResponse upload3(
            ProductSaveRequest request,
            @Size(min = 2, message = "最少上传2个文件")
            List<FileData> files
    );

    /**
     * 下载
     * @param id 业务参数
     * @return 文件
     */
    @Open("openapi.download")
    FileData download(Integer id);

}

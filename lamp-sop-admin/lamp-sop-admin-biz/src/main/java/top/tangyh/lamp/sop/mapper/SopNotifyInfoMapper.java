package top.tangyh.lamp.sop.mapper;


import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.springframework.stereotype.Repository;
import top.tangyh.basic.base.mapper.SuperMapper;
import top.tangyh.lamp.sop.entity.SopNotifyInfo;

/**
 * <p>
 * Mapper 接口
 * 回调信息
 * </p>
 *
 * @author zuihou
 * @date 2025-12-17 15:38:07
 * @create [2025-12-17 15:38:07] [zuihou] [代码生成器生成]
 */
@Repository
@InterceptorIgnore(tenantLine = "true")
public interface SopNotifyInfoMapper extends SuperMapper<SopNotifyInfo> {


}

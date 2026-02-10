package top.tangyh.lamp.sop.service;

import cn.hutool.core.lang.tree.Tree;
import top.tangyh.basic.base.service.SuperService;
import top.tangyh.lamp.sop.entity.SopDocApp;
import top.tangyh.lamp.sop.entity.SopDocInfo;
import top.tangyh.lamp.sop.vo.result.DocInfoViewVO;

import java.util.List;


/**
 * <p>
 * 业务接口
 * 文档信息
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
public interface SopDocInfoService extends SuperService<Long, SopDocInfo> {
    List<Tree<Long>> tree(Long docAppId, Integer isPublic);

    Boolean publish(Long id, Integer isPublish);

    void syncAppDoc(Long docAppId);

    void syncDoc(Long docInfoId);

    void syncDocInfo(SopDocApp docApp, Long docInfoId);

    DocInfoViewVO getDocView(Long id);
}



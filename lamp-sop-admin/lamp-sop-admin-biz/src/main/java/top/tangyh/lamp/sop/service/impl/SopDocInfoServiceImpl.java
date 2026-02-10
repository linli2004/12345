package top.tangyh.lamp.sop.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.NodeParser;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.exception.BizException;
import top.tangyh.basic.utils.ArgumentAssert;
import top.tangyh.basic.utils.BeanPlusUtil;
import top.tangyh.basic.utils.StrPool;
import top.tangyh.lamp.model.enumeration.BooleanEnum;
import top.tangyh.lamp.sop.entity.SopDocApp;
import top.tangyh.lamp.sop.entity.SopDocContent;
import top.tangyh.lamp.sop.entity.SopDocInfo;
import top.tangyh.lamp.sop.enums.DocSourceTypeEnum;
import top.tangyh.lamp.sop.manager.SopDocAppManager;
import top.tangyh.lamp.sop.manager.SopDocContentManager;
import top.tangyh.lamp.sop.manager.SopDocInfoManager;
import top.tangyh.lamp.sop.manager.SopSysConfigManager;
import top.tangyh.lamp.sop.manager.TornaClient;
import top.tangyh.lamp.sop.service.SopDocInfoService;
import top.tangyh.lamp.sop.vo.TreeNode;
import top.tangyh.lamp.sop.vo.query.DocIdsParam;
import top.tangyh.lamp.sop.vo.result.DocInfoViewVO;
import top.tangyh.lamp.sop.vo.result.DocSettingDTO;
import top.tangyh.lamp.sop.vo.result.SopDocInfoTreeVO;
import top.tangyh.lamp.sop.vo.result.TornaDocDTO;
import top.tangyh.lamp.sop.vo.result.TornaDocInfoDTO;
import top.tangyh.lamp.sop.vo.result.TornaDocInfoViewDTO;
import top.tangyh.lamp.sop.vo.result.TornaDocParamDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务实现类
 * 文档信息
 * </p>
 *
 * @author zuihou
 * @since 2025-05-07 10:52:47
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SopDocInfoServiceImpl extends SuperServiceImpl<SopDocInfoManager, Long, SopDocInfo> implements SopDocInfoService {
    private final SopDocAppManager sopDocAppManager;
    private final SopSysConfigManager sopSysConfigManager;
    private final SopDocContentManager sopDocContentManager;
    private final TornaClient tornaClient;


    @Override
    public List<Tree<Long>> tree(Long docAppId, Integer isPublic) {
        List<SopDocInfo> list = superManager.list(Wraps.<SopDocInfo>lbQ()
                .eq(SopDocInfo::getDocAppId, docAppId)
                .eq(SopDocInfo::getIsPublish, isPublic)
        );

        List<SopDocInfoTreeVO> voList = BeanPlusUtil.copyToList(list, SopDocInfoTreeVO.class);

        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("docId");
        List<Tree<Long>> treeList = TreeUtil.build(voList, 0L, treeNodeConfig, new SopNodeParser());
        return treeList == null ? Collections.emptyList() : treeList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean publish(Long id, Integer isPublish) {
        SopDocInfo docInfo = this.getById(id);
        ArgumentAssert.notNull(docInfo, "文档不存在");

        // 如果是文件夹,发布下面所有的文档

        boolean result;
        if (BooleanEnum.TRUE.eq(docInfo.getIsFolder())) {
            List<SopDocInfo> children = superManager.list(Wraps.<SopDocInfo>lbQ().eq(SopDocInfo::getParentId, docInfo.getDocId()));
            Set<Long> ids = children.stream().map(SopDocInfo::getId).collect(Collectors.toSet());

            result = superManager.update(Wraps.<SopDocInfo>lbU().set(SopDocInfo::getIsPublish, isPublish).in(SopDocInfo::getId, ids));
        } else {
            // 发布单个文档
            result = superManager.update(Wraps.<SopDocInfo>lbU().set(SopDocInfo::getIsPublish, isPublish).eq(SopDocInfo::getId, docInfo.getId()));
        }

        // 发布一个接口自动发布所属应用
        Long docAppId = docInfo.getDocAppId();
        if (BooleanEnum.TRUE.eq(isPublish)) {
            sopDocAppManager.update(Wraps.<SopDocApp>lbU().set(SopDocApp::getIsPublish, isPublish).eq(SopDocApp::getId, docAppId));
        } else {
            // 如果应用下的接口都未发布,应用也改成未发布

            long count = superManager.count(Wraps.<SopDocInfo>lbQ().eq(SopDocInfo::getDocAppId, docAppId).eq(SopDocInfo::getIsFolder, BooleanEnum.FALSE.getInteger()).eq(SopDocInfo::getIsPublish, BooleanEnum.TRUE.getInteger()));
            if (count == 0) {
                sopDocAppManager.update(Wraps.<SopDocApp>lbU().set(SopDocApp::getIsPublish, BooleanEnum.FALSE.getInteger()).eq(SopDocApp::getId, docAppId));
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncAppDoc(Long docAppId) {
        SopDocApp docApp = sopDocAppManager.getById(docAppId);
        this.syncDocInfo(docApp, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncDoc(Long docInfoId) {
        SopDocInfo docInfo = superManager.getById(docInfoId);
        SopDocApp docApp = sopDocAppManager.getById(docInfo.getDocAppId());
        this.syncDocInfo(docApp, docInfoId);
    }

    /**
     * 同步远程文档
     *
     * @param docApp    应用
     * @param docInfoId 同步某个文档,如果为null则同步整个应用文档
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncDocInfo(SopDocApp docApp, Long docInfoId) {
        Long docAppId = docApp.getId();
        List<SopDocInfo> docInfoList = superManager.list(Wraps.<SopDocInfo>lbQ().eq(SopDocInfo::getDocAppId, docAppId));
        Map<String, SopDocInfo> nameVersionMap = docInfoList.stream()
                .collect(Collectors.toMap(docInfo -> docInfo.getDocName() + StrPool.COLON + docInfo.getDocVersion(), Function.identity(), (v1, v2) -> v2));

        String token = docApp.getToken();
        // add doc
        DocIdsParam docIdsParam = buildSearchParam(docInfoId);
        TornaDocDTO tornaDocDTO = tornaClient.execute("doc.list", docIdsParam, token, TornaDocDTO.class);
        List<TornaDocInfoDTO> docList = tornaDocDTO.getDocList();
        if (CollectionUtils.isEmpty(docList)) {
            return;
        }

        List<SopDocInfo> updateList = new ArrayList<>();
        for (TornaDocInfoDTO tornaDocInfoDTO : docList) {
            String key = buildKey(tornaDocInfoDTO);
            SopDocInfo docInfo = nameVersionMap.get(key);
            // 需要修改的文档
            if (docInfo != null) {
                docInfo.setDocId(tornaDocInfoDTO.getId());
                docInfo.setDocTitle(tornaDocInfoDTO.getName());
                docInfo.setDocCode("");
                if (BooleanEnum.TRUE.eq(tornaDocInfoDTO.getIsFolder())) {
                    docInfo.setIsPublish(BooleanEnum.TRUE.getInteger());
                    docInfo.setDocName(tornaDocInfoDTO.getName());
                }
                docInfo.setDocId(tornaDocInfoDTO.getId());
                docInfo.setDocType(tornaDocInfoDTO.getType().intValue());
                docInfo.setDescription(tornaDocInfoDTO.getDescription());
                docInfo.setIsFolder(tornaDocInfoDTO.getIsFolder());
                docInfo.setParentId(tornaDocInfoDTO.getParentId());
                updateList.add(docInfo);
            }
            superManager.updateBatchById(updateList);
        }

        // 新增的文档
        List<SopDocInfo> saveList = docList.stream()
                .filter(tornaDocInfoDTO -> {
                    String key = buildKey(tornaDocInfoDTO);
                    return !nameVersionMap.containsKey(key);
                })
                .map(tornaDocInfoDTO -> {
                    SopDocInfo docInfo = new SopDocInfo();
                    docInfo.setDocAppId(docAppId);
                    docInfo.setDocId(tornaDocInfoDTO.getId());
                    docInfo.setDocTitle(tornaDocInfoDTO.getName());
                    docInfo.setDocCode("");
                    docInfo.setDocType(tornaDocInfoDTO.getType().intValue());
                    docInfo.setSourceType(DocSourceTypeEnum.TORNA.getValue());
                    if (BooleanEnum.TRUE.eq(tornaDocInfoDTO.getIsFolder())) {
                        docInfo.setIsPublish(BooleanEnum.TRUE.getInteger());
                        docInfo.setDocName(tornaDocInfoDTO.getName());
                    } else {
                        docInfo.setIsPublish(BooleanEnum.FALSE.getInteger());
                        docInfo.setDocName(tornaDocInfoDTO.getUrl());
                    }
                    docInfo.setDocVersion(tornaDocInfoDTO.getVersion());
                    docInfo.setDescription(tornaDocInfoDTO.getDescription());
                    docInfo.setIsFolder(tornaDocInfoDTO.getIsFolder());
                    docInfo.setParentId(tornaDocInfoDTO.getParentId());
                    return docInfo;
                })
                .collect(Collectors.toList());
        superManager.saveBatch(saveList);

        Set<Long> docIds = docList.stream().map(TornaDocInfoDTO::getId).collect(Collectors.toSet());
        this.syncContent(docApp, docIds);
    }

    private void syncContent(SopDocApp docApp, Set<Long> docIds) {
        List<SopDocInfo> list = superManager.list(Wraps.<SopDocInfo>lbQ().eq(SopDocInfo::getDocAppId, docApp.getId()).in(SopDocInfo::getDocId, docIds));
        Map<Long, String> docIdMap = this.getContentMap(docApp.getToken(), docIds);
        for (SopDocInfo docInfo : list) {
            String content = docIdMap.getOrDefault(docInfo.getDocId(), "");
            sopDocContentManager.saveContent(docInfo.getId(), content);
        }
    }

    /**
     * 批量获取Torna文档内容
     *
     * @param token  token
     * @param docIds Torna文档id
     * @return key:文档id, value:文档内容
     */
    private Map<Long, String> getContentMap(String token, Collection<Long> docIds) {
        // 获取torna文档信息
        List<TornaDocInfoViewDTO> tornaDocInfoViewList = tornaClient.executeList(
                "doc.details",
                new DocIdsParam(docIds),
                token,
                TornaDocInfoViewDTO.class
        );
        for (TornaDocInfoViewDTO docInfoViewDTO : tornaDocInfoViewList) {
            convertTree(docInfoViewDTO);
        }
        return tornaDocInfoViewList.stream()
                .collect(Collectors.toMap(TornaDocInfoViewDTO::getId, JSON::toJSONString, (v1, v2) -> v1));
    }

    private void convertTree(TornaDocInfoViewDTO tornaDocInfoViewDTO) {
        List<TornaDocParamDTO> requestParams = tornaDocInfoViewDTO.getRequestParams();
        List<TornaDocParamDTO> responseParams = tornaDocInfoViewDTO.getResponseParams();
        List<TornaDocParamDTO> requestTree = TreeNode.convertTree(requestParams, 0L);
        List<TornaDocParamDTO> responseTree = TreeNode.convertTree(responseParams, 0L);

        tornaDocInfoViewDTO.setRequestParams(requestTree);
        tornaDocInfoViewDTO.setResponseParams(responseTree);
    }

    private String buildKey(TornaDocInfoDTO tornaDocInfoDTO) {
        return BooleanEnum.TRUE.eq(tornaDocInfoDTO.getIsFolder()) ?
                tornaDocInfoDTO.getName() + ":" + tornaDocInfoDTO.getVersion()
                : tornaDocInfoDTO.getUrl() + ":" + tornaDocInfoDTO.getVersion();
    }

    private DocIdsParam buildSearchParam(Long docInfoId) {
        if (docInfoId == null) {
            return null;
        }
        DocIdsParam docIdsParam = new DocIdsParam();
        SopDocInfo docInfo = superManager.getById(docInfoId);
        List<Long> docIdList = new ArrayList<>();
        docIdList.add(docInfo.getDocId());
        // 如果是文件夹,找下面的子文档
        if (BooleanEnum.TRUE.eq(docInfo.getIsFolder())) {
            List<SopDocInfo> docList = superManager.list(Wraps.<SopDocInfo>lbQ().eq(SopDocInfo::getParentId, docInfo.getDocId()));
            List<Long> docIds = docList.stream().map(SopDocInfo::getId).toList();
            docIdList.addAll(docIds);
        }
        docIdsParam.setDocIds(docIdList);
        return docIdsParam;
    }

    @Override
    public DocInfoViewVO getDocView(Long id) {
        TornaDocInfoViewDTO tornaDocInfoViewDTO = getDocDetail(id);
        DocSettingDTO docInfoConfigDTO = sopSysConfigManager.getDocSetting();

        DocInfoViewVO docInfoViewDTO = new DocInfoViewVO();
        docInfoViewDTO.setDocInfoView(tornaDocInfoViewDTO);
        docInfoViewDTO.setDocInfoConfig(docInfoConfigDTO);
        return docInfoViewDTO;
    }

    public TornaDocInfoViewDTO getDocDetail(Long id) {
        SopDocInfo docInfo = this.getById(id);
        if (docInfo == null || !BooleanEnum.TRUE.eq(docInfo.getIsPublish())) {
            throw new BizException("文档不存在");
        }

        SopDocContent sopDocContent = sopDocContentManager.getOne(Wraps.<SopDocContent>lbQ().eq(SopDocContent::getDocInfoId, docInfo.getId()), false);
        if (sopDocContent == null) {
            return new TornaDocInfoViewDTO();
        }

        return JSON.parseObject(sopDocContent.getContent(), TornaDocInfoViewDTO.class);
    }

    private static class SopNodeParser implements NodeParser<SopDocInfoTreeVO, Long> {
        @Override
        public void parse(SopDocInfoTreeVO treeNode, Tree<Long> tree) {
            Map<String, Object> map = BeanUtil.beanToMap(treeNode);
            tree.putAll(map);

            tree.setId(treeNode.getDocId());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getWeight());
            tree.setName(treeNode.getDocName());

            //扩展字段
            final Map<String, Object> extra = treeNode.getExtra();
            if (MapUtil.isNotEmpty(extra)) {
                extra.forEach(tree::putExtra);
            }
        }
    }

}



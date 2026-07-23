package top.tangyh.lamp.base.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.lamp.base.entity.WorkReadRecord;
import top.tangyh.lamp.base.manager.WorkReadRecordManager;
import top.tangyh.lamp.base.service.WorkReadRecordService;
import top.tangyh.lamp.common.constant.DsConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@DS(DsConstant.BASE_TENANT)
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class WorkReadRecordServiceImpl extends SuperServiceImpl<WorkReadRecordManager, Long, WorkReadRecord> implements WorkReadRecordService {

    @Override
    public List<Boolean> checkRead(List<WorkReadRecord> workReadRecords) {
        List<WorkReadRecord> result = superManager.selectExistingRecords(workReadRecords);
        Set<String> existSet = result.stream()
                .map(r -> r.getReadKey() + "_" + r.getRoleCode() + "_" + r.getReadRandomStr())
                .collect(Collectors.toSet());

        return workReadRecords.stream()
                .map(r -> {
                    String targetKey = r.getReadKey() + "_" + r.getRoleCode() + "_" + r.getReadRandomStr();
                    return existSet.contains(targetKey);
                })
                .collect(Collectors.toList());
    }
}

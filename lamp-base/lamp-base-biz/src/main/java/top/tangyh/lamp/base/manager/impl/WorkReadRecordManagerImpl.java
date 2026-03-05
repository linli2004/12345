package top.tangyh.lamp.base.manager.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.manager.impl.SuperManagerImpl;
import top.tangyh.lamp.base.entity.WorkReadRecord;
import top.tangyh.lamp.base.manager.WorkReadRecordManager;
import top.tangyh.lamp.base.mapper.WorkReadRecordMapper;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorkReadRecordManagerImpl extends SuperManagerImpl<WorkReadRecordMapper, WorkReadRecord> implements WorkReadRecordManager {
    @Override
    public List<WorkReadRecord> selectExistingRecords(List<WorkReadRecord> records) {
        return baseMapper.selectExistingRecords(records);
    }
}

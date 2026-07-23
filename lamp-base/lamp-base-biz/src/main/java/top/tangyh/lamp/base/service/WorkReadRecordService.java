package top.tangyh.lamp.base.service;

import top.tangyh.basic.base.service.SuperService;
import top.tangyh.lamp.base.entity.WorkReadRecord;

import java.util.List;

public interface WorkReadRecordService extends SuperService<Long, WorkReadRecord> {

    List<Boolean> checkRead(List<WorkReadRecord> workReadRecords);
}

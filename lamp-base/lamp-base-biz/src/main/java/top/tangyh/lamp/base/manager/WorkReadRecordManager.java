package top.tangyh.lamp.base.manager;

import org.apache.ibatis.annotations.Param;
import top.tangyh.basic.base.manager.SuperManager;
import top.tangyh.lamp.base.entity.WorkReadRecord;

import java.util.List;

public interface WorkReadRecordManager extends SuperManager<WorkReadRecord> {

    List<WorkReadRecord> selectExistingRecords(List<WorkReadRecord> records);

}

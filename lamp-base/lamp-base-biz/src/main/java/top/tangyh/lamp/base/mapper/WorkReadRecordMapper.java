package top.tangyh.lamp.base.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.tangyh.basic.base.mapper.SuperMapper;
import top.tangyh.lamp.base.entity.WorkReadRecord;

import java.util.List;

@Repository
public interface WorkReadRecordMapper extends SuperMapper<WorkReadRecord> {
    List<WorkReadRecord> selectExistingRecords(@Param("list") List<WorkReadRecord> records);
}

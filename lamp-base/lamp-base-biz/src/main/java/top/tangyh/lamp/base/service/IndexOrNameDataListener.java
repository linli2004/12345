package top.tangyh.lamp.base.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import top.tangyh.lamp.base.entity.NormalWorkOrder;
import top.tangyh.lamp.base.entity.NormalWorkOrderExcel;

import java.util.List;

@Slf4j
public class IndexOrNameDataListener extends AnalysisEventListener<NormalWorkOrderExcel> {
    private static final int BATCH_COUNT = 50;
    private List<NormalWorkOrder> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    @Override
    public void invoke(NormalWorkOrderExcel data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        NormalWorkOrder normalWorkOrder = BeanUtil.toBean(data, NormalWorkOrder.class);
        cachedDataList.add(normalWorkOrder);
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());

        log.info("存储数据库成功！");
    }
}

package top.tangyh.lamp.test.service2.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.tangyh.basic.base.service.impl.SuperServiceImpl;
import top.tangyh.lamp.test.entity.DefGenTestSimple;
import top.tangyh.lamp.test.manager2.SimpleManager;
import top.tangyh.lamp.test.service2.SimpleService;
import top.tangyh.lamp.test.vo.save.DefGenTestSimpleSaveVO;


/**
 * <p>
 * 业务接口
 * 测试单表
 * </p>
 *
 * @author zuihou
 * @date 2022-04-15 15:36:45
 * @create [2022-04-15 15:36:45] [zuihou] [代码生成器生成]
 */
@Service
@RequiredArgsConstructor
public class SimpleServiceImpl extends SuperServiceImpl<SimpleManager, Long, DefGenTestSimple>
        implements SimpleService {


    @Override
    public <SaveVO> DefGenTestSimple save(SaveVO saveVO) {
        DefGenTestSimpleSaveVO sssss = (DefGenTestSimpleSaveVO) saveVO;
        return super.save(saveVO);
    }
//    @Override
//    public <SaveVO> DefGenTestSimple save(DefGenTestSimple saveVO) {
//        return super.save(saveVO);
//    }
}



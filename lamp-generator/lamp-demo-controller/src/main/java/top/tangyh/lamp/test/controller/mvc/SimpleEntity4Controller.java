package top.tangyh.lamp.test.controller.mvc;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.annotation.log.WebLog;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperWriteController;
import top.tangyh.lamp.test.entity.DefGenTestSimple;
import top.tangyh.lamp.test.service2.SimpleService;
import top.tangyh.lamp.test.vo.save.DefGenTestSimpleSaveVO;
import top.tangyh.lamp.test.vo.update.DefGenTestSimpleUpdateVO;

import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mvc4")
@Tag(name = "mvc4")
@WebLog(enabled = false)
public class SimpleEntity4Controller extends SuperWriteController<SimpleService, Long, DefGenTestSimple, DefGenTestSimpleSaveVO, DefGenTestSimpleUpdateVO> {


    @Operation(summary = "删除222")
    @DeleteMapping("/delete")
    public R<Boolean> delete22(@RequestBody List<Long> ids) {
        R<Boolean> result = handlerDelete(ids);
        if (result.getDefExec()) {
            return R.success(getSuperService().removeByIds(ids));
        }
        return result;
    }

    @Operation(summary = "put222")
    @PutMapping("/put")
    public R<Boolean> put222(@RequestBody List<Long> ids) {
        R<Boolean> result = handlerDelete(ids);
        if (result.getDefExec()) {
            return R.success(getSuperService().removeByIds(ids));
        }
        return result;
    }
}

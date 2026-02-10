package top.tangyh.lamp.test.controller.page;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.annotation.log.WebLog;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.controller.SuperController;
import top.tangyh.basic.base.request.PageParams;
import top.tangyh.basic.database.mybatis.conditions.Wraps;
import top.tangyh.basic.database.mybatis.conditions.query.QueryWrap;
import top.tangyh.lamp.generator.entity.DefGenTableColumn;
import top.tangyh.lamp.generator.service.DefGenTableColumnService;
import top.tangyh.lamp.generator.vo.query.DefGenTableColumnPageQuery;
import top.tangyh.lamp.generator.vo.result.DefGenTableColumnResultVO;
import top.tangyh.lamp.generator.vo.save.DefGenTableColumnSaveVO;
import top.tangyh.lamp.generator.vo.update.DefGenTableColumnUpdateVO;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/page")
@Tag(name = "分页示例")
@WebLog(enabled = false)
public class PageDemoController extends SuperController<DefGenTableColumnService, Long, DefGenTableColumn, DefGenTableColumnSaveVO,
        DefGenTableColumnUpdateVO, DefGenTableColumnPageQuery, DefGenTableColumnResultVO> {

    @Override
    public QueryWrap<DefGenTableColumn> handlerWrapper(DefGenTableColumn model, PageParams<DefGenTableColumnPageQuery> params) {
        QueryWrap<DefGenTableColumn> query = Wraps.q(model, params.getExtra(), getEntityClass());
        query.lambda().le(DefGenTableColumn::getId, 300L);
        return query;
    }

    @PostMapping(value = "/page2")
    public R<IPage<DefGenTableColumnResultVO>> page2(@RequestBody PageParams<DefGenTableColumnPageQuery> params) {
        return super.page(params);
    }
}

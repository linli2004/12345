package top.tangyh.lamp.test.controller;


import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.tangyh.basic.base.R;
import top.tangyh.basic.base.entity.SuperEntity;
import top.tangyh.basic.jackson.JsonUtil;
import top.tangyh.lamp.test.vo.save.DateVO;
import top.tangyh.lamp.test.vo.save.FormValidatorSaveVO;
import top.tangyh.lamp.test.vo.save.SerializeVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/anyTenant/demo")
@Tag(name = "功能示例")
public class DemoController {

    @PostMapping("/save")
    public R<FormValidatorSaveVO> bodyPos3(@Validated @RequestBody FormValidatorSaveVO data) {  // 6
        log.info("post1={}", data);

        log.info("jackson {}", JsonUtil.toJson(data));
        log.info("fastjson {}", JSONObject.toJSONString(data));
        log.info("hutool json {}", JSONUtil.toJsonStr(data));

        return R.success(data);
    }

    @PostMapping("/update")
    public R<FormValidatorSaveVO> update(@Validated(SuperEntity.Update.class) @RequestBody FormValidatorSaveVO data) {  // 6
        log.info("post1={}", data);

        log.info("jackson {}", JsonUtil.toJson(data));
        log.info("fastjson {}", JSONObject.toJSONString(data));
        log.info("hutool json {}", JSONUtil.toJsonStr(data));

        return R.success(data);
    }

    @PostMapping("/post2")
    public R<SerializeVO> bodyPos2(@RequestBody SerializeVO data) {  // 6
        log.info("post1={}", data);

        log.info("jackson {}", JsonUtil.toJson(data));
        log.info("fastjson {}", JSONObject.toJSONString(data));
        log.info("hutool json {}", JSONUtil.toJsonStr(data));

        return R.success(data);
    }


    @PostMapping("/post1")
    public R<DateVO> bodyPos1(@RequestBody DateVO data) {
        log.info("post1={}", data);
        return R.success(data);
    }

    /***
     * 调用这个接口会报错
     * @param data
     * @return
     */
    @GetMapping("/get1")
    public R<DateVO> get1(DateVO data) {
        log.info("get1={}", data);
        return R.success(data);
    }

    @GetMapping("/get2")
    public R<DateVO> get2(@RequestParam(required = false, value = "date") Date date,
                          @RequestParam(required = false, value = "dt") LocalDateTime dt, // 3
                          @RequestParam(required = false, value = "d") LocalDate d,
                          @RequestParam(required = false, value = "t") LocalTime t) {
        DateVO dateVO = new DateVO();
        dateVO.setDate(date);
        dateVO.setLocalDateTime(dt);
        dateVO.setLocalDate(d);
        dateVO.setLocalTime(t);
        return R.success(dateVO);
    }

}

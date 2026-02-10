package top.tangyh.lamp.bbb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BbService {
    public void bbb() {
        log.debug("bbb={}", 123);
        log.trace("bbb={}", 123);
        log.info("bbb={}", 123);
        log.warn("bbb={}", 123);
        log.error("bbb={}", 123);
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            log.error("出错了 {}", 123, e);
        }
    }
}

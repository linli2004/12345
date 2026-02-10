package top.tangyh.lamp.aaa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AaaService {
    public void aaa() {
        log.debug("aaa={}", 123);
        log.trace("aaa={}", 123);
        log.info("aaa={}", 123);
        log.warn("aaa={}", 123);
        log.error("aaa={}", 123);
    }
}

package top.tangyh.lamp.tenant.dynamic.processor;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import jakarta.servlet.http.HttpServletRequest;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.tangyh.basic.context.ContextUtil;

/**
 * 从请求头或者Thread变量中获取参数
 *
 * @author zuihou
 * @date 2020年03月15日11:12:54
 */
public class DsHeaderOrThreadProcessor extends DsProcessor {

    /**
     * 请求头或线程变量 前缀
     */
    private static final String HEADER_PREFIX = "#headerThread";

    @Override
    public boolean matches(String key) {
        return key.startsWith(HEADER_PREFIX);
    }

    @Override
    public String doDetermineDatasource(MethodInvocation invocation, String key) {
        String ds = ContextUtil.get(key.substring(HEADER_PREFIX.length() + 1));
        if (ds != null && !"".equals(ds)) {
            return ds;
        }
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        HttpServletRequest request = requestAttributes.getRequest();
        return request.getHeader(key.substring(HEADER_PREFIX.length() + 1));

    }

}

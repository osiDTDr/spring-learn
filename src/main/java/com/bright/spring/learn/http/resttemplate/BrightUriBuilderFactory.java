package com.bright.spring.learn.http.resttemplate;

import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;
import java.util.Map;

/**
 * 在 RestTemplate默认uri处理结束后，将uri中的域名部分（componentId.segmentId）通过服务寻址后翻译成ip:port
 *
 * @author zhengyuan
 * @since 2021/01/11
 */
public class BrightUriBuilderFactory extends DefaultUriBuilderFactory {

    @SuppressWarnings("NullableProblems")
    @Override
    public URI expand(String uriTemplate, Map<String, ?> uriVars) {
        URI uri = super.expand(uriTemplate, uriVars);
        return replace(uri);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public URI expand(String uriTemplate, Object... uriVars) {
        URI uri = super.expand(uriTemplate, uriVars);
        return replace(uri);
    }

    /**
     * 后置 uri 处理
     *
     * @param uri uri
     * @return Processed uri
     */
    private URI replace(URI uri) {
        return uri;
    }
}

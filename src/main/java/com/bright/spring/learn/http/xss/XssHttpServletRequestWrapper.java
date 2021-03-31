package com.bright.spring.learn.http.xss;

import com.alibaba.fastjson.JSON;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * ServletRequest包装类,对request做XSS过滤处理
 * 对于不可信的输入可以采用 apache.commons.lang3.StringEscapeUtils 对输入字符串进行过滤，将’<’ ‘>’ ‘*’ 三个字符转换成html编码格式 < & &gt. 防止而已的HTML注入攻击
 *
 * @author zhengyuan
 * @since 2021/02/07
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        return StringEscapeUtils.escapeHtml4(super.getHeader(name));
    }

    @Override
    public String getQueryString() {
        return StringEscapeUtils.escapeHtml4(super.getQueryString());
    }

    @Override
    public String getParameter(String name) {
        return StringEscapeUtils.escapeHtml4(super.getParameter(name));
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapeValues = new String[length];
            for (int i = 0; i < length; i++) {
                escapeValues[i] = StringEscapeUtils.escapeHtml4(values[i]);
            }
            return escapeValues;
        }
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        String str = getRequestBody(super.getInputStream());
        Map<String, Object> map = JSON.parseObject(str, Map.class);
        Map<String, Object> resultMap = new HashMap<>(map.size());
        for (String key : map.keySet()) {
            Object val = map.get(key);
            if (map.get(key) instanceof String) {
                resultMap.put(key, StringEscapeUtils.escapeHtml4(val.toString()));
            } else {
                resultMap.put(key, val);
            }
        }
        str = JSON.toJSONString(resultMap);
        final ByteArrayInputStream bais = new ByteArrayInputStream(str.getBytes());
        return new ServletInputStream() {
            @Override
            public int read() {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }
        };
    }

    private String getRequestBody(InputStream stream) {
        String line;
        StringBuilder body = new StringBuilder();

        // 读取POST提交的数据内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        try {
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body.toString();
    }
}

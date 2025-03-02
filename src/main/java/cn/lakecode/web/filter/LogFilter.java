
package cn.lakecode.web.filter;

import cn.lakecode.web.constant.Constants;
import cn.lakecode.web.utils.HttpUtil;
import cn.lakecode.web.utils.RandomUtil;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class LogFilter implements Filter {

    private final Set<String> ignoreUrls = new HashSet<>();

    public LogFilter() {
        ignoreUrls.add("/favicon.ico");
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestToUse = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseToUse = new ContentCachingResponseWrapper((HttpServletResponse) response);

        String traceId = requestToUse.getHeader(Constants.TRACE_ID);
        if (!StringUtils.hasLength(traceId)) {
            traceId = RandomUtil.randomStr(16);
        }
        MDC.put(Constants.TRACE_ID, traceId);
        filterChain.doFilter(requestToUse, responseToUse);

        String ip = HttpUtil.getIp(requestToUse);
        String uri = requestToUse.getRequestURI();
        String params = formatParams(requestToUse.getParameterMap());
        String body = formatBody(requestToUse);
        if (!ignoreUrls.contains(uri)) {
            byte[] byteArray = responseToUse.getContentAsByteArray();
            String resp = null;
            if (byteArray.length > 0) {
                resp = new String(byteArray, StandardCharsets.UTF_8);
            }
            StringBuilder logStr = new StringBuilder();
            logStr.append("url: ")
                    .append(uri).append(",")
                    .append("ip [").append(ip).append("],");
            if (params != null) {
                logStr.append("params [").append(params).append("],");
            }
            if (body != null) {
                logStr.append("body [").append(body).append("],");
            }
            if (resp != null) {
                logStr.append("resp [").append(resp).append("],");
            }
            log.info("{}", logStr.substring(0, logStr.length() - 1));
            responseToUse.copyBodyToResponse();
        }
    }

    private String formatParams(Map<String, String[]> map) {
        if (!map.isEmpty()) {
            JSONObject p = new JSONObject();
            map.forEach((k, v) -> {
                if (v.length == 0) {
                    p.put(k, "");
                } else if (v.length == 1) {
                    p.put(k, v[0]);
                } else {
                    p.put(k, v);
                }
            });
            return p.toJSONString();
        }
        return null;
    }

    private String formatBody(ContentCachingRequestWrapper request) {
        if (request.getContentType() == null || !request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
            return null;
        }
        if (request.getContentLength() == 0) {
            return null;
        }
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            return new String(content, StandardCharsets.UTF_8);
        }
        return null;
    }
}

package cn.lakecode.web.handler;

import cn.lakecode.web.constant.Constants;
import cn.lakecode.web.annotation.EncryptBody;
import cn.lakecode.web.resp.R;
import cn.lakecode.web.resp.ResponseCode;
import cn.lakecode.web.utils.DataUtils;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;


@Slf4j
@RestControllerAdvice
public class ResponseEncryptResolver implements ResponseBodyAdvice<Object> {

    @Value("${app.aes-key:1234567890123456}")
    private String aesKey;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        EncryptBody annotation = returnType.getDeclaringClass().getAnnotation(EncryptBody.class);
        if (annotation == null) {
            annotation = Objects.requireNonNull(returnType.getMethod()).getAnnotation(EncryptBody.class);
        }
        return annotation != null;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (body != null) {
            if (body instanceof R<?> r) {
                log.info("respData:{}", JSONObject.toJSONString(r));
                Object data = r.getData();
                // 响应数据加密
                if (data != null && ResponseCode.SUCCESS.test(r.getErrCode())) {
                    httpServletRequest.setAttribute(Constants.DATA_RESPONSE, JSONObject.toJSONString(body));
                    body = R.ok(DataUtils.encrypt(data, aesKey));
                }
            }
        }
        return body;
    }
}

package cn.lakecode.web.handler;

import cn.lakecode.web.constant.Constants;
import cn.lakecode.web.annotation.DecryptBody;
import cn.lakecode.web.exception.RException;
import cn.lakecode.web.resp.ResponseCode;
import cn.lakecode.web.utils.DataUtils;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver;

import java.util.List;

@Component
@Slf4j
public class RequestDecryptResolver extends AbstractMessageConverterMethodArgumentResolver {

    @Value("${app.aes-key:1234567890123456}")
    private String aesKey;

    public RequestDecryptResolver(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(DecryptBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String d = webRequest.getParameter(Constants.DATA);
        if (StringUtils.hasLength(d)) {
            try {
                Object decrypt = DataUtils.decrypt(d, aesKey, parameter.getDeclaringClass());
                log.info("reqData:{}", JSONObject.toJSONString(decrypt));
                return decrypt;
            } catch (Exception e) {
                throw new RException(ResponseCode.DATA_ERROR);
            }
        }
        throw new RException(ResponseCode.PARAMS_ERROR);
    }
}

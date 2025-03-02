package cn.lakecode.web.manager.plat;

import cn.lakecode.web.manager.plat.bo.PlatUser;
import cn.lakecode.web.utils.HttpUtil;
import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WxLoginService implements PlatLoginService {

    private final String appId;

    private final String appSecret;

    public WxLoginService(String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
    }

    @Override
    public PlatUser login(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("appid", appId);
        params.put("secret", appSecret);
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        String resp = HttpUtil.get(url, params);
        if (resp != null) {
            JSONObject json = JSONObject.parseObject(resp);
            String openId = json.getString("openid");
            return new PlatUser(openId);
        }
        return null;
    }
}

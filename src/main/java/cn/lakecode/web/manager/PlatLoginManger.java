package cn.lakecode.web.manager;


import cn.lakecode.web.manager.plat.WxLoginService;
import cn.lakecode.web.manager.plat.bo.PlatType;
import cn.lakecode.web.manager.plat.bo.PlatUser;

public class PlatLoginManger {

    public static PlatUser login(PlatType platType, String appId, String appSecret, String code) {
        if (PlatType.WX.equals(platType)) {
            WxLoginService wxLoginService = new WxLoginService(appId, appSecret);
            return wxLoginService.login(code);
        }
        throw new RuntimeException("not support plat login");
    }
}

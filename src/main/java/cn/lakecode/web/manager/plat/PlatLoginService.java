package cn.lakecode.web.manager.plat;


import cn.lakecode.web.manager.plat.bo.PlatUser;

public interface PlatLoginService {

    /**
     * 登录
     *
     * @param code code
     * @return user
     */
    PlatUser login(String code);

}

package cn.lakecode.web.bo;


import lombok.Data;

@Data
public class MailConf {

    private String host;

    private Integer port;

    private String username;

    private String password;

    private String from;

    private String fromName;

}

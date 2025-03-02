package cn.lakecode.web.security;

import lombok.Data;

import java.util.List;

@Data
public class Authentication {

    private Long uid;

    private String client;

    private String token;

    private String role;

    private boolean supRole;

    private List<String> permissions;
}

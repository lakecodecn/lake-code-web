package cn.lakecode.web.security;

public class SecurityHolder {

    private static final ThreadLocal<Authentication> CONTEXT = new ThreadLocal<>();

    public static void setAuthentication(Authentication authentication) {
        CONTEXT.set(authentication);
    }

    public static Authentication getAuthentication() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}

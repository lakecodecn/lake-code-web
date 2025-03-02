package cn.lakecode.web.gencode.model;

import lombok.Getter;

import java.util.function.Function;

@Getter
public class ClassInfo {

    private final String entityClassName;

    private final String serviceClassName;

    private final String serviceImplClassName;

    private final String mapperClassName;

    private final String controllerClassName;

    private final String entityPackage;

    private final String servicePackage;

    private final String serviceImplPackage;

    private final String mapperPackage;

    private final String controllerPackage;

    private final String reqPackage;

    private final String reqClassName;

    private final String reqPageClassName;

    private final String serviceVarName;

    private final String moduleName;

    public ClassInfo(String name, String packageName, Function<String, String> className) {
        this.entityClassName = className.apply(name);
        this.entityPackage = packageName + ".model.entity";
        this.serviceClassName = "I" + entityClassName + "Service";
        this.servicePackage = packageName + ".service";
        this.serviceImplClassName = entityClassName + "ServiceImpl";
        this.serviceImplPackage = packageName + ".service.impl";
        this.mapperClassName = entityClassName + "Mapper";
        this.mapperPackage = packageName + ".mapper";
        this.controllerClassName = entityClassName + "Controller";
        this.controllerPackage = packageName + ".controller";

        this.reqPackage = packageName + ".model.req";
        this.reqClassName = entityClassName + "Req";
        this.reqPageClassName = entityClassName + "PageReq";
        this.serviceVarName = Character.toLowerCase(serviceClassName.charAt(1)) + serviceClassName.substring(2);

        this.moduleName = this.entityClassName.toLowerCase();
    }

}

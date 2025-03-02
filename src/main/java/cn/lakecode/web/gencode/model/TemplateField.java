package cn.lakecode.web.gencode.model;

import lombok.Data;

@Data
public class TemplateField {

    private Boolean pk = false;

    private String comment;

    private String classType;

    private String displayName;

    private String name;
}

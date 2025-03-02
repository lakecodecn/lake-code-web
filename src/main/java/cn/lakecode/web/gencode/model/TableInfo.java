package cn.lakecode.web.gencode.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TableInfo {

    private String name;

    private String comment;

    private List<TableField> fields;

    public TableInfo(String name, String comment) {
        this.name = name;
        this.comment = comment;
        this.fields = new ArrayList<TableField>();
    }
}

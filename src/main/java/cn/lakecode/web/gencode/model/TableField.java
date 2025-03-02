package cn.lakecode.web.gencode.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class TableField {

    private String primaryKey;

    private String name;

    private String type;

    private String comment;
}

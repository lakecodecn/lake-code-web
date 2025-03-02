package ${packageName};

import com.baomidou.mybatisplus.annotation.IdType;
<#if hasPk>
import com.baomidou.mybatisplus.annotation.TableId;
</#if>
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
<#if importPackages?? && (importPackages?size > 0)>
<#list importPackages as pkg>
import ${pkg};
</#list>
</#if>
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * ${comment}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Getter
@Setter
@TableName("${table}")
public class ${className} implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    <#list fields as field>


    <#if field.pk>
    @TableId(value = "${field.name}", type = IdType.AUTO)
    </#if>
    <#if field.comment?has_content>
    /**
     * ${field.comment}
     */
    </#if>
    private ${field.classType} ${field.displayName};
    </#list>
}

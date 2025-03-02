package ${packageName};


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
<#if importPackages?? && (importPackages?size > 0)>
<#list importPackages as pkg>
import ${pkg};
</#list>
</#if>

/**
 * <p>
 * ${comment}Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
public interface ${className} extends BaseMapper<${entityClassName}> {


}

package ${packageName};


import cn.lakecode.web.resp.PageVo;
<#if importPackages?? && (importPackages?size > 0)>
<#list importPackages as pkg>
import ${pkg};
</#list>
</#if>

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * ${comment}服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
@AllArgsConstructor
public class ${className} extends ServiceImpl<${mapperClassName}, ${entityClassName}> implements ${interfaceClassName} {

    @Override
    public void add(${reqClassName} req) {

    }

    @Override
    public void edit(${reqClassName} req) {

    }

    @Override
    public void del(List<Long> ids) {

    }

    @Override
    public PageVo<${entityClassName}> findPage(Page<${entityClassName}> page, ${reqPageClassName} req) {
        return null;
    }

}

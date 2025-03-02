package ${packageName};

import cn.lakecode.web.resp.PageVo;
<#if importPackages?? && (importPackages?size > 0)>
<#list importPackages as pkg>
import ${pkg};
</#list>
</#if>
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * ${comment}服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
public interface ${className} extends IService<${entityClassName}> {


    /**
     * 新增
     *
     * @param req 参数
     */
    void add(${reqClassName} req);

    /**
     * 编辑
     *
     * @param req 参数
     */
    void edit(${reqClassName} req);

    /**
     * 删除
     *
     * @param ids ids
     */
    void del(List<Long> ids);

    /**
     * 分页查询
     *
     * @return 分页数据
     */
    PageVo<${entityClassName}> findPage(Page<${entityClassName}> page, ${reqPageClassName} req);
}

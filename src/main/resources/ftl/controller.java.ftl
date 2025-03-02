package ${packageName};


<#if importPackages?? && (importPackages?size > 0)>
<#list importPackages as pkg>
import ${pkg};
</#list>
</#if>
import cn.lakecode.web.BaseController;
import cn.lakecode.web.annotation.HasAuthority;
import cn.lakecode.web.resp.R;
import cn.lakecode.web.resp.PageVo;
import cn.lakecode.web.utils.ArrayUtils;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * ${comment}前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@RestController
@RequestMapping("/api/admin/${module}")
@AllArgsConstructor
@HasAuthority("admin:${module}")
public class ${className} extends BaseController {

    private final ${serviceClassName} ${serviceVarName};

    @PostMapping("add")
    public R<?> add(@Validated({${reqClassName}.Add.class}) ${reqClassName} req) {
        ${serviceVarName}.add(req);
        return R.ok();
    }

    @PostMapping("edit")
    public R<?> edit(@Validated({${reqClassName}.Update.class}) ${reqClassName} req) {
        ${serviceVarName}.edit(req);
        return R.ok();
    }

    @PostMapping("del")
    public R<?> del(String idStr) {
        List<Long> ids = ArrayUtils.toLongList(idStr);
        ${serviceVarName}.del(ids);
        return R.ok();
    }

    @PostMapping("list")
    public R<?> list(${reqPageClassName} req) {
        PageVo<${entityClassName}> page = ${serviceVarName}.findPage(getPage(), req);
        return R.ok(page);
    }
}

package cn.lakecode.web;

import cn.lakecode.web.security.Authentication;
import cn.lakecode.web.security.SecurityHolder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

public class BaseController {

    private HttpServletRequest request;


    @ModelAttribute
    void setRequest(HttpServletRequest request) {
        this.request = request;
    }


    public <T> Page<T> getPage() {
        long pageNumber = 1L;
        long pageSize = 20L;

        String currentPage = request.getParameter("currentPage");
        if (StringUtils.hasText(currentPage)) {
            pageNumber = Long.parseLong(currentPage);
        }
        String size = request.getParameter("pageSize");
        if (StringUtils.hasText(size)) {
            pageSize = Long.parseLong(size);
        }
        return new Page<>(pageNumber, pageSize);
    }


    public Authentication authentication() {
        return SecurityHolder.getAuthentication();
    }


}

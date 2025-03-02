package cn.lakecode.web.resp;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

@Data
public class PageVo<T> {

    private Long total;

    private List<T> list;

    public PageVo(Page<T> page) {
        this.total = page.getTotal();
        this.list = page.getRecords();
    }

    public PageVo(Long total, List<T> data) {
        this.total = total;
        this.list = data;
    }
}

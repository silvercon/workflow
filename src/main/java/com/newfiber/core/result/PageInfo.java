package com.newfiber.core.result;

import com.github.pagehelper.Page;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import lombok.Data;

/**
 * @author : xieyj
 * @since : 2019-01-26 18:03
 */
@Data
@SuppressWarnings("unchecked")
public class PageInfo<T> implements Serializable {

    private static final long serialVersionUID = 2552429215424205489L;

    /**
     * 当前页
     */
    @ApiModelProperty(name = "pageNum", value = "当前页")
    private int pageNum;

    /**
     * 每页的数量
     */
    @ApiModelProperty(name = "pageSize", value = "每页的数量")
    private int pageSize;

    /**
     * 当前页的条数
     */
    @ApiModelProperty(name = "size", value = "当前页的条数")
    private int size;

    /**
     * 总记录数
     */
    @ApiModelProperty(name = "total", value = "总记录数")
    protected long total;

    /**
     * 总页数
     */
    @ApiModelProperty(name = "pages", value = "总页数")
    private int pages;

    /**
     * 列表查询
     */
    @ApiModelProperty(name = "list", value = "列表数据")
    protected List<T> list;

    public PageInfo() {
    }

    public PageInfo(List<T> list) {
        this(list, 8);
    }

    public PageInfo(List<T> list, int navigatePages) {
        if (list instanceof Page) {
            Page page = (Page) list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.pages = page.getPages();
            this.list = page;
            this.size = page.size();
            this.total = page.getTotal();
        } else if (list instanceof Collection) {
            this.pageNum = 1;
            this.pageSize = list.size();
            this.pages = this.pageSize > 0 ? 1 : 0;
            this.list = list;
            this.size = list.size();
            this.total = (long) list.size();
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("PageInfo{");
        sb.append("pageNum=").append(this.pageNum);
        sb.append(", pageSize=").append(this.pageSize);
        sb.append(", size=").append(this.size);
        sb.append(", total=").append(this.total);
        sb.append(", pages=").append(this.pages);
        sb.append(", list=").append(this.list);
        sb.append('}');
        return sb.toString();
    }
}

    
    
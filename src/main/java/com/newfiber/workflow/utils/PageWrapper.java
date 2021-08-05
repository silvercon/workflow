package com.newfiber.workflow.utils;

import com.newfiber.core.base.WorkflowPageReq;
import com.newfiber.core.result.PageInfo;
import java.util.List;

/**
 * @author : silver
 * @since : 2019-06-12 16:37
 */
public class PageWrapper<T> extends PageInfo<T> {

    public PageWrapper(List<T> list, WorkflowPageReq pageReq) {
        this.list = list;
        this.total = list.size();

        super.setPageSize(pageReq.getPageSize());
        super.setPageNum(pageReq.getPageNum());

        int pages = list.size() % pageReq.getPageSize() == 0 ?
                list.size() / pageReq.getPageSize() : list.size() / pageReq.getPageSize() + 1;
        super.setPages(pages);
    }

    public List<T> getPage(Integer queryPageNum) {
        int start = Math.min((queryPageNum - 1) * getPageSize(), (int) getTotal());
        int end = queryPageNum >= getPages() ? list.size() : queryPageNum * getPageSize();

        return list.subList(start, end);
    }

}

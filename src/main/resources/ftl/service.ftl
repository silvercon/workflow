package ${serviceUrl};

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import ${entityUrl}.${entityName};
import ${requestUrl}.${entityName}CreateReq;
import ${requestUrl}.${entityName}ListReq;
import ${requestUrl}.${entityName}ModifyReq;
import ${requestUrl}.${entityName}PageReq;

import ${commonUrl}.result.PageInfo;

/**
 * ${entityComment}Service
 *
 * @author : ${author}
 * @since : ${createTime}
 */
public interface ${entityName}Service extends IService<${entityName}> {

    /**
     * 新增${entityComment}
     *
     * @param req 新增${entityComment}入参
     */
    void create(${entityName}CreateReq req);

    /**
     * 删除${entityComment}
     *
     * @param id 主键ID
     */
     void remove(${idType} id);

    /**
     * 修改${entityComment}
     *
     * @param req 修改${entityComment}入参
     */
    void modify(${entityName}ModifyReq req);

    /**
     * 详情查询${entityComment}
     *
     * @param id 主键ID
     * @return ${entityComment}详情数据
     */
     ${entityName} detail(${idType} id);

    /**
     * 分页查询${entityComment}
     *
     * @param req 分页查询${entityComment}入参
     * @return ${entityComment}分页数据
     */
    PageInfo<${entityName}> page(${entityName}PageReq req);

    /**
     * 列表查询${entityComment}
     *
     * @param req 列表查询${entityComment}入参
     * @return ${entityComment}列表数据
     */
     List<${entityName}> list(${entityName}ListReq req);

}
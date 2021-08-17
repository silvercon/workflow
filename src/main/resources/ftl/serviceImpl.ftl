package ${serviceImplUrl};

import ${mapperUrl}.${entityName}Dao;

import ${entityUrl}.${entityName};

import ${requestUrl}.${entityName}CreateReq;
import ${requestUrl}.${entityName}ListReq;
import ${requestUrl}.${entityName}ModifyReq;
import ${requestUrl}.${entityName}PageReq;

import com.github.pagehelper.PageHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${serviceUrl}.${entityName}Service;

import ${commonUrl}.result.PageInfo;

import java.util.List;
import java.util.Arrays;

import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * ${entityComment}ServiceImpl
 *
 * @author : ${author}
 * @since : ${createTime}
 */
@Service
public class ${entityName}ServiceImpl extends ServiceImpl<${entityName}Dao, ${entityName}> implements ${entityName}Service {

    @Resource
    private ${entityName}Dao ${objectName}Dao;

    /**
     * 新增${entityComment}
     *
     * @param req 新增${entityComment}入参
     */
    @Override
    public void create(${entityName}CreateReq req) {
        ${entityName} ${objectName} = new ${entityName}();
        BeanUtils.copyProperties(req, ${objectName});

        this.save(${objectName});
    }

    /**
     * 删除${entityComment}
     *
     * @param id 主键ID
     */
    @Override
    public void remove(${idType} id) {
        this.removeById(id);
    }

    /**
     * 修改${entityComment}
     *
     * @param req 修改${entityComment}入参
     */
    @Override
    public void modify(${entityName}ModifyReq req) {
        ${entityName} ${objectName} = new ${entityName}();
        BeanUtils.copyProperties(req, ${objectName});

        this.updateById(${objectName});
    }

    /**
     * 详情查询${entityComment}
     *
     * @param id 主键ID
     * @return ${entityComment}对象
     */
    @Override
    public ${entityName} detail(${idType} id) {
        return this.getById(id);
    }

    /**
    * 分页查询${entityComment}
    *
    * @param req 分页查询${entityComment}入参
    * @return 分页${entityComment}对象
    */
    @Override
    public PageInfo<${entityName}> page(${entityName}PageReq req) {
        ${entityName} condition = new ${entityName}();
        BeanUtils.copyProperties(req, condition);
        PageHelper.startPage(req.getPageNum(), req.getPageSize(), req.getOrderBy());

        List<${entityName}> list = ${objectName}Dao.selectByCondition(condition);
        return new PageInfo<>(list);
    }

    /**
    * 列表查询${entityComment}
    *
    * @param req 列表查询${entityComment}入参
    * @return 列表${entityComment}对象
    */
    @Override
    public List<${entityName}> list(${entityName}ListReq req) {
        ${entityName} condition = new ${entityName}();
        BeanUtils.copyProperties(req, condition);

        return ${objectName}Dao.selectByCondition(condition);
    }

}
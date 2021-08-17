package ${controllerUrl};

import ${commonUrl}.result.PageInfo;
import java.util.List;

import ${entityUrl}.${entityName};
import ${requestUrl}.${entityName}CreateReq;
import ${requestUrl}.${entityName}ListReq;
import ${requestUrl}.${entityName}ModifyReq;
import ${requestUrl}.${entityName}PageReq;
import ${serviceUrl}.${entityName}Service;

import ${commonUrl}.result.ResultCode;
import ${commonUrl}.result.Result;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import javax.validation.Valid;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ${entityComment}Controller
 *
 * @author : ${author}
 * @since : ${createTime}
 */
@RestController
@Api(value = "${entityComment}管理", tags = "${entityComment}管理")
@RequestMapping("/${requestMappingUrl}")
public class ${entityName}Controller{

    @Resource
    private ${entityName}Service ${objectName}Service;

    @ApiOperation(value = "新增${entityComment}对象", position = 10)
    @PostMapping(value = "/create")
    public Result<Object> create(@RequestBody @Valid ${entityName}CreateReq request) {
        ${objectName}Service.create(request);
        return new Result<>(ResultCode.SUCCESS);
    }

    @ApiOperation(value = "删除${entityComment}对象", position = 20)
    @PostMapping("/remove/{id}")
    public Result<Object> remove(@PathVariable("id") @Valid ${idType} id) {
        ${objectName}Service.remove(id);
        return new Result<>(ResultCode.SUCCESS);
    }

    @ApiOperation(value = "修改${entityComment}对象", position = 30)
    @PostMapping(value = "/modify")
    public Result<Object> modify(@RequestBody @Valid ${entityName}ModifyReq request) {
        ${objectName}Service.modify(request);
        return new Result<>(ResultCode.SUCCESS);
    }

    @ApiOperation(value = "查询${entityComment}对象", position = 40)
    @PostMapping("/detail/{id}")
    public Result<${entityName}> detail(@PathVariable("id") @Valid ${idType} id) {
        return new Result<>(ResultCode.SUCCESS, ${objectName}Service.detail(id));
    }

    @ApiOperation(value = "分页条件查询${entityComment}", position = 50)
    @PostMapping(value = "/page")
    public Result<PageInfo<${entityName}>> page(@RequestBody @Valid ${entityName}PageReq request) {
        return new Result<>(ResultCode.SUCCESS, ${objectName}Service.page(request));
    }

    @ApiOperation(value = "列表条件查询${entityComment}", position = 70)
    @PostMapping(value = "/list")
    public Result<List<${entityName}>> list(@RequestBody @Valid ${entityName}ListReq request) {
        return new Result<>(ResultCode.SUCCESS, ${objectName}Service.list(request));
    }

}
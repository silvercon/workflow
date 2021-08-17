package ${mapperUrl};

import ${entityUrl}.${entityName};
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * ${entityComment}Mapper
 *
 * @author : ${author}
 * @since : ${createTime}
 */
@Mapper
public interface ${entityName}Dao extends BaseMapper<${entityName}>{

    /**
     * 根据条件查询
     *
     * @param condition 查询条件
     * @return ${entityComment}列表
     */
    List<${entityName}> selectByCondition(${entityName} condition);

}
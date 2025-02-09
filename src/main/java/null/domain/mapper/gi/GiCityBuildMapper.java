package null.domain.mapper.gi;

import org.apache.ibatis.annotations.Mapper;

import null.domain.model.gi.CGiCityBuild;
import null.domain.model.gi.GiCityBuild;
import com.yxbear.core.db.mybatis.BaseMapper;

@Mapper
public interface GiCityBuildMapper extends BaseMapper<Integer, GiCityBuild, CGiCityBuild> {

}
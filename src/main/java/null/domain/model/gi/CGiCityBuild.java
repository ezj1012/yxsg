package null.domain.model.gi;

import com.yxbear.core.bean.Condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CGiCityBuild implements Condition {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer[] ids;

    private Integer startId;

    private Integer endId;

    private String bid CfgBuild;

    private String[] bid CfgBuilds;

    private String bid CfgBuildEqual;

    private String cityId GiCity;

    private String[] cityId GiCitys;

    private String cityId GiCityEqual;

    private Integer lv;

    private Integer[] lvs;

    private Integer startLv;

    private Integer endLv;

    private Integer status;

    private Integer[] statuss;

    private Integer startStatus;

    private Integer endStatus;

}
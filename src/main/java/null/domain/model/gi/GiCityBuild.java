package null.domain.model.gi;

import com.yxbear.core.bean.EntityBean;

import lombok.Data;

@Data
public class GiCityBuild implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /**  */
    private String bid CfgBuild;

    /**  */
    private String cityId GiCity;

    /**  建筑等级  */
    private Integer lv;

    /**  0:正常,1:升级中,2:降级  */
    private Integer status;



}
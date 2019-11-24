package me.zhengjie.modules.vehicle.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * @author Aaron Tang
 * @date 2019-9-10
 */
@Data
public class VehicleDTO implements Serializable {
    private String plate;
    private String type;
    private String url;
}

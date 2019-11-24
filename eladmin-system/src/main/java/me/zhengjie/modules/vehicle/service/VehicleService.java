package me.zhengjie.modules.vehicle.service;

import me.zhengjie.modules.system.domain.User;
import me.zhengjie.domain.Picture;
import me.zhengjie.modules.security.security.JwtUser;
import me.zhengjie.modules.system.service.dto.UserDTO;
import me.zhengjie.modules.system.service.dto.UserQueryCriteria;
import me.zhengjie.modules.vehicle.service.dto.VehicleDTO;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@CacheConfig(cacheNames = "user")
public interface VehicleService {
    /**
     * 判断车辆的车牌和车型
     * 
     * @param username
     * @param url
     */
    @CacheEvict(allEntries = true)
    VehicleDTO uploadVehicle(Picture picture);
}

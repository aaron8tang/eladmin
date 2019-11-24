package me.zhengjie.modules.vehicle.service.impl;

import me.zhengjie.modules.monitor.service.RedisService;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.domain.Picture;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.exception.EntityNotFoundException;
import me.zhengjie.modules.vehicle.repository.VehicleRepository;
import me.zhengjie.modules.vehicle.service.VehicleService;
import me.zhengjie.modules.vehicle.service.dto.VehicleDTO;
import me.zhengjie.utils.BaiduUtils;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.Optional;

/**
 * @author Aaron Tang
 * @date 2019-9-10
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private RedisService redisService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VehicleDTO uploadVehicle(Picture picture) {
    	/*
    	 * 先转换成base64的字符串，然后获取车牌和车型
    	 * */
    	/*
    	 * 获取车牌信息
    	 * */
    	VehicleDTO dto = new VehicleDTO();
    	String plate = "test";
    	String type = "test";
    	if(picture != null) {
	    	if(StringUtils.isNotEmpty(picture.getBase64String()))
	    	{
	    		plate = BaiduUtils.licensePlate(picture.getBase64String());
	    		type = BaiduUtils.vehicleType(picture.getBase64String());
	    	}
	    	
	    	dto.setUrl(picture.getUrl());
    	}
    	dto.setPlate(plate);
    	dto.setType(type);
    	
    	return dto;
    }
}

package com.tianze.dao;

import com.tianze.entity.CanInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-12-17 13:57)
 * Version: 1.0
 * Updated:
 */
public interface CanInfoDao {
    public List<Map<String, Object>> selectVehicle(String sql);

    public List<CanInfoEntity> selectVehicle();
}

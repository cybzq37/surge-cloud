package com.surge.common.mybatis.helper;

import cn.hutool.core.convert.Convert;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.utils.SpringUtils;
import com.surge.common.mybatis.enums.DataBaseType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * 数据库助手
 *
 * @author lichunqing
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataBaseHelper {

    private static final DynamicRoutingDataSource DS = SpringUtils.getBean(DynamicRoutingDataSource.class);

    /**
     * 获取当前数据库类型
     */
    public static DataBaseType getDataBaseType() {
        DataSource dataSource = DS.determineDataSource();
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            String databaseProductName = metaData.getDatabaseProductName();
            return DataBaseType.find(databaseProductName);
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public static boolean isMySql() {
        return DataBaseType.MY_SQL == getDataBaseType();
    }

    public static boolean isOracle() {
        return DataBaseType.ORACLE == getDataBaseType();
    }

    public static boolean isPostgerSql() {
        return DataBaseType.POSTGRE_SQL == getDataBaseType();
    }

    public static boolean isSqlServer() {
        return DataBaseType.SQL_SERVER == getDataBaseType();
    }

    public static String findInSet(Object fieldValue, String column) {
        DataBaseType dataBasyType = getDataBaseType();
        String fieldValueStr = Convert.toStr(fieldValue);
        if (dataBasyType == DataBaseType.SQL_SERVER) {
            // charindex(',100,' , ',0,100,101,') <> 0
            return "charindex('," + fieldValueStr + ",' , ','+" + column + "+',') <> 0";
        } else if (dataBasyType == DataBaseType.POSTGRE_SQL) {
            // (select position(',100,' in ',0,100,101,')) <> 0
            return "(select position('," + fieldValueStr + ",' in ','||" + column + "||',')) <> 0";
        } else if (dataBasyType == DataBaseType.ORACLE) {
            // instr(',0,100,101,' , ',100,') <> 0
            return "instr(','||" + column + "||',' , '," + fieldValueStr + ",') <> 0";
        }
        // find_in_set('100' , '0,100,101')
        return "find_in_set('" + fieldValueStr + "' , " + column + ") <> 0";
    }
}

package com.surge.device.rules;

import com.surge.common.core.exception.ServiceException;
import com.surge.common.gis.util.GeomUtils;
import com.surge.device.domain.entity.DeviceTrace;
import com.surge.device.domain.entity.ElectricFence;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class NoExitRule implements IElectricFenceRule {

    @Override
    public boolean check(ElectricFence electricFence, DeviceTrace deviceTrace) {
        // 获取围栏范围
        GeometryFactory geometryFactory = new GeometryFactory();
        String geoJsonScope = electricFence.getGeoJson().toString();
        String scope = GeomUtils.geoJsonToWkt(geoJsonScope);
        WKTReader wktReader = new WKTReader();
        Geometry fenceScope = null;
        try {
            fenceScope = wktReader.read(scope);
        } catch (ParseException e) {
            throw new ServiceException("读取电子围栏范围错误");
        }
        Point point = geometryFactory.createPoint(new Coordinate(deviceTrace.getLon(), deviceTrace.getLat()));
        // 判断一个几何对象是否完全包含另一个几何对象
        return !fenceScope.contains(point);
    }
}

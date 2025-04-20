package com.surge.common.gis.util;

import com.surge.common.core.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.GeodeticCalculator;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.operation.linemerge.LineMerger;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;

@Slf4j
public class GeomUtils {

    public static void validateGeoJSON(String geoJson) {
        GeometryJSON gjson = new GeometryJSON();
        try(Reader reader = new StringReader(geoJson)) {
            gjson.read(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String lonlatToGeoJson(double lon, double lat) {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(lon, lat));
        GeometryJSON geometryJSON = new GeometryJSON();
        return geometryJSON.toString(point);
    }

    public static double[] geoJsonToLonlat(String geoJson) {
        double[] lonLat = new double[2];
        GeometryJSON geometryJSON = new GeometryJSON();
        Geometry geometry = null;
        try(Reader reader = new StringReader(geoJson)) {
            geometry = geometryJSON.read(reader);
        } catch (IOException e) {
            log.error("geojson读取错误", e);
            throw new ServiceException("geojson读取错误");
        }
        if(geometry == null) {
            throw new ServiceException("geojson读取错误，内容为空");
        }
        if (geometry instanceof Point) {
            lonLat[0] = geometry.getCoordinate().x;
            lonLat[1] = geometry.getCoordinate().y;
        } else {
            throw new ServiceException("geojson转经纬度错误，不是Point类型");
        }
        return lonLat;
    }


    public static String geoJsonToWkt(String geoJson) {
        String wkt = null;
        GeometryJSON gjson = new GeometryJSON();
        Reader reader = new StringReader(geoJson);
        try {
            Geometry geometry = gjson.read(reader);
            wkt = geometry.toText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wkt;
    }

    public static String wktToGeoJson(String wkt) {
        String geoJson = null;
        try(StringWriter writer = new StringWriter()) {
            Geometry geometry = new WKTReader().read(wkt);
            GeometryJSON g = new GeometryJSON();
            g.write(geometry, writer);
            geoJson = writer.toString();
        } catch (Exception e) {
            log.error("wtk转geoJson错误", e);
            throw new ServiceException("wtk转geoJson错误");
        }
        return geoJson;
    }

    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        // 创建GeodeticCalculator对象
        GeodeticCalculator calculator = new GeodeticCalculator();

        // 设置计算的起始点和结束点
        calculator.setStartingGeographicPoint(x1, y1);
        calculator.setDestinationGeographicPoint(x2, y2);

        // 计算两点之间的距离
        return calculator.getOrthodromicDistance();
    }

    public static Double getGeometryLength(String geoJson) {
        try {
            GeometryJSON gjson = new GeometryJSON();
            Geometry geometry = gjson.read(geoJson);
            if (geometry instanceof LineString) {
                double length = geometry.getLength(); // 平面长度计算
                return length;
            }
            if(geometry instanceof MultiLineString) {
                double length = geometry.getLength();
                return length;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Double getGeometryLengthSphere(String geoJson) {
        try {
            GeometryJSON gjson = new GeometryJSON();
            Geometry geometry = gjson.read(geoJson);
            if (geometry instanceof LineString || geometry instanceof MultiLineString) {
                Coordinate[] coordinates = geometry.getCoordinates();
                GeodeticCalculator calculator = new GeodeticCalculator();

                // 累计球面距离
                double totalDistance = 0.0;

                // 遍历每一段线段，计算球面距离
                for (int i = 0; i < coordinates.length - 1; i++) {
                    Coordinate start = coordinates[i];
                    Coordinate end = coordinates[i + 1];

                    // 设置起点和终点
                    calculator.setStartingGeographicPoint(start.x, start.y);
                    calculator.setDestinationGeographicPoint(end.x, end.y);

                    // 获取两点间的球面距离（米）
                    totalDistance += calculator.getOrthodromicDistance();
                }
                return totalDistance;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String MergeLines(List<String> lines) {
        LineMerger lineMerger = new LineMerger();
        for(String line : lines) {
            GeometryJSON geometryJSON = new GeometryJSON();
            Geometry geometry = null;
            try(Reader reader = new StringReader(line)) {
                geometry = geometryJSON.read(reader);
            } catch (IOException e) {
                log.error("geojson读取错误", e);
                throw new ServiceException("geojson读取错误");
            }
            if(geometry == null) {
                throw new ServiceException("geojson读取错误，内容为空");
            }
            if (geometry instanceof LineString) {
                lineMerger.add(geometry);
            } else {
                throw new ServiceException("geojson转经纬度错误，不是Point类型");
            }
        }


        Collection mergedLines = lineMerger.getMergedLineStrings();
        // 如果结果是单一线段集合，则合并为一个 LineString
        LineString finalMergedLine = null;
        if (mergedLines.size() == 1) {
            finalMergedLine = (LineString) mergedLines.iterator().next();
        } else {
            throw new ServiceException("线段合并错误");
//            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
//            MultiLineString multiLineString = geometryFactory.createMultiLineString((LineString[]) mergedLines.toArray(new LineString[0]));
        }

        String geoJson = null;
        try(StringWriter writer = new StringWriter()) {
            GeometryJSON g = new GeometryJSON();
            g.write(finalMergedLine, writer);
            geoJson = writer.toString();
        } catch (Exception e) {
            log.error("wtk转geoJson错误", e);
            throw new ServiceException("wtk转geoJson错误");
        }
        return geoJson;
    }


}

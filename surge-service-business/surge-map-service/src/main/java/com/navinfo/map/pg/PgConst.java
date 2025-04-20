package com.surge.map.pg;

public class PgConst {

    // 动态表结构map常用的key
    public static final String Column_Field = "field";
    public static final String Column_PKID = "pkid";
    public static final String Column_Name = "name";
    public static final String Column_String = "stirng";
    public static final String Column_Type = "type";
    public static final String Geometry = "geometry";
    public static final String GEOMETRY = "Geometry";
    public static final String The_Geom = "the_geom";
    public static final String geom = "geom";
    public static final String Data_Count = "dataSize";
    public static final String Data_Type = "dataType";
    public static final String KML_SRS = "CRS:84";

    // 默认的编码格式
    protected static final String DefaultEncoding = "UTF-8";
    // 坐标类型
    protected static final String Line = "line";
    protected static final String Vector_Line = "vector-line";
    protected static final String Point = "point";
    protected static final String Vector_Point = "vector-point";
    protected static final String Polygon = "polygon";
    protected static final String Vector_Polygon = "vector-polygon";
    protected static final String Multiline = "vector-multiline";
    protected static final String Multipoint = "vector-multipoint";
    protected static final String Multipolygon = "vector-multipolygon";
    // 坐标pg方法
    public static final String Coo_Function = "ST_GeomFromText('%s',%s)";
    protected static final String Coo_Function_SQL = "public.st_geometryfromtext('%s',4326)";

    public static final String MINEGEOJSON = "minegeojson";

    public static String getDataType(String geometryType) {
        geometryType = geometryType.toLowerCase();
        if (geometryType.contains(Polygon)) {
            return Polygon;
        } else if (geometryType.contains(Line)) {
            return Line;
        } else if (geometryType.contains(Point)) {
            return Point;
        } else {
            return geometryType;
        }
    }
}

package com.surge.common.gis.util;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

public class WktUtils {

    private static WKTReader WKTREADER = new WKTReader();
    private static WKTWriter WKTWRITER = new WKTWriter();

    public static Geometry geometry(String wkt){

        if (wkt == null){
            return null;
        }

        try {
            Geometry geometry = new WKTReader().read(wkt);
            return geometry;
        } catch (ParseException e) {
            System.out.println("--------------" + wkt + "---------------------");
            e.printStackTrace();
            return null;
        }
    }
    public static String toWKT(Geometry geom){
        if (geom == null){
            return null;
        }
        return new WKTWriter().write(geom);
    }

    public static Geometry unifyGeom(Geometry geom){

        String eastlon = "POLYGON((0.0 -90.0 , 180.0 -90.0 , 180.0 90.0 , 0.0 90.0 , 0.0 -90.0))";
        String westlon = "POLYGON((180.0 -90.0 , 360.0 -90.0 , 360.0 90.0 , 180.0 90 , 180.0 -90.0))";

        if (check(geom)){
            return geom;
        }

        Geometry eastGeom = geometry(eastlon);
        Geometry westGeom = geometry(westlon);

        Geometry westInter = geom.intersection(westGeom);

        if (westInter != null){

            Geometry eastInter = geom.intersection(eastGeom);

            east2west(westInter);

            eastInter =  eastInter.union(westInter);

            return eastInter;
        }else{
            return geom;
        }
    }

    public static boolean check(Geometry geom){
        if (geom == null){
            return true;
        }
        Coordinate[] cs = geom.getCoordinates();
        for (Coordinate coor : cs){
            if (coor.x > 180.0){
                return false;
            }
        }
        return true;
    }

    public static void east2west(Geometry geo){
        if (geo == null){
            return;
        }
        Coordinate[] cs = geo.getCoordinates();
        for (Coordinate coor : cs){
            if (coor.x >= 180.0){
                coor.x = coor.x - 360.0;
            }
        }
    }
}

package com.surge.common.core.constant.enums;

public enum GisFileType {

    IS_SHP("shp", "data-shp", "SHP"),
    IS_MIF("mif", "data-mif", "MIF"),
    IS_GEO_JSON("geojson", "data-geojson", "GeoJSON"),
    IS_KML("kml", "data-kml", "KML"),
    IS_CSV("csv", "data-csv", "CSV"),
    IS_TIFF("tiff", "data-raster", "TIFF"),
    IS_TIF("tif", "data-raster", "TIF"),
    IS_3D_TILES("3dtiles", "data-3dtiles", "3D Tiles"),
    IS_GLTF("gltf", "data-gltf", "Gltf"),
    IS_OTHER("other", "data-other", ""),
    ;
    private String suffix;
    private String name;
    private String type;

    GisFileType(String suffix, String type, String name) {
        this.suffix = suffix;
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getSuffix() {
        return suffix;
    }

    public String getName() {
        return name;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }



    public static GisFileType getFileSuffixEnum(String suffix) {
        for (GisFileType fileSuffixEnum : GisFileType.values()) {
            if (fileSuffixEnum.getSuffix().equals(suffix)) {
                return fileSuffixEnum;
            }
        }
        return null;
    }
}

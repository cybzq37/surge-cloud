package com.surge.common.gis.enums;

public enum EpsgEnum {


        //    WG84_3857(3857, "webmercator"),
    WG84_4326(4326, "EPSG:4326 - WGS84"),
    //    CGCS2000_4547(4547, "webmercator"),
    CGCS2000_4490(4490, "EPSG:4490 - CGCS2000"),
    GCJ_02(4326, "GCJ_02"),
    ;

    private Integer code;
    private String label;

    EpsgEnum(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

    public Integer getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static EpsgEnum getByCode(Integer code) {
        for (EpsgEnum typeEnum : EpsgEnum.values()) {
            if (typeEnum.code.equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }

    public static EpsgEnum getByLabel(String label) {
        for (EpsgEnum typeEnum : EpsgEnum.values()) {
            if (typeEnum.label.equals(label)) {
                return typeEnum;
            }
        }
        return null;
    }
}

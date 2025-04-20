package com.surge.common.gis.ogr;

import lombok.Data;

@Data
public class VectorInfo {

    public double[] central;

    public double[] extent;

    public Integer epsg;
}

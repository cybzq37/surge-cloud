package com.surge.common.gis.ogr;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.gdal.gdal.gdal;
import org.gdal.ogr.*;
import org.gdal.osr.SpatialReference;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.*;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

@Slf4j
public class VectorUtils {

    static {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            log.info("win load gdal");
            System.loadLibrary("gdal"); // gdal.dll
        } else if (osName.contains("linux")) {
            log.info("linux load gdal");
         System.loadLibrary("gdal");    //libgdal.so
        }
        ogr.RegisterAll();
        gdal.AllRegister();//设置gdal环境
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","YES");//支持中文路径
        gdal.SetConfigOption("SHAPE_ENCODING","CP936");//属性表字段支持中文
    }

    public static void main(String[] args) {
//        File file = new File("E:\\work\\map_file\\矢量格式\\geojson\\气候灾害预报.geojson");
//        Map<String, FieldBO> schema = getSchema("E:\\work\\map_file\\矢量格式\\geojson\\气候灾害预报.geojson");
//        String javaEncode = FileEncodeUtil.getJavaEncode(file);
//        getVectorBound(new DataEntryVO(), "E:\\work\\feishu\\Raomen-4326.mif", null, false);
    }

//    public static void getFileLayer(String vectorPath, String csvPath, String code) {
//        ogr.RegisterAll();
//        // 为了支持中文路径，请添加下面这句代码
//        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
//        // 为了使属性表字段支持中文，请添加下面这句
//        gdal.SetConfigOption("GEOJSON_ENCODING", code);
//        DataSource open = ogr.Open(vectorPath);
//        if (open != null) {
//            writeVectorFileCsv(csvPath, open.GetLayer(0));
//            open.FlushCache();
//            open.delete();
//        }
//    }

//    public static Layer getPgLayer(DataSourceDataAccessBO dataAccessBO) {
//        // 注册所有的驱动
//        ogr.RegisterAll();
//        // 为了支持中文路径，请添加下面这句代码
//        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","YES");
//        // 为了使属性表字段支持中文，请添加下面这句
//        gdal.SetConfigOption("SHAPE_ENCODING", "CP936");
//        //打开驱动
//        Driver postgreSQLDriver = ogr.GetDriverByName("PostgreSQL");
//        if(Objects.isNull(postgreSQLDriver)){
//            throw new CustomException(ErrcodeEnum.CLIENT_ERROR, "不支持 PostgreSQL 驱动");
//        }
//        //GDAL连接PostGIS
//        StringBuffer pgConnetcBuff = new StringBuffer();
//        pgConnetcBuff.append("PG:dbname=").append(dataAccessBO.getDatabase())
//                .append(" host=").append(dataAccessBO.getHostIP())
//                .append(" port=").append(dataAccessBO.getPort())
//                .append(" user=").append(dataAccessBO.getUser())
//                .append(" password=").append(dataAccessBO.getPassword());
//        String path = pgConnetcBuff.toString();
//        DataSource pgDataSource = postgreSQLDriver.Open(path, 0);
//        //获取图层
//        Layer pgLayer = pgDataSource.GetLayerByName(dataAccessBO.getTableName());
//        if (Objects.isNull(pgLayer)) {
//            log.error("获取图层失败");
//            throw new CustomException(ErrcodeEnum.CLIENT_ERROR, "获取图层失败");
//        }
//        return pgLayer;
//    }

//    public static void writeVectorFileCsv(String csvPath, Layer layer) {
//        // 为了支持中文路径，请添加下面这句代码
//        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
//        // 为了使属性表字段支持中文，请添加下面这句
//        gdal.SetConfigOption("SHAPE_ENCODING", "CP936");
//        // 注册所有的驱动
////        ogr.RegisterAll();
//        String strDriverName = "CSV";
//        Driver oDriver = ogr.GetDriverByName(strDriverName);
//        if (oDriver == null) {
//            System.out.println("创建shp驱动失败");
//            throw new RuntimeException("创建shp驱动失败");
//        }
//        // 步骤1、创建数据源
//        DataSource dataSource = oDriver.CreateDataSource(csvPath);
//        if (dataSource == null) {
//            log.error("创建矢量文件失败！");
//            throw new RuntimeException("创建矢量文件失败");
//        }
//        //步骤2、设置空间坐标系，使用原始数据
//        SpatialReference dstRef = layer.GetSpatialRef();
//        //步骤3、创建图层，并添加坐标系，创建一个多边形图层(wkbGeometryType.wkbUnknown,存放任意几何特征)
//        Layer newLayer = dataSource.CreateLayer(layer.GetName(), dstRef);
//        if (newLayer == null) {
//            log.error("图层创建失败！");
//            throw new RuntimeException("图层创建失败");
//        }
//        createLayerField(layer, newLayer);
//        writeFeatureInfo(layer, newLayer);
//        dataSource.FlushCache();
//        dataSource.delete();
//    }
//
//    private static void createLayerField(Layer layer, Layer newLayer) {
//        //1.创建字段信息，从读取出来的图层中获取
//        FeatureDefn featureDefn = layer.GetLayerDefn();
//        int fieldCount = featureDefn.GetFieldCount();
//        for (int i = 0; i < fieldCount; i++) {
//            FieldDefn fieldDefn = featureDefn.GetFieldDefn(i);
//            FieldDefn oFieldID = new FieldDefn(fieldDefn.GetName(), fieldDefn.GetFieldType());
//            newLayer.CreateField(oFieldID);
//        }
//        FieldDefn oFieldID = new FieldDefn("geometry", 4);
//        newLayer.CreateField(oFieldID);
//
//    }
//
//    private static void writeFeatureInfo(Layer layer, Layer newLayer) {
//        //从读取出来的图层中读取数据
//        long featureCount = layer.GetFeatureCount();
//        for (int i1 = 0; i1 < featureCount; i1++) {
//            Feature feature = layer.GetNextFeature();
//            //空间几何信息
//            Geometry geometry = feature.GetGeometryRef();
//            //新的图层中创建要素
//            FeatureDefn featureDefn = newLayer.GetLayerDefn();
//            Feature newFeature = new Feature(featureDefn);
//            //设置几何信息，这里需要处理坐标转换
//            newFeature.SetField("geometry", geometry.ExportToWkt());
//            //设置属性值
//            int fieldCount = feature.GetFieldCount();
//            for (int i2 = 0; i2 < fieldCount; i2++) {
//                FieldDefn fieldDefn = feature.GetFieldDefnRef(i2);
//                String fieldName = fieldDefn.GetName();
//                String str = feature.GetFieldAsString(fieldName);
//                newFeature.SetField(fieldName, str);
//            }
//            newLayer.CreateFeature(newFeature);
//        }
//    }
//
//
//    @SneakyThrows
//    public static Map<String, FieldBO> getSchema(String inputFile) {
//        String javaEncode = FileEncodeUtil.getJavaEncode(new File(inputFile));
//        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
//        gdal.SetConfigOption("GEOJSON_ENCODING", javaEncode);
//        Map<String, FieldBO> fields = new HashMap<>();
//        ogr.RegisterAll();
//        DataSource dataset = ogr.Open(inputFile);
//        //默认获取第一个layer
//        Layer layer = dataset.GetLayer(0);
//        FeatureDefn featureDefn = layer.GetLayerDefn();
//        int fieldCount = featureDefn.GetFieldCount();
//        for (int i = 0; i < fieldCount; i++) {
//            FieldDefn fieldDefn = featureDefn.GetFieldDefn(i);
//            int fieldType = fieldDefn.GetFieldType();
//            String fieldTypeName = fieldDefn.GetFieldTypeName(fieldType);
//            FileColumn fileColumn = FileColumn.fileType(fieldTypeName);
//
//            FieldBO fieldBO = new FieldBO(fieldDefn.GetName().toLowerCase(), fileColumn.getTypeInFile(), fileColumn.getTypeInPg(), 0, null);
//            fields.put(fieldBO.getFieldName(), fieldBO);
//        }
//        FieldBO fieldBO = new FieldBO("pkid", FileColumn.Integer4.getTypeInFile(), FileColumn.Integer4.getTypeInPg(), 0, null);
//        fields.put("pkid", fieldBO);
//        dataset.FlushCache();
//        dataset.delete();
//        return fields;
//    }
//
//
    @SneakyThrows
    public static VectorInfo getCentral(String file) {
        VectorInfo vectorInfo = new VectorInfo();
        // 为了支持中文路径，请添加下面这句代码
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
        DataSource open = ogr.Open(file, 0);
        if (open != null) {
            Layer layer = open.GetLayer(0);
            double[] doubles = layer.GetExtent();
            if (doubles != null && doubles.length == 4) {
                Envelope envelope = toPolygon(doubles[0], doubles[2], doubles[1], doubles[3]).getEnvelope().getEnvelopeInternal();
                Coordinate centroid = envelope.centre();
                double[] central = new double[]{centroid.getX(), centroid.getY()};
                double[] extent = new double[]{envelope.getMinX(), envelope.getMinY(), envelope.getMaxX(), envelope.getMaxY()};
                vectorInfo.setCentral(central);
                vectorInfo.setExtent(extent);
            }
            // 如果不指定读取文件坐标系
            SpatialReference spatialReference = layer.GetSpatialRef();
            if (spatialReference == null) {
                throw new RuntimeException("数据缺失坐标系信息");
            }
            try {
                CoordinateReferenceSystem coordinateReferenceSystem = CRS.parseWKT(spatialReference.ExportToPrettyWkt());
                Integer epsgCode = CRS.lookupEpsgCode(coordinateReferenceSystem, true);
                vectorInfo.setEpsg(epsgCode);
            } catch (Exception e) {
                throw new RuntimeException("缺失EPSG-CODE，请指定后重新上传");
            }
            open.FlushCache();
            open.delete();
        }
        return vectorInfo;
    }

    public static Polygon toPolygon(double x, double y, double x1, double y1) {
        GeometryFactory factory = new GeometryFactory();
        Coordinate[] coordinates = new Coordinate[5];
        coordinates[0] = new Coordinate(x, y);
        coordinates[1] = new Coordinate(x1, y);
        coordinates[2] = new Coordinate(x1, y1);
        coordinates[3] = new Coordinate(x, y1);
        coordinates[4] = new Coordinate(x, y);
        LinearRing lr = factory.createLinearRing(coordinates);
        return new Polygon(lr, null, factory);
    }


}

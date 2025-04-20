package com.surge.common.gis.vector.shp;

import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.utils.PinyinUtils;
import com.surge.common.core.utils.file.FileEncodeUtils;
import com.surge.common.gis.model.DataField;
import com.surge.common.gis.util.WktUtils;
import com.vividsolutions.jts.geom.Geometry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.ResourceInfo;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureTypes;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.util.Classes;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class ShapeDataStore implements AutoCloseable {

    private ShapefileDataStore shapefileDataStore;
    private SimpleFeatureSource simpleFeatureSource;

    public ShapeDataStore(String fileName) throws Exception {
        File file = new File(fileName);
        shapefileDataStore = (ShapefileDataStore) (FileDataStoreFinder.getDataStore(file));
        String charSet = getCharSet(fileName);
        shapefileDataStore.setCharset(Charset.forName(charSet));
        simpleFeatureSource = shapefileDataStore.getFeatureSource();
    }

    public List<DataField> getSchema() {
        SimpleFeatureType simpleFeatureType = simpleFeatureSource.getSchema();
        List<DataField> fieldList = new ArrayList<>();
        for (int i = 0; i < simpleFeatureType.getAttributeCount(); i++) {
            AttributeType attributeType = simpleFeatureType.getType(i);
            AttributeDescriptor attributeDescriptor = simpleFeatureType.getDescriptor(i);
            DataField dataField = new DataField();
            // 属性名称
            String label = attributeDescriptor.getName().toString();
            dataField.setLabel(attributeDescriptor.getLocalName());
            // 属性标识
            String name = PinyinUtils.convertToPinyin(label);
            if(StringUtils.isBlank(name)) {
                throw new ServiceException("文件校验失败，属性标识为空");
            }
            dataField.setName(name);
            // 区分标识
            dataField.setIdentified(attributeType.isIdentified());
            // 是否必填
            dataField.setRequired(attributeDescriptor.isNillable());
            // 属性类型
            String type = Classes.getShortName(attributeType.getBinding());
            if (name.equalsIgnoreCase("the_geom") || name.equalsIgnoreCase("geometry")) {
                dataField.setName("geometry");
                dataField.setType("geometry");
            }else{
                dataField.setType(type);
            }
            // 属性长度
            int length = FeatureTypes.getFieldLength(simpleFeatureType.getDescriptor(i));
            dataField.setLength(length);
            // 添加到列表
            fieldList.add(dataField);
        }
        if (fieldList.stream().noneMatch(it-> it.getType().equalsIgnoreCase("geometry"))) {
            throw new ServiceException("文件校验失败, geometry字段不存在");
        }
        return fieldList;
    }

    public List<Map<String,Object>> getData(List<DataField> fieldList) {
        // schemaList 转为 Map<String,String>
        Map<String, String> schema = new HashMap<>();
        for (DataField dataField : fieldList) {
            schema.put(dataField.getName(), dataField.getType());
        }
        List<Map<String,Object>> result = new ArrayList<>();
        try(SimpleFeatureIterator featuresIterator = simpleFeatureSource.getFeatures().features()) {
            while (featuresIterator.hasNext()) {
                SimpleFeature feature = featuresIterator.next();
                result.add(getRow(schema, feature));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private String getCharSet(String fileName) {
        if (fileName == null || fileName.length() == 0) {
            return "GBK";
        }
        String cpgFile = fileName.replace(".shp", ".cpg");
        File file = new File(cpgFile);
        if (file.exists()) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                String charSet = bufferedReader.readLine();
                if (StringUtils.isEmpty(charSet.trim())) {
                    return "GBK";
                }
                return charSet.trim();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return "GBK";
        } else {
            String dbfFile = fileName.replace(".shp", ".dbf");
            try(InputStream inputStream = Files.newInputStream(Paths.get(dbfFile))) {
                return FileEncodeUtils.getJavaEncode(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private Map<String, Object> getRow(Map<String, String> schema, SimpleFeature feature) {
        Map<String, Object> row = new HashMap<>();
        for (Map.Entry<String, String> entry : schema.entrySet()) {
            String fieldName = entry.getKey();
            String fieldType = entry.getValue();
            Object val = feature.getAttribute(fieldName);
            if (fieldType.equalsIgnoreCase("double")) {
                val = val == null ? 0 : (double) val;
            } else if (fieldType.equalsIgnoreCase("float")) {
                val = val == null ? 0 : (float) val;
            } else if (fieldType.equalsIgnoreCase("integer")) {
                val = val == null ? 0 : (int) val;
            } else if (fieldType.equalsIgnoreCase("long")) {
                val = val == null ? 0 : (Long) val;
            } else if (fieldType.equalsIgnoreCase("date")) {
                val = val == null ? 0 : (Date) val;
            } else if (fieldType.equalsIgnoreCase("boolean")) {
                val = val == null ? false : (Boolean) val;
            } else if (fieldType.equalsIgnoreCase("decimal")) {
                val = val == null ? 0 : (BigDecimal) val;
            } else if (fieldType.equalsIgnoreCase("geometry")) {
                Geometry geometry = WktUtils.geometry(feature.getDefaultGeometry().toString());
                String wkt = WktUtils.toWKT(geometry);
                val = wkt;
            } else {
                val = val == null ? "" : val;
            }
            row.put(fieldName, val);
        }
        return row;
    }

    private List<Map<String, Object>> query(Query query) throws Exception {
        SimpleFeatureCollection collection = null;
        if (query == null) {
            collection = simpleFeatureSource.getFeatures();
        } else {
            collection = simpleFeatureSource.getFeatures(query);
        }
        SimpleFeatureIterator features = collection.features();
        ResourceInfo info = simpleFeatureSource.getInfo();
        CoordinateReferenceSystem crs = info.getCRS();
        String crsWkt = "GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]";
        CoordinateReferenceSystem crs84 = CRS.parseWKT(crsWkt);
        CoordinateReferenceSystem csr4326 = CRS.decode("EPSG:4326");
        System.out.println(csr4326.toWKT());
        MathTransform mathTransform = CRS.findMathTransform(crs, crs84);
        List<Map<String, Object>> datas = new ArrayList<>();
        while (features.hasNext()) {
            SimpleFeature feature = features.next();
            Collection<Property> properties = feature.getProperties();
            Map<String, Object> attr = new HashMap<>();
            Iterator<Property> it = properties.iterator();
            while (it.hasNext()) {
                Property property = it.next();
                String field = property.getName().toString();
                Object obj = property.getValue();
                if (field.equalsIgnoreCase("the_geom")) {
                    field = "geometry";
                    WKTReader wktReader = new WKTReader();
                    org.locationtech.jts.geom.Geometry geometry = wktReader.read(obj.toString());
                    org.locationtech.jts.geom.Geometry geom = JTS.transform(geometry, mathTransform);
                    String wkt = geom.toText();
                    System.out.println(wkt);
                }
                attr.put(field, obj);
            }
            datas.add(attr);
        }
        return datas;
    }

    @Override
    public void close() throws Exception {
        if (shapefileDataStore != null) {
            shapefileDataStore.dispose();
        }

    }
}

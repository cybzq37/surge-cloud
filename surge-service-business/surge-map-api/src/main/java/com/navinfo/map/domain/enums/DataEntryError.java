package com.surge.map.domain.enums;

public class DataEntryError {
    /**
     * 多次压缩
     */
    public final static String MULTIPLE_ZIP = "压缩包存在多次压缩情况，请检查修正后重新上传";
    /**
     * 嵌套文件夹
     */
    public final static String ZIP_DIR = "压缩包存在文件夹，请检查修正后重新上传";
    /**
     * 文件缺失
     */
    public final static String CHECK_SUFFIX = "上传%s文件组中缺失必要文件【%s】,请检查修正后重新上传";

    /**
     * 上传远程文件失败
     */
    public final static String UPLOAD_FILE = "文件上传至服务器失败，请联系管理员";
    /**
     * geojson、kml、csv数据只能包含点线面任一类型的数据，不能混合存储
     */
    public final static String DATA_TYPE = "空间数据类型不统一，请检查修正后重新上传";

    /**
     * 数据中必须包含geometry字段
     */
    public final static String GEOMETRY_IS_NULL = "上传数据中空间字段【geometry】不存在，请检查修正后重新上传";

    /**
     * 文件名重复
     */
    public final static String FILE_NAME_REPEAT = "上传文件中【%s】文件名重复，请检查修正后重新上传";

    /**
     * 文件解析乱码，请检查编码格式
     */
    public final static String GARBLED_CODE = "上传文件解析乱码，请检查修正后重新上传";

    /**
     * 字段不能包含中文
     */
    public final static String FILED_CN = "上传数据中字段【%s】存在中文，请检查修正后重新上传";
    /**
     * 字段数字不能开头
     */
    public final static String FILED_NUM = "上传数据中字段【%s】以数字开头，请检查修正后重新上传";
    /**
     * 字段不能包含特殊字符
     */
    public final static String FILED_SPECIAL = "上传数据中字段【%s】包含特殊字符，请检查修正后重新上传";
    /**
     * 字段不能是数据库关键字
     */
    public final static String FILED_KEYWORD = "上传数据中字段【%s】为数据库关键字，请检查修正后重新上传";
    /**
     * 字段不能有重复字段
     */
    public final static String FILED_REPEAT = "上传数据中字段【%s】为重复字段，请检查修正后重新上传";
    /**
     * 字段为空
     */
    public final static String FILED_NULL = "上传数据中【%d】个字段名存在为空，请检查修正后重新上传";

    /**
     * csv文件中必须存在地理信息字段且以WKT格式存储
     */
    public final static String CSV_GEOMETRY = "上传数据中地理信息字段内容不是wkt格式，暂不支持解析，请修正后重新上传";
    /**
     * csv文件中必须存在地理信息字段且以WKT格式存储
     */
    public final static String CSV_COLUMN = "csv每行长度不一致，与表头不对应，请修正后重新上传";
    /**
     * 空间信息为0条
     */
    public final static String DATA_IS_NULL = "上传的数据文件数据量为0，请确认数据无误后重新上传";
    /**
     * 未在指定坐标系范围
     */
    public final static String NOT_EPSG_WITHIN = "暂不支持该坐标系，请转换为WGS84经纬度或CGCS2000经纬度或GCJ_02后重新上传";
    /**
     * 未在指定epsgcode
     */
    public final static String NOT_EPSG_CODE = "缺失EPSG-CODE，请指定后重新上传";
    /**
     * 多种srid（多种坐标系数据）
     */
    public final static String MULTIPLE_SRID = "该数据存在多种srid，请处理为统一srid后重新接入";
    /**
     * 程序执行异常
     */
    public final static String PROGRAM_ERROR = "程序执行异常，请联系管理员";
    /**
     * 中间数据库连接失败
     */
    public final static String DATASOURCE_ERROR = "中间数据库连接失败";

    /**
     * 数据缺失坐标系信息
     */
    public final static String NO_PROJECTION = "数据缺失坐标系信息";



    /**
     * 更新字段名与原始字名段不一致
     */
    public final static String UPDATE_FILED_NAME_INCONSISTENT = "更新数据字段名与原始数据字段名不一致，请检查修正后重新上传";
    /**
     * 更新字段数量与原始字名段数量不一致
     */
    public final static String UPDATE_FILED_SIZE_INCONSISTENT = "更新数据字段数量小于原始数据字段数量，请检查修正后重新上传";
    /**
     * 更新字段信息与原始字段信息不一致
     */
    public final static String UPDATE_FILED_MAP_INCONSISTENT = "更新数据字段属性与原始数据字段属性不一致，请检查修正后重新上传";
    /**
     * 未在指定epsgcode
     */
    public final static String UPDATE_EPSG_CODE_UNEQUAL = "更新数据EPSG不一致，请检查修正后重新上传";
    /**
     * 更新数据同步失败
     */
    public final static String UPDATE_DATA_SYNC = "数据更新同步时失败，请联系管理员！";


    /**
     *
     * *****************************************************************************
     *
     * ************************ TODO 以下为业务流程提醒msg **************************
     *
     * *****************************************************************************
     *
     *
     *
     */

    /**
     * 当前数据已是最新版本
     */
    public final static String DATA_PUBLISH_VERSION_LATESTVERSION = "当前数据已是最新版本！";

    /**
     * 同版本切换
     */
    public final static String DATA_PUBLISH_VERSION_SWITCH = "相同版本不能切换！";

    /**
     * 同版本切换
     */
    public final static String DATA_PUBLISH_VERSION_IS_NULL = "当前数据版本已切换！";


}

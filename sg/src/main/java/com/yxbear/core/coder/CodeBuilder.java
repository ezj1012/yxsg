package com.yxbear.core.coder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.yxbear.core.CommUtils;
import com.yxbear.core.bean.Condition;
import com.yxbear.core.bean.EntityBean;
import com.yxbear.core.db.mybatis.BaseMapper;

public class CodeBuilder {

    final String productName;

    final String basePkg;

    final String baskModelPkg;

    final String baskMapperPkg;

    final File baseFile;

    final File baseModeFile;

    final File baseMapperFile;

    public CodeBuilder(String productName, String basePkg) {
        super();
        this.productName = productName;
        this.basePkg = basePkg;
        this.baseFile = findBaseFile(productName);
        this.baskModelPkg = basePkg + ".domain.model";
        this.baskMapperPkg = basePkg + ".domain.mapper";
        this.baseModeFile = new File(baseFile, "src/main/java/" + replaceD(getBaseModelPkg()));
        this.baseMapperFile = new File(baseFile, "src/main/java/" + replaceD(getBaseMapperPkg()));
    }

    File getBaseModeFile() {
        return baseModeFile;
    }

    File getBaseMapperFile() {
        return baseMapperFile;
    }

    String getBaseModelPkg() {
        return baskModelPkg;
    }

    String getBaseMapperPkg() {
        return baskMapperPkg;
    }

    public PackageBuilder pkg(String name) {
        return PackageBuilder.getInst(this, name);
    }

    public void buildAll() {
        PackageBuilder.keyMap.values().forEach(PackageBuilder::buildPackageCode);
    }

    public static class PackageBuilder {

        public static final Map<String, PackageBuilder> keyMap = new HashMap<>();

        public static PackageBuilder getInst(CodeBuilder code, String pkg) {
            pkg = pkg.toLowerCase();
            return keyMap.computeIfAbsent(pkg, pkg1 -> new PackageBuilder(code, pkg1));
        }

        List<ModelBuilder> models = new ArrayList<>();

        CodeBuilder code;

        String pkgName;

        public PackageBuilder(CodeBuilder code, String pkgName) {
            this.code = code;
            this.pkgName = pkgName;
        }

        /**
         * 新建一个model 默认主键为Long类型
         * 
         * @param modelName
         * @return
         */
        public ModelBuilder model(String modelName) {
            return modelLong(modelName);
        }

        private ModelBuilder model(String modelName, AttrType idType) {
            ModelBuilder modelBuilder = new ModelBuilder(this, modelName, idType);
            models.add(modelBuilder);
            return modelBuilder;
        }

        public ModelBuilder modelLong(String modelName) {
            return model(modelName, AttrType.LONG);
        }

        public ModelBuilder modelInt(String modelName) {
            return model(modelName, AttrType.INT);
        }

        public ModelBuilder modelLongNoAudit(String modelName) {
            return model(modelName, AttrType.LONG).noAudit();
        }

        public ModelBuilder modelIntNoAudit(String modelName) {
            return model(modelName, AttrType.INT).noAudit();
        }

        // intId = 0,
        // longId = 1,
        // intIdNoAudit = 2,
        // longIdNoAudit = 3,
        public ModelBuilder model(String modelName, int type) {
            switch (type) {
                case 0 :
                    return model(modelName, AttrType.INT);
                case 1 :
                    return model(modelName, AttrType.LONG);
                case 2 :
                    return model(modelName, AttrType.INT).noAudit();
                case 3 :
                    return model(modelName, AttrType.LONG).noAudit();
                default :
                    return model(modelName, AttrType.INT).noAudit();
            }
        }

        public CodeBuilder buildPackageCode() {
            models.forEach(m -> m.build());
            return code;
        }

    }

    public static class ModelBuilder {

        PackageBuilder pkgBuilder;

        String modelName;

        AttrBuilder idBuilder;

        boolean auditable = true;

        private List<AttrBuilder> attrs = new ArrayList<>();

        public ModelBuilder(PackageBuilder pkgBuilder, String modelName, AttrType idType) {
            this.pkgBuilder = pkgBuilder;
            if (!modelName.toLowerCase().startsWith(pkgBuilder.pkgName)) {
                modelName = UpFirstChar(pkgBuilder.pkgName) + UpFirstChar(modelName);
            }
            this.modelName = modelName;
            this.idBuilder = new AttrBuilder(this, "id", "唯一主键", idType);
            attrs.add(idBuilder);
        }

        public String getClassName() {
            return modelName;
        }

        public String getTableName() {
            return CommUtils.camelCaseToUnderscore(getClassName());
        }

        public PackageBuilder _____endCheck____() {
            return pkgBuilder;
        }

        public ModelBuilder noAudit() {
            auditable = false;
            return this;
        }

        public ModelBuilder attr(AttrBuilder attrBuilder) {
            attrs.add(attrBuilder);
            return this;
        }

        /**
         * 枚举类型,
         * 
         * @param name
         * @param desc
         * @return
         */
        public ModelBuilder attrInt2(String name, String desc) {
            AttrBuilder attrBuilder = new AttrBuilder(this, name, desc, AttrType.INT2);
            attrs.add(attrBuilder);
            return this;
        }

        public ModelBuilder attrInt4(String name, String desc) {
            AttrBuilder attrBuilder = new AttrBuilder(this, name, desc, AttrType.INT4);
            attrs.add(attrBuilder);
            return this;
        }

        public ModelBuilder attrInt(String name, String desc) {
            AttrBuilder attrBuilder = new AttrBuilder(this, name, desc, AttrType.INT);
            attrs.add(attrBuilder);
            return this;
        }

        public ModelBuilder attrLong(String name, String desc) {
            AttrBuilder attrBuilder = new AttrBuilder(this, name, desc, AttrType.LONG);
            attrs.add(attrBuilder);
            return this;
        }

        public ModelBuilder attrStr40(String name, String desc) {
            AttrBuilder attrBuilder = new AttrBuilder(this, name, desc, AttrType.STR40);
            attrs.add(attrBuilder);
            return this;
        }

        public ModelBuilder attrStr100(String name, String desc) {
            AttrBuilder attrBuilder = new AttrBuilder(this, name, desc, AttrType.STR100);
            attrs.add(attrBuilder);
            return this;
        }

        /***
         * 长度200
         * 
         * @param name
         * @param desc
         * @return
         */
        public ModelBuilder attrStr(String name, String desc) {
            return attrStr200(name, desc);
        }

        public ModelBuilder attrStr200(String name, String desc) {
            AttrBuilder attrBuilder = new AttrBuilder(this, name, desc, AttrType.STR200);
            attrs.add(attrBuilder);
            return this;
        }

        public ModelBuilder attrStr500(String name, String desc) {
            AttrBuilder attrBuilder = new AttrBuilder(this, name, desc, AttrType.STR500);
            attrs.add(attrBuilder);
            return this;
        }

        public ModelBuilder attrStr1000(String name, String desc) {
            AttrBuilder attrBuilder = new AttrBuilder(this, name, desc, AttrType.STR1000);
            attrs.add(attrBuilder);
            return this;
        }

        public ModelBuilder attrStr2000(String name, String desc) {
            AttrBuilder attrBuilder = new AttrBuilder(this, name, desc, AttrType.STR2000);
            attrs.add(attrBuilder);
            return this;
        }

        public ModelBuilder defVal(Object defVal) {
            if (!attrs.isEmpty()) {
                attrs.get(attrs.size() - 1).defaultValue(null == defVal ? "" : String.valueOf(defVal));
            }
            return this;
        }

        public ModelBuilder notNull() {
            if (!attrs.isEmpty()) {
                attrs.get(attrs.size() - 1).notNull = true;
            }
            return this;
        }

        public void build() {
            CodeWriter.writeAll(this);
        }

    }

    public static enum AttrType {

        /** 名称 */
        STR40(String.class, 40), //
        /** 长名称 */
        STR100(String.class, 100), //
        /** 短内容 */
        STR200(String.class, 200), //
        /** 长内容 */
        STR500(String.class, 500),
        /** 文本 */
        STR1000(String.class, 1000),
        /** 文本 */
        STR2000(String.class, 2000),
        /** 文档 */
        TEXT(String.class, 65535),
        /** 枚举,状态 */
        INT2(Integer.class),
        /** 枚举,状态 */
        INT4(Integer.class),
        /** */
        INT(Integer.class),
        /** */
        LONG(Long.class);

        private Class<?> javaType;

        private int length = 0;

        private AttrType(Class<?> javaType, int length) {
            this.javaType = javaType;
            this.length = length;
        }

        private AttrType(Class<?> javaType) {
            this.javaType = javaType;
        }

        public Class<?> getJavaType() {
            return javaType;
        }

        public int getLength() {
            return length;
        }

        public String getMySQLDbType() {
            if (this == INT) {
                return "INT";
            } else if (this == INT2) {
                return "TINYINT";
            } else if (this == INT4) {
                return "SMALLINT";
            } else if (this == LONG) {
                return "BIGINT";
            } else if (this == TEXT) {
                return "TEXT";
            } else {
                return "varchar(" + length + ")";
            }
        }

        public static AttrType of(String type) {
            type = type.toLowerCase().trim();
            if (Objects.equals(type, "str40") || Objects.equals(type, "s40")) {
                return AttrType.STR40;
            } else if (Objects.equals(type, "str100") || Objects.equals(type, "s100")) {
                return AttrType.STR100;
            } else if (Objects.equals(type, "str200") || Objects.equals(type, "s200") || Objects.equals(type, "str") || Objects.equals(type, "string")) {
                return AttrType.STR200;
            } else if (Objects.equals(type, "str500") || Objects.equals(type, "s500")) {
                return AttrType.STR500;
            } else if (Objects.equals(type, "str1000") || Objects.equals(type, "s1000")) {
                return AttrType.STR1000;
            } else if (Objects.equals(type, "str2000") || Objects.equals(type, "s2000")) {
                return AttrType.STR2000;
            } else if (Objects.equals(type, "text")) {
                return AttrType.TEXT;
            } else if (Objects.equals(type, "int2") || Objects.equals(type, "tiny")) {
                return AttrType.INT2;
            } else if (Objects.equals(type, "int4") || Objects.equals(type, "small")) {
                return AttrType.INT4;
            } else if (Objects.equals(type, "int") || Objects.equals(type, "number")) {
                return AttrType.INT;
            } else if (Objects.equals(type, "long") || Objects.equals(type, "big")) {
                return AttrType.LONG;//
            }
            return AttrType.STR200;
        }

    }

    public static class AttrBuilder {

        ModelBuilder mb;

        String name;

        String desc;

        String defaultValue = "";

        boolean notNull = false;

        AttrType type;

        public AttrBuilder(ModelBuilder mb, String name, String desc, AttrType type) {
            super();
            this.mb = mb;
            this.name = formatAttrName(name);
            if (DB_KEY.contains(name.toUpperCase())) {
                // System.err.println(mb.getClassName() + " " + name + " 是关键字!");
                throw new RuntimeException(mb.getClassName() + " " + name + " 是关键字!");
            }
            this.desc = desc;
            this.type = type;
        }

        public ModelBuilder endAttr() {
            return mb;
        }

        public ModelBuilder defaultValue(String defVal) {
            this.defaultValue = "DEFAULT " + defVal;
            return mb;
        }

        public ModelBuilder notNull() {
            this.notNull = true;
            return mb;
        }

        public ModelBuilder notNull(boolean notNull) {
            this.notNull = notNull;
            return mb;
        }

        /**
         * 
         * @param mb
         * @param attr
         *            name!:string ="1212" //asdasd
         * @return
         */
        public static AttrBuilder parse(ModelBuilder mb, String attr) {
            int t = attr.indexOf("//");
            String desc = "";
            if (t >= 0) {
                desc = attr.substring(t + 2, attr.length());
                attr = attr.substring(0, t).trim();
            }
            // 默认值
            t = attr.indexOf("=");
            String defaultValue = null;
            if (t >= 0) {
                defaultValue = attr.substring(t + 1, attr.length());
                if (defaultValue.startsWith("\"")) {
                    defaultValue = defaultValue.substring(1);
                }
                if (defaultValue.endsWith("\"")) {
                    defaultValue = defaultValue.substring(0, defaultValue.length() - 1);
                }

                attr = attr.substring(0, t).trim();
            }

            t = attr.indexOf(":");
            AttrType type = AttrType.STR200;
            if (t >= 0) {
                String ts = attr.substring(t + 1, attr.length());
                type = AttrType.of(ts);
                attr = attr.substring(0, t).trim();
            }
            t = attr.indexOf("!");
            boolean notNull = false;
            if (t >= 0) {
                notNull = true;
                attr = attr.substring(0, t).trim();
            }
            attr = attr.replace(">", "");

            String name = attr;
            AttrBuilder attrBuilder = new AttrBuilder(mb, name, desc, type);
            attrBuilder.defaultValue(defaultValue);
            attrBuilder.notNull(notNull);

            return attrBuilder;
        }

    }

    public static void main(String[] args) {
        AttrBuilder.parse(null, "name!:string =\"1212\" //asdasd");
    }

    public static class CodeWriter {

        public static void writeAll(ModelBuilder build) {
            writeModel(build);
            writeModelCdt(build);
            writeMapper(build);
            buildSql(build);
        }

        static final String MODEL_FORMAT = "package %s;\n\n" + "import %s;\n\n" //
                + "import lombok.Data;\n\n@Data\n" //
                + "public class %s implements EntityBean<%s> {\n\n%s%s\n\n}" //
        ;

        static final String MODEL_AU_FORMAT = "    /** 创建时间 */\\n    private Long createTime;\\n\\n    /** 最后一次修改时间 */\\n    private Long modifyTime;";

        public static void writeModel(ModelBuilder build) {
            String pkg = build.pkgBuilder.code.getBaseModelPkg() + "." + build.pkgBuilder.pkgName;
            StringBuilder attrBuf = new StringBuilder();
            build.attrs.forEach(a -> {
                attrBuf.append(buildModelAttr(a));
            });

            String content = String.format(MODEL_FORMAT,
                    //
                    pkg, EntityBean.class.getName(),
                    //
                    build.getClassName(), build.idBuilder.type.getJavaType().getSimpleName(),
                    //
                    attrBuf.toString(), build.auditable ? MODEL_AU_FORMAT : "");
            //
            File file = new File(build.pkgBuilder.code.getBaseModeFile(), build.pkgBuilder.pkgName);
            file.mkdirs();
            file = new File(file, build.getClassName() + ".java");
            writeFile(file, content);
        }

        static final String CDT_FORMAT =
                ////
                "package %s;\n"//
                        + "\n"//
                        + "import " + Condition.class.getName() + ";\n\n"//
                        //
                        + "import lombok.AllArgsConstructor;\n"
                        //
                        + "import lombok.Builder;\n"
                        //
                        + "import lombok.Data;\n"
                        //
                        + "import lombok.NoArgsConstructor;\n\n"
                        //
                        + "@Data\n" //
                        + "@Builder\n" //
                        + "@AllArgsConstructor\n" //
                        + "@NoArgsConstructor\n" //
                        + "public class C%s implements Condition {\n" //
                        + "\n    private static final long serialVersionUID = 1L;\n\n" //
                        + "%s" //
                        + "%s" //
                        + "}"; //

        static final String CDT_AU_FORMAT = "    private Long createTime;\n\n" //
                + "    private Long[] createTimes;\n\n" //
                + "    private Long startCreateTime;\n\n" //
                + "    private Long endCreateTime;\n\n" //
                + "    private Long modifyTime;\n\n" //
                + "    private Long[] modifyTimes;\n\n" //
                + "    private Long startModifyTime;\n\n" //
                + "    private Long endModifyTime;\n\n" //
        ;

        // static final AttrBuilder idAttr = new AttrBuilder(null, "createTime","主键" ,
        // );

        public static void writeModelCdt(ModelBuilder build) {
            String pkg = build.pkgBuilder.code.getBaseModelPkg() + "." + build.pkgBuilder.pkgName;
            StringBuilder attrBuf = new StringBuilder();

            build.attrs.forEach(a -> {
                attrBuf.append(buildModelCdtAttr(a));
            });

            String content = String.format(CDT_FORMAT,
                    //
                    pkg,
                    //
                    build.getClassName(),
                    //
                    attrBuf.toString(), build.auditable ? CDT_AU_FORMAT : "");
            //
            File file = new File(build.pkgBuilder.code.getBaseModeFile(), build.pkgBuilder.pkgName);
            file.mkdirs();
            file = new File(file, "C" + build.getClassName() + ".java");
            writeFile(file, content);
        }

        static final String MAPPER_FORMAT = "package %s;\n" // 001
                + "\n" //
                + "import org.apache.ibatis.annotations.Mapper;\n" //
                + "\n" //
                + "import %s.C%s;\n" // 002
                + "import %s.%s;\n" // 003
                + "import " + BaseMapper.class.getName() + ";\n" //
                + "\n" //
                + "@Mapper\n" //
                + "public interface %sMapper extends BaseMapper<%s, %s, C%s> {\n" // 004
                + "\n" //
                + "}"; //

        public static void writeMapper(ModelBuilder build) {
            String modelPkg = build.pkgBuilder.code.getBaseModelPkg() + "." + build.pkgBuilder.pkgName;
            String mapperPkg = build.pkgBuilder.code.getBaseMapperPkg() + "." + build.pkgBuilder.pkgName;
            String className = build.getClassName();
            String idClassName = build.idBuilder.type.getJavaType().getSimpleName();

            String content = String.format(MAPPER_FORMAT,
                    // 001
                    mapperPkg,
                    // 002
                    modelPkg, className,
                    // 003
                    modelPkg, className,
                    // 004
                    className, idClassName, className, className);
            //
            File file = new File(build.pkgBuilder.code.getBaseMapperFile(), build.pkgBuilder.pkgName);
            file.mkdirs();
            file = new File(file, build.getClassName() + "Mapper.java");
            writeFile(file, content);
        }

        static final String MODEL_ATTR_FORMAT = "    /** %s */\n    private %s %s;\n\n";

        public static String buildModelAttr(AttrBuilder attr) {
            return String.format(MODEL_ATTR_FORMAT, attr.desc, attr.type.getJavaType().getSimpleName(), attr.name);
        }

        public static StringBuilder buildModelCdtAttr(AttrBuilder attr) {
            // return String.format(MODEL_ATTR_FORMAT, attr.desc, attr.type.getSimpleName(),
            // attr.name);
            StringBuilder classBodyBuf = new StringBuilder();
            String type = attr.type.getJavaType().getSimpleName();
            classBodyBuf.append("    private ").append(type).append(" ").append(attr.name).append(";\n\n");

            if ("String".equals(type)) {
                classBodyBuf.append("    private ").append(type).append("[] ").append(attr.name).append("s;\n\n");
                classBodyBuf.append("    private ").append(type).append(" ").append(attr.name).append("Equal").append(";\n\n");
            } else if ("Integer".equals(type) || "Long".equals(type) || "Short".equals(type)) {
                String fName = UpFirstChar(attr.name);
                classBodyBuf.append("    private ").append(type).append("[] ").append(attr.name).append("s;\n\n");
                classBodyBuf.append("    private ").append(type).append(" ").append("start").append(fName).append(";\n\n");
                classBodyBuf.append("    private ").append(type).append(" ").append("end").append(fName).append(";\n\n");
            } else if ("Boolean".equalsIgnoreCase(type)) {

            }
            return classBodyBuf;
        }

        static final String SQL_CRT = "CREATE TABLE IF NOT EXISTS `%s` (\n"//
                + "  `ID` %s NOT NULL,\n%s"//
                + "%s"// AU
                + "  PRIMARY KEY (`id`)\n" //
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";//

        static final String SQL_AU_CRT = "  `CREATE_TIME` bigint ,\n"//
                + "  `MODIFY_TIME` bigint ,\n"//
        ;

        static final String SQL_COL = "  `%s` %s %s %s COMMENT '%s',\n";

        private static String useSQLCreate(String tableName, String idType, String otherColumns, boolean au) {
            return String.format(SQL_CRT, tableName, idType, otherColumns, au ? SQL_AU_CRT : "");
        }

        public static void buildSql(ModelBuilder build) {
            StringBuilder otherColumns = new StringBuilder();
            build.attrs.forEach(ab -> {
                if ("id".equalsIgnoreCase(ab.name)) { return; }
                String name = CommUtils.camelCaseToUnderscore(ab.name);
                otherColumns.append(String.format(SQL_COL, name, ab.type.getMySQLDbType(), ab.defaultValue, ab.notNull ? "NOT NULL" : " ", ab.desc == null ? "" : ab.desc));
                // String format = "ALTER TABLE sanguo.%s MODIFY COLUMN %s %s;";
                // System.out.println(String.format(format, build.getTableName(), name,
                // ab.type.getMySQLDbType(), ab.desc));
            });

            String sql = useSQLCreate(build.getTableName(), build.idBuilder.type.getMySQLDbType(), otherColumns.toString(), build.auditable);
            System.out.println("\n\n");
            // System.out.println("DROP TABLE " + build.getTableName() + ";\n\n");
            System.out.println(sql);
        }

    }

    private static final Set<String> DB_KEY = new HashSet<>(Arrays.asList("ADD", "TYPE", "ALL", "ALTER", "ANALYZE", "AND", "AS", "ASC", "ASENSITIVE", "BEFORE", "BETWEEN", "BIGINT", "BINARY", "BLOB", "BOTH", "BY", "CALL", "CASCADE", "CASE", "CHANGE", "CHAR", "CHARACTER", "CHECK",
            "GROUP", "RANK", "COLLATE", "COLUMN", "CONDITION", "CONSTRAINT", "CONTINUE", "CONVERT", "CREATE", "CROSS", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "DATABASE", "DATABASES", "DAY_HOUR", "DAY_MICROSECOND", "DAY_MINUTE",
            "DAY_SECOND", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DELAYED", "DELETE", "DESC", "DESCRIBE", "DETERMINISTIC", "DISTINCT", "DISTINCTROW", "DIV", "DOUBLE", "DROP", "DUAL", "EACH", "ELSE", "ELSEIF", "ENCLOSED", "ESCAPED", "EXISTS", "EXIT", "EXPLAIN", "FALSE", "FETCH",
            "FLOAT", "FLOAT4", "FLOAT8", "FOR", "FORCE", "FOREIGN", "FROM", "FULLTEXT", "GENERATED", "GET", "GRANT", "GROUP", "HAVING", "HIGH_PRIORITY", "HOUR_MICROSECOND", "HOUR_MINUTE", "HOUR_SECOND", "IF", "IGNORE", "IN", "INDEX", "INFILE", "INNER", "INOUT", "INSENSITIVE",
            "INSERT", "INT", "INT1", "INT2", "INT3", "INT4", "INT8", "INTEGER", "INTERVAL", "INTO", "IS", "ITERATE", "JOIN", "KEY", "KEYS", "KILL", "LABEL", "LEADING", "LEAVE", "LEFT", "LIKE", "LIMIT", "LINEAR", "LINES", "LOAD", "LOCALTIME", "LOCALTIMESTAMP", "LOCK", "LONG",
            "LONGBLOB", "LONGTEXT", "LOOP", "LOW_PRIORITY", "MATCH", "MEDIUMBLOB", "MEDIUMINT", "MEDIUMTEXT", "MIDDLEINT", "MINUTE_MICROSECOND", "MINUTE_SECOND", "MOD", "MODIFIES", "NATURAL", "NOT", "NO_WRITE_TO_BINLOG", "NULL", "NUMERIC", "ON", "OPTIMIZE", "OPTION",
            "OPTIONALLY", "OR", "ORDER", "OUT", "OUTER", "OUTFILE", "PARTITION", "PRECISION", "PRIMARY", "PROCEDURE", "PURGE", "RANGE", "READ", "READS", "READ_WRITE", "REAL", "REFERENCES", "REGEXP", "RELEASE", "RENAME", "REPEAT", "REPLACE", "REQUIRE", "RESIGNAL", "RESTRICT",
            "RETURN", "REVOKE", "RIGHT", "RLIKE", "SCHEMA", "SCHEMAS", "SECOND_MICROSECOND", "SELECT", "SENSITIVE", "SEPARATOR", "SET", "SHOW", "SIGNAL", "SLOW", "SMALLINT", "SPATIAL", "SPECIFIC", "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "SQL_BIG_RESULT",
            "SQL_CALC_FOUND_ROWS", "SQL_SMALL_RESULT", "SSL", "STARTING", "STORED", "STRAIGHT_JOIN", "TABLE", "TERMINATED", "THEN", "TINYBLOB", "TINYINT", "TINYTEXT", "TO", "TRAILING", "TRIGGER", "TRUE", "UNDO", "UNION", "UNIQUE", "UNLOCK", "UNSIGNED", "UPDATE", "USAGE", "USE",
            "USING", "VALUE", "UTC_DATE", "UTC_TIME", "UTC_TIMESTAMP", "VALUES", "VARBINARY", "VARCHAR", "VARCHARACTER", "VARYING", "VIRTUAL", "WHEN", "WHERE", "WHILE", "WINDOW", "WITH", "WRITE", "XOR", "YEAR_MONTH", "ZEROFILL"));

    private static String replaceD(String r) {
        return r.replace(".", "/");
    }

    private static String formatAttrName(String name) {
        if (!name.contains("_")) { return name; }
        StringBuilder sb = new StringBuilder();
        boolean u = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '_') {
                u = true;
            } else if (u) {
                sb.append(Character.toUpperCase(c));
                u = false;
            } else {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }

    private static String UpFirstChar(String name) {
        char[] charArray = name.toCharArray();
        charArray[0] = Character.toUpperCase(charArray[0]);
        return new String(charArray);
    }

    private static void writeFile(File file, String content) {
        file.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File findBaseFile(String productName) {
        try {
            File baseDir = new File("../").getCanonicalFile();
            boolean flag = true;
            while (flag) {
                flag = false;
                File[] listFiles = baseDir.listFiles(f -> f.isDirectory());
                for (File file : listFiles) {
                    if (file.getName().equals(productName)) {
                        baseDir = file.getCanonicalFile();
                        flag = true;
                        break;
                    }
                }
            }
            return baseDir;
        } catch (IOException e) {
            throw new RuntimeException("查找根目录错误!");
        }
    }

}

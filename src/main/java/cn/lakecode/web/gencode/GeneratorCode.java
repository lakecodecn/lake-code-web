package cn.lakecode.web.gencode;

import cn.lakecode.web.gencode.data.MysqlData;
import cn.lakecode.web.gencode.model.ClassInfo;
import cn.lakecode.web.gencode.model.TableField;
import cn.lakecode.web.gencode.model.TableInfo;
import cn.lakecode.web.gencode.model.TemplateField;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;


@Slf4j
public class GeneratorCode {

    private Map<String, TableInfo> tableInfo;

    private String[] table;

    private String author;

    private String outputDir;

    private String packageName;

    private final Configuration configuration;

    {
        try {
            configuration = new Configuration(Configuration.getVersion());
            configuration.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/ftl"));
            configuration.setDefaultEncoding("UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("init freemarker configuration fail");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static void main(String[] args) throws Exception {
        GeneratorCode generatorCode = new Builder()
                .url("jdbc:mysql://xxx.xx.xx.xx:xx/lk_ship?serverTimezone=GMT%2B8&useSSL=true")
                .username("root")
                .password("R")
                .table("wk_store")
                .packageName("cn.lakecode.demo")
                .outputDir("D:/demo")
                .author("lake")
                .build();
        generatorCode.autoGenerate();
    }

    public void autoGenerate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dtf.format(LocalDateTime.now());
        File root = dir(outputDir);
        for (String t : table) {
            TableInfo info = tableInfo.get(t);
            ClassInfo classInfo = classInfo(info.getName());
            entity(info, classInfo, date, root);
            service(info, classInfo, date, root);
            controller(info, classInfo, date, root);
            serviceImpl(info, classInfo, date, root);
            mapper(info, classInfo, date, root);
            mapperXml(info, classInfo, date, root);
            front(classInfo, root);
        }
    }

    private File srcJavaFile(File root) {
        File parent = new File(root, "src" + File.separator + "main" + File.separator + "java");
        if (!parent.exists()) {
            if (!parent.mkdirs()) {
                throw new RuntimeException("mkdirs fail");
            }
        }
        String f = packageName.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
        File file = new File(parent, f);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new RuntimeException("mkdirs fail");
            }
        }
        return file;
    }

    private File srcResourcesFile(File root) {
        File parent = new File(root, "src" + File.separator + "main" + File.separator + "resources");
        if (!parent.exists()) {
            if (!parent.mkdirs()) {
                throw new RuntimeException("mkdirs fail");
            }
        }
        return parent;
    }

    private void front(ClassInfo classInfo, File parent) {
        File file = srcResourcesFile(parent);
        File front = dir(file, "front");
        Map<String, Object> model = getJsModel(classInfo);
        file(front, classInfo.getModuleName() + ".js", "api.js.ftl", model);
        file(front, classInfo.getModuleName() + ".vue", "view.vue.ftl", model);
    }

    private void entity(TableInfo info, ClassInfo classInfo, String date, File parent) {
        File file = srcJavaFile(parent);
        File entity = dir(file, "model" + File.separator + "entity");
        Map<String, Object> model = getEntityModel(info, classInfo, date);
        fileJava(entity, classInfo.getEntityClassName() + ".java", "entity.java.ftl", model);
        File req = dir(file, "model" + File.separator + "req");
        model = getReqModel(info, classInfo, date);
        fileJava(req, classInfo.getReqClassName() + ".java", "req.java.ftl", model);
        fileJava(req, classInfo.getReqPageClassName() + ".java", "pagereq.java.ftl", model);
    }

    private void controller(TableInfo info, ClassInfo classInfo, String date, File parent) {
        File file = srcJavaFile(parent);
        File controller = dir(file, "controller");
        Map<String, Object> model = getControllerModel(info, classInfo, date);
        fileJava(controller, classInfo.getControllerClassName() + ".java", "controller.java.ftl", model);
    }

    private void service(TableInfo info, ClassInfo classInfo, String date, File parent) {
        File file = srcJavaFile(parent);
        File service = dir(file, "service");
        Map<String, Object> model = getServiceModel(info, classInfo, date);
        fileJava(service, classInfo.getServiceClassName() + ".java", "service.java.ftl", model);
    }

    private void serviceImpl(TableInfo info, ClassInfo classInfo, String date, File parent) {
        File file = srcJavaFile(parent);
        File serviceImpl = dir(file, "service" + File.separator + "impl");
        Map<String, Object> model = getServiceImplModel(info, classInfo, date);
        fileJava(serviceImpl, classInfo.getServiceImplClassName() + ".java", "serviceimpl.java.ftl", model);
    }

    private void mapper(TableInfo info, ClassInfo classInfo, String date, File parent) {
        File file = srcJavaFile(parent);
        File mapper = dir(file, "mapper");
        Map<String, Object> model = getMapperModel(info, classInfo, date);
        fileJava(mapper, classInfo.getMapperClassName() + ".java", "mapper.java.ftl", model);
    }

    private void mapperXml(TableInfo info, ClassInfo classInfo, String date, File parent) {
        File file = srcResourcesFile(parent);
        File mapper = dir(file, "mapper");
        Map<String, Object> model = getMapperXmlModel(info, classInfo, date);
        file(mapper, classInfo.getMapperClassName() + ".xml", "mapper.xml.ftl", model);
    }

    private File dir(File root, String path) {
        File file = new File(root, path);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return file;
            }
        }
        return file;
    }

    private File dir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return file;
            }
        }
        return file;
    }

    private void fileJava(File dir, String fileName, String ftl, Map<String, Object> model) {
        model.put("className", fileName.substring(0, fileName.lastIndexOf(".java")));
        file(dir, fileName, ftl, model);
    }

    private void file(File dir, String fileName, String ftl, Map<String, Object> model) {
        model.put("author", author);
        File file = new File(dir, fileName);
        if (file.exists()) {
            return;
        }
        try (Writer out = new FileWriter(file)) {
            Template template = configuration.getTemplate(ftl);
            template.process(model, out);
        } catch (Exception e) {
            log.warn("generate file={} fail", file);
        }
    }

    public Map<String, Object> getControllerModel(TableInfo info, ClassInfo classInfo, String date) {
        Map<String, Object> map = getModel(info, date);
        map.put("packageName", classInfo.getControllerPackage());
        map.put("serviceClassName", classInfo.getServiceClassName());
        map.put("serviceVarName", classInfo.getServiceVarName());
        map.put("reqClassName", classInfo.getReqClassName());
        map.put("reqPageClassName", classInfo.getReqPageClassName());
        map.put("module", classInfo.getModuleName());
        map.put("entityClassName", classInfo.getEntityClassName());
        List<String> imports = new ArrayList<>();
        imports.add(classInfo.getReqPackage() + "." + classInfo.getReqClassName());
        imports.add(classInfo.getReqPackage() + "." + classInfo.getReqPageClassName());
        imports.add(classInfo.getServicePackage() + "." + classInfo.getServiceClassName());
        imports.add(classInfo.getEntityPackage() + "." + classInfo.getEntityClassName());
        map.put("importPackages", imports);
        return map;
    }

    public Map<String, Object> getMapperXmlModel(TableInfo info, ClassInfo classInfo, String date) {
        Map<String, Object> map = getModel(info, date);
        map.put("mapperClass", classInfo.getMapperPackage() + "." + classInfo.getMapperClassName());
        return map;
    }

    public Map<String, Object> getMapperModel(TableInfo info, ClassInfo classInfo, String date) {
        Map<String, Object> map = getModel(info, date);
        map.put("packageName", classInfo.getMapperPackage());
        map.put("entityClassName", classInfo.getEntityClassName());
        List<String> imports = new ArrayList<>();
        imports.add(classInfo.getEntityPackage() + "." + classInfo.getEntityClassName());
        map.put("importPackages", imports);
        return map;
    }

    public Map<String, Object> getServiceImplModel(TableInfo info, ClassInfo classInfo, String date) {
        Map<String, Object> map = getModel(info, date);
        map.put("packageName", classInfo.getServiceImplPackage());
        map.put("entityClassName", classInfo.getEntityClassName());
        map.put("mapperClassName", classInfo.getMapperClassName());
        map.put("interfaceClassName", classInfo.getServiceClassName());
        map.put("reqClassName", classInfo.getReqClassName());
        map.put("reqPageClassName", classInfo.getReqPageClassName());
        List<String> imports = new ArrayList<>();
        imports.add(classInfo.getEntityPackage() + "." + classInfo.getEntityClassName());
        imports.add(classInfo.getMapperPackage() + "." + classInfo.getMapperClassName());
        imports.add(classInfo.getServicePackage() + "." + classInfo.getServiceClassName());
        imports.add(classInfo.getReqPackage() + "." + classInfo.getReqClassName());
        imports.add(classInfo.getReqPackage() + "." + classInfo.getReqPageClassName());
        map.put("importPackages", imports);
        return map;
    }

    public Map<String, Object> getServiceModel(TableInfo info, ClassInfo classInfo, String date) {
        Map<String, Object> map = getModel(info, date);
        map.put("packageName", classInfo.getServicePackage());
        map.put("entityClassName", classInfo.getEntityClassName());
        map.put("reqClassName", classInfo.getReqClassName());
        map.put("reqPageClassName", classInfo.getReqPageClassName());
        List<String> imports = new ArrayList<>();
        imports.add(classInfo.getEntityPackage() + "." + classInfo.getEntityClassName());
        imports.add(classInfo.getReqPackage() + "." + classInfo.getReqClassName());
        imports.add(classInfo.getReqPackage() + "." + classInfo.getReqPageClassName());
        map.put("importPackages", imports);
        return map;
    }

    public Map<String, Object> getModel(TableInfo info, String date) {
        Map<String, Object> map = new HashMap<>();
        map.put("author", author);
        map.put("date", date);
        map.put("comment", info.getComment());
        return map;
    }

    private Map<String, Object> getJsModel(ClassInfo classInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("moduleName", classInfo.getModuleName());
        return map;
    }

    private ClassInfo classInfo(String name) {
        return new ClassInfo(name, packageName, this::className);
    }


    public Map<String, Object> getReqModel(TableInfo info, ClassInfo classInfo, String date) {
        Map<String, Object> map = getModel(info, date);
        map.put("packageName", classInfo.getReqPackage());
        return map;
    }

    public Map<String, Object> getEntityModel(TableInfo info, ClassInfo classInfo, String date) {
        Map<String, Object> map = getModel(info, date);
        map.put("packageName", classInfo.getEntityPackage());
        map.put("table", info.getName());
        map.put("hasPk", false);
        List<TemplateField> templateFields = new ArrayList<>();
        List<String> importPackages = new ArrayList<>();
        List<TableField> fields = info.getFields();
        for (TableField field : fields) {
            TemplateField templateField = new TemplateField();
            templateField.setName(field.getName());
            templateField.setComment(field.getComment());
            templateField.setPk("YES".equalsIgnoreCase(field.getPrimaryKey()));
            templateField.setDisplayName(humpName(field.getName()));
            String className = SqlTypeConvert.convert(field.getType());
            templateField.setClassType(classType(className));
            if (!isLangPackage(className) && !importPackages.contains(className)) {
                importPackages.add(className);
            }
            if (templateField.getPk()) {
                map.put("hasPk", true);
            }
            templateFields.add(templateField);
        }
        map.put("fields", templateFields);
        map.put("importPackages", importPackages);
        return map;
    }

    public String classType(String className) {
        return className.substring(className.lastIndexOf(".") + 1);
    }

    public boolean isLangPackage(String packageName) {
        return packageName.startsWith("java.lang");
    }

    public String humpName(String source) {
        while (source.contains("_")) {
            int index = source.indexOf("_");
            String suffixSource = source.substring(index + 1);
            suffixSource = Character.toUpperCase(suffixSource.charAt(0)) + suffixSource.substring(1);
            source = source.substring(0, index) + suffixSource;
        }
        return source;
    }

    public String className(String name) {
        name = humpName(name);
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    public String javaFileName(String name) {
        name = humpName(name);
        return Character.toUpperCase(name.charAt(0)) + name.substring(1) + ".java";
    }

    public static class Builder {
        private String url;
        private String username;
        private String password;
        private String[] table;
        private String author;
        private String outputDir;
        private String packageName;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder table(String... table) {
            this.table = table;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder outputDir(String outputDir) {
            this.outputDir = outputDir;
            return this;
        }

        public Builder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public GeneratorCode build() {
            GeneratorCode generateCode = new GeneratorCode();
            generateCode.table = table;
            generateCode.author = author;
            generateCode.outputDir = outputDir;
            generateCode.packageName = packageName;
            MysqlData mysqlData = new MysqlData(url, username, password);
            generateCode.tableInfo = mysqlData.tableInfo();
            return generateCode;
        }
    }
}

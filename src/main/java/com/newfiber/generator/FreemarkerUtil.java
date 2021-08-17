package com.newfiber.generator;

import com.newfiber.generator.common.BasisInfo;
import com.newfiber.generator.common.DictInfo;
import com.newfiber.generator.common.JsonResult;
import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author : qianLei
 * @since : 2019-02-16 14:17
 */
public class FreemarkerUtil {

    public static JsonResult createFile(Object dataModel, String templateName, String filePath) {
        JsonResult result = new JsonResult();
        FileWriter out = null;

        String fileName;
        if ("enum.ftl".equals(templateName)) {
            fileName = "E" + ((DictInfo) dataModel).getEntityName()
                    + ((DictInfo) dataModel).getEnumName()
                    + messageStr(templateName);
        } else {
            fileName = ((BasisInfo) dataModel).getEntityName() + messageStr(templateName);
        }

        try {
            // 通过FreeMarker的Configuration读取相应的模板文件
            Configuration configuration = new Configuration();
            // 设置模板路径
            configuration.setClassForTemplateLoading(FreemarkerUtil.class, "/ftl_02");
            // 设置默认字体
            configuration.setDefaultEncoding("utf-8");
            // 获取模板
            Template template = configuration.getTemplate(templateName);
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            } else {
                result.setCode(-1);
                result.setMessage("已经存在" + fileName);
                return result;
            }

            //设置输出流
            out = new FileWriter(file);
            //模板输出静态文件
            template.process(dataModel, out);

            result.setCode(1);
            result.setMessage("创建" + fileName + "成功");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        result.setCode(-1);
        result.setMessage("创建" + fileName + "失败");
        return result;
    }

    public static String messageStr(String name) {
        if (name.equals("entity.ftl")) {
            name = ".java";
        } else if (name.equals("mapper.ftl")) {
            name = "Dao.java";
        } else if (name.equals("mapper_xml.ftl")) {
            name = "Mapper.xml";
        } else if (name.equals("service.ftl")) {
            name = "Service.java";
        } else if (name.equals("serviceImpl.ftl")) {
            name = "ServiceImpl.java";
        } else if (name.equals("req_create.ftl")) {
            name = "SaveReq.java";
        } else if (name.equals("req_modify.ftl")) {
            name = "UpdateReq.java";
        } else if (name.equals("req_page.ftl")) {
            name = "PageReq.java";
        } else if (name.equals("req_list.ftl")) {
            name = "ListReq.java";
        } else if (name.equals("enum.ftl")) {
            name = ".java";
        }
        return name;
    }
}

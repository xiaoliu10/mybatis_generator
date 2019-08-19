package com.test.util;

import com.test.enums.MethodEnum;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.DefaultCommentGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

/**
 * Description: mybatis_generator
 * Created by zhangxun27348 on 2019/8/15 16:35
 *
 * @author zhangxun27348
 */
public class MyCommentGenerator extends DefaultCommentGenerator {

    private Properties properties;
    private boolean suppressDate;

    private boolean suppressAllComments;

    private String author;

    private String pattern;

    public MyCommentGenerator() {
        super();
        properties = new Properties();
        suppressDate = false;
        suppressAllComments = false;
    }

    interface Constant {

        String AUTHOR = "author";

        String PATTERN = "pattern";

        String DEF_AUTHOR = "Mr.zhang";

        String DEF_PATTERN = "yyyy-MM-dd HH:mm:ss";
    }

    /**
     * 读xml 配置文件
     *
     * @param properties
     */
    @Override
    public void addConfigurationProperties(Properties properties) {
        super.addConfigurationProperties(properties);
        this.properties.putAll(properties);

        suppressDate = isTrue(properties
                .getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));

        suppressAllComments = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));
        author = properties.getProperty(Constant.AUTHOR) == null ? Constant.DEF_AUTHOR : properties.getProperty(Constant.AUTHOR);
        pattern = properties.getProperty(Constant.PATTERN) == null ? Constant.DEF_PATTERN : properties.getProperty(Constant.PATTERN);
    }

    @Override
    protected void addJavadocTag(JavaElement javaElement,
                                 boolean markAsDoNotDelete) {
        javaElement.addJavaDocLine(" *");
        javaElement.addJavaDocLine(" * @author " + author);
        StringBuilder sb = new StringBuilder();
        sb.append(" * @date ");
        String s = getDateString();
        if (s != null) {
            sb.append(s);
        }
        javaElement.addJavaDocLine(sb.toString());
    }

    @Override
    public void addClassComment(InnerClass innerClass,
                                IntrospectedTable introspectedTable) {
        this.addClassComment(innerClass, introspectedTable, false);
    }

    @Override
    public void addGeneralMethodComment(Method method,
                                        IntrospectedTable introspectedTable) {
        String name = method.getName();
        MethodEnum methodEnum = MethodEnum.nameOf(name);
        if (null != methodEnum){
            String describe = methodEnum.getDescribe();
            StringBuilder sb = new StringBuilder();
            method.addJavaDocLine("/**");
            sb.append(" * 方法名 ");
            sb.append(method.getName());
            method.addJavaDocLine(sb.toString());
            method.addJavaDocLine(" * " + describe);
            List<Parameter> parameters = method.getParameters();
            parameters.stream().forEach(entity -> {
                method.addJavaDocLine(" * @param " + entity.getName());
            });
            method.addJavaDocLine(" * @return " + method.getReturnType().getShortName());
            method.addJavaDocLine(" */");
        }
    }

    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
            StringBuilder sb = new StringBuilder();
            innerClass.addJavaDocLine("/**");
            sb.append(" * 此类对应于数据库表 ");
            sb.append(introspectedTable.getFullyQualifiedTable());
            innerClass.addJavaDocLine(sb.toString());
            innerClass.addJavaDocLine(" * 此类为 " + introspectedTable.getRemarks() + "表的CRUD");
            addJavadocTag(innerClass, markAsDoNotDelete);
            innerClass.addJavaDocLine(" */");
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // 获取表注释
        String remarks = introspectedTable.getRemarks();

        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * " + remarks);
        topLevelClass.addJavaDocLine(" *");
        topLevelClass.addJavaDocLine(" * @tableName " + introspectedTable.getFullyQualifiedTable());
        topLevelClass.addJavaDocLine(" * @author " + author);
        String s = getDateString();
        if (s != null) {
            topLevelClass.addJavaDocLine(" * @date " + s);
        }
        topLevelClass.addJavaDocLine(" */");
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
            // 获取列注释
            String remarks = introspectedColumn.getRemarks();
            field.addJavaDocLine("/**");
            field.addJavaDocLine(" * " + remarks);
            field.addJavaDocLine(" */");
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
            field.addJavaDocLine("/**");
            field.addJavaDocLine(" * " + field.getName());
            this.addJavadocTag(field, false);
            field.addJavaDocLine(" */");
    }

    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {
    }

    @Override
    public void addGetterComment(Method method,
                                 IntrospectedTable introspectedTable,
                                 IntrospectedColumn introspectedColumn) {
        // 不需要getter 注解
    }

    @Override
    public void addSetterComment(Method method,
                                 IntrospectedTable introspectedTable,
                                 IntrospectedColumn introspectedColumn) {
        // 不需要setter 注解
    }


    @Override
    protected String getDateString() {
        if (!this.suppressDate) {
            return null;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.format(new Date());
        }
    }

}

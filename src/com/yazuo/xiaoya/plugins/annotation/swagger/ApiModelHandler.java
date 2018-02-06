package com.yazuo.xiaoya.plugins.annotation.swagger;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.yazuo.xiaoya.plugins.annotation.swagger.SwaggerAnnotationHandler;
import com.yazuo.xiaoya.plugins.constanst.Constants;
import com.yazuo.xiaoya.plugins.entity.Class;
import com.yazuo.xiaoya.plugins.entity.Field;
import com.yazuo.xiaoya.plugins.utils.AnnotationUtil;

import java.util.HashMap;
import java.util.Map;

import static com.yazuo.xiaoya.plugins.constanst.Constants.SWAGGER_PREFIX;

/**
 * ApiModel的注解实现通过解析注释自动添加类和字段级别的注解
 * Created by scvzerng on 2017/6/23.
 */
public class ApiModelHandler extends SwaggerAnnotationHandler {
    public static final String MODEL = "ApiModel";
    public static final String MODEL_PROPERTY = "ApiModelProperty";
    public static final String MODEL_PARAM = "ApiParam";
    private int param;
    public ApiModelHandler(Project project,Class clazz,int param) {
        super(project,clazz);
        this.param = param;
    }

    @Override
    public void addMethodAnnotation() {
      //nothing to do
    }

    @Override
    public void addFieldAnnotation() {
        this.clazz.getFields().stream().filter(this::isNotExist).map(Field::getDocument).forEach(doc->{
            String annotationText = doc.getDescriptionText().trim();
            Map<String,String> params = new HashMap<>();
            if(param>0){
                params.put("notes",annotationText);

            }else{
                params.put("value",annotationText);

            }
            PsiAnnotation annotation = elementFactory.createAnnotationFromText(AnnotationUtil.createAnnotationText(param>0?MODEL_PARAM:MODEL_PROPERTY,params),clazz.getPsiClass());
            doc.addAnnotation(annotation);
        });
    }

    @Override
    public void addClassAnnotation() {
        String annotationText = getAnnotationText(clazz);
        if(annotations.findAnnotation(SWAGGER_PREFIX+"."+MODEL)==null){
            Map<String,String> params = new HashMap<>();
            params.put("description",annotationText);
            PsiAnnotation annotation = elementFactory.createAnnotationFromText(AnnotationUtil.createAnnotationText(MODEL,params),clazz.getPsiClass());
            this.clazz.getDocument().addAnnotation(annotation);

        }
    }

    private String getAnnotationText(Class clazz){
        return clazz.getDocument().firstLine().trim();
    }
    private boolean isNotExist(Field field){
        return field.getField().getModifierList().findAnnotation(Constants.SWAGGER_PREFIX+"."+MODEL_PROPERTY)==null;
    }

}

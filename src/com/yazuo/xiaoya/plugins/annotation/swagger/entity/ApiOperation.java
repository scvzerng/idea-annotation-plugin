package com.yazuo.xiaoya.plugins.annotation.swagger.entity;

import com.intellij.psi.PsiAnnotation;
import com.yazuo.xiaoya.plugins.entity.Method;
import com.yazuo.xiaoya.plugins.utils.AnnotationUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Year: 2017-2017/6/24-14:10
 * Project:idea-plugins-plugin
 * Package:com.yazuo.xiaoya.plugins
 * To change this template use File | Settings | File Templates.
 */
public class ApiOperation {
    private String value;
    private String notes;
    private String response;
    private String httpMethod;
    public ApiOperation(Method method){
        PsiAnnotation annotation = AnnotationUtil.getHttpAnnotation(method.getMethod());
        String annotationText = annotation.getText();
        this.httpMethod =annotationText.substring(1,annotationText.indexOf("Mapping")).toUpperCase();
        String text = method.getMethod().getReturnType().getPresentableText();
        this.response = text.contains("<")?method.getDocument().getReturnTag().get().getValue():text;
        this.notes = method.getDocument().firstLine().trim();
        this.value = method.getDocument().firstLine().trim();
    }


    @Override
    public String toString() {
        return String.format("@ApiOperation(value = \"%s\",notes = \"%s\",response =%s.class, httpMethod = \"%s\")",value,notes,response,httpMethod);
    }



}
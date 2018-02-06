package com.yazuo.xiaoya.plugins.annotation.swagger;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.yazuo.xiaoya.plugins.annotation.swagger.entity.ApiOperation;
import com.yazuo.xiaoya.plugins.entity.Class;
import com.yazuo.xiaoya.plugins.entity.Document;
import com.yazuo.xiaoya.plugins.entity.Method;
import com.yazuo.xiaoya.plugins.utils.AnnotationUtil;

import java.util.HashMap;
import java.util.Map;

import static com.yazuo.xiaoya.plugins.constanst.Constants.SWAGGER_PREFIX;

/**
 * Api的实现可以自动扫描注释和spring mvc的注解来生成简单的swagger注解
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Year: 2017-2017/6/24-19:41
 * Project:idea-plugins-plugin
 * Package:com.yazuo.xiaoya.plugins
 * To change this template use File | Settings | File Templates.
 */

public class ApiHandler extends SwaggerAnnotationHandler {
    public static final String API = "Api";
    public static final String API_METHOD = "ApiOperation";
    public ApiHandler(Project project, Class clazz) {
        super(project, clazz);
    }
    class HttpMethod{
        private ApiOperation apiOperation;
        private Method method;

        public HttpMethod(Method method) {
            this.method = method;
            this.apiOperation = new ApiOperation(method);
        }

        public ApiOperation getApiOperation() {
            return apiOperation;
        }

        public void setApiOperation(ApiOperation apiOperation) {
            this.apiOperation = apiOperation;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }
    }
    @Override
    public void addMethodAnnotation() {
             this.clazz.getMethods().stream()
                     .filter(AnnotationUtil::isRest)
                     .filter(this::isNotExist)
                     .map(HttpMethod::new).
                     forEach(httpMethod->{
                         Document document = httpMethod.getMethod().getDocument();
                         PsiAnnotation annotation = elementFactory.createAnnotationFromText(httpMethod.getApiOperation().toString(),clazz.getPsiClass());
                         document.addAnnotation(annotation);
     });
    }


    @Override
    public void addFieldAnnotation() {
        //nothing to do
    }


    @Override
    public void addClassAnnotation() {
        if(AnnotationUtil.isRestController(this.clazz.getPsiClass())&&isNotExist(this.clazz)){
            Map<String,String> params = new HashMap<>();
            params.put("description",clazz.getDocument().firstLine().trim());
            PsiAnnotation api = elementFactory.createAnnotationFromText(AnnotationUtil.createAnnotationText(API,params),clazz.getPsiClass());
           clazz.getDocument().addAnnotation(api);
        }
    }

    private boolean isNotExist(Method method){
        return method.getMethod().getModifierList().findAnnotation(SWAGGER_PREFIX+"."+API_METHOD)==null;
    }
    private boolean isNotExist(Class clazz){
        return clazz.getPsiClass().getModifierList().findAnnotation(SWAGGER_PREFIX+"."+API)==null;
    }


}

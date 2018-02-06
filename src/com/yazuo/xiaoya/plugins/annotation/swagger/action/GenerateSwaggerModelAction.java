package com.yazuo.xiaoya.plugins.annotation.swagger.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.yazuo.xiaoya.plugins.entity.FileClass;
import com.yazuo.xiaoya.plugins.annotation.AnnotationHandler;
import com.yazuo.xiaoya.plugins.annotation.swagger.ApiHandler;
import com.yazuo.xiaoya.plugins.annotation.swagger.ApiModelHandler;
import com.yazuo.xiaoya.plugins.utils.AnnotationUtil;
/**
 * 自动根据注释生成swagger注解
 * Created by scvzerng on 2017/6/20.
 */
public class GenerateSwaggerModelAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        DataContext dataContext = anActionEvent.getDataContext();
        PsiFile psiFile = anActionEvent.getData(DataKeys.PSI_FILE);
        Project project = CommonDataKeys.PROJECT.getData(dataContext);
        FileClass fileClass = new FileClass((PsiJavaFile) psiFile,project);
        int state = Messages.showYesNoDialog(project,"是否为@ApiParam","Model","是","否", AllIcons.FileTypes.Any_type);
        new WriteCommandAction.Simple(project,psiFile){
            @Override
            protected void run() throws Throwable {
                    fileClass.process(clazz->{
                        AnnotationHandler handler = AnnotationUtil.isRestController(clazz.getPsiClass())?new ApiHandler(project,clazz): new ApiModelHandler(project,clazz,state);
                        handler.addClassAnnotation();
                        handler.addFieldAnnotation();
                        handler.addMethodAnnotation();
                    });
            }
        }.execute();
    }

}

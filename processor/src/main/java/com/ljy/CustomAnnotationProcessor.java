package com.ljy;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

/**
 * Created by LJY on 2018/4/9.
 * <p>
 * Android中使用AbstractProcessor在编译时生成代码apt
 */
@SupportedAnnotationTypes({"com.ljy.CustomClass", "com.ljy.CustomAnnotation"})//待处理的注解全称
//@SupportedSourceVersion(SourceVersion.RELEASE_7)//表示处理的JAVA版本
public class CustomAnnotationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (Element elementClass : roundEnvironment.getElementsAnnotatedWith(CustomClass.class)) {
            String tagClass = elementClass.getAnnotation(CustomClass.class).value();
            String classPath = elementClass.toString();
            String className = elementClass.getSimpleName().toString();
            StringBuilder builder = new StringBuilder()
                    .append("package com.ljy.ljyutils.annotation;\n\n")
                    .append("import com.ljy.util.LjyLogUtil;\n")
                    .append("import " + classPath + ";\n")
                    .append("public class " + className + "Utils {\n\n");
            for (Element elementMethod : roundEnvironment.getElementsAnnotatedWith(CustomAnnotation.class)) {
                String methodName = elementMethod.getSimpleName().toString();
                String tagMethod = elementMethod.getAnnotation(CustomAnnotation.class).value();
                if (tagClass.equals(tagMethod)) {
                    builder.append("\tpublic static void " + methodName + "(" + className + " who){\n")
                            .append("\t\tLjyLogUtil.i(\"doSomthing before call " + className + "." + methodName + "\");\n")
                            .append("\t\twho." + methodName + "();\n")
                            .append("\t}\n");
                }
            }
            builder.append("}\n");
            try {
                JavaFileObject source = processingEnv.getFiler().createSourceFile(
                        "com.ljy.ljyutils.annotation." + className + "Utils");
                Writer writer = source.openWriter();
                writer.write(builder.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}

package com.tyron.completion.java.util;

import static com.tyron.completion.progress.ProgressManager.checkCanceled;

import com.tyron.completion.java.compiler.CompileTask;
import com.tyron.completion.java.provider.ScopeHelper;

import org.openjdk.javax.lang.model.element.Element;
import org.openjdk.javax.lang.model.element.ElementKind;
import org.openjdk.javax.lang.model.element.ExecutableElement;
import org.openjdk.javax.lang.model.element.Modifier;
import org.openjdk.javax.lang.model.element.TypeElement;
import org.openjdk.javax.lang.model.element.VariableElement;
import org.openjdk.javax.lang.model.type.DeclaredType;
import org.openjdk.javax.lang.model.type.TypeMirror;
import org.openjdk.source.tree.Scope;

import java.util.List;
import java.util.Set;

public class ElementUtil {

    public static String simpleType(TypeMirror mirror) {
        return simpleClassName(mirror.toString());
    }

    public static String simpleClassName(String name) {
        return name.replaceAll("[a-zA-Z\\.0-9_\\$]+\\.", "");
    }


    public static boolean isEnclosingClass(DeclaredType type, Scope start) {
        checkCanceled();

        for (Scope s : ScopeHelper.fastScopes(start)) {
            // If we reach a static method, stop looking
            ExecutableElement method = s.getEnclosingMethod();
            if (method != null && method.getModifiers().contains(Modifier.STATIC)) {
                return false;
            }
            // If we find the enclosing class
            TypeElement thisElement = s.getEnclosingClass();
            if (thisElement != null && thisElement.asType().equals(type)) {
                return true;
            }
            // If the enclosing class is static, stop looking
            if (thisElement != null && thisElement.getModifiers().contains(Modifier.STATIC)) {
                return false;
            }
        }
        return false;
    }

    public static boolean isFinal(ExecutableElement element) {
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers == null) {
            return false;
        }
        return modifiers.contains(Modifier.FINAL);
    }

    /**
     * Search all the super class of the provided TypeElement to see if the given method is
     * overriding a method from them
     *
     * This is a hack, since ExecutableElement does not override equals and hashCode, i manually
     * checked the necessary information to determine on whether the two methods are equal.
     */
    public static boolean isMemberOf(CompileTask task, ExecutableElement method, TypeElement aClass) {
        if (aClass == null) {
            return false;
        }
        outer : for (Element member : task.task.getElements().getAllMembers(aClass)) {
            if (member.getKind() != ElementKind.METHOD) {
                continue;
            }
            ExecutableElement execMember = (ExecutableElement) member;
            if (ElementUtil.isFinal(execMember)) {
                // cannot override final methods
                continue;
            }

            // check the name of methods
            if (!method.getSimpleName().contentEquals(execMember.getSimpleName())) {
                continue;
            }

            // return type does not matter when checking if two methods are equal

            List<? extends VariableElement> parameters = method.getParameters();
            List<? extends VariableElement> execParameters = execMember.getParameters();
            if (parameters.size() != execParameters.size()) {
                continue;
            }

            // check for the parameter types, only checking their class names
            for (VariableElement parameter : parameters) {
                for (VariableElement execParameter : execParameters) {
                    if (parameter != null && execParameter != null) {
                        if (!parameter.asType().toString().equals(execParameter.asType().toString())) {
                            continue outer;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}

package org.zstack.core.config;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Target({java.lang.annotation.ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface GlobalConfigTemplateDef {
    String type() default "system";
    String defaultValue() default "";
    String validatorRegularExpression() default "";
}
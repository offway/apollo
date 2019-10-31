package cn.offway.apollo.dynamic;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DS {
    String value() default DataSourceNames.SR;
}

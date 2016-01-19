package com.test.rmmfl.annotation;

/**
 * Created by opolishchuk on 15.10.2015.
 */
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Qualifier("dataSourceMySQL")
public @interface ToolsDataSource {
}

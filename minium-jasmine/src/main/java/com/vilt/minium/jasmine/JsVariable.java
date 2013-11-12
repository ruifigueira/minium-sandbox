package com.vilt.minium.jasmine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsVariable {
    
    public enum ResourceType {
        NONE,
        JSON,
        STRING
    }
    
    public String value();
    public ResourceType resourceType() default ResourceType.NONE;
}

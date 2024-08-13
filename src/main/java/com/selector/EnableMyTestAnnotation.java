package com.selector;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Import(MySelectorClass.class)
public @interface EnableMyTestAnnotation {
}

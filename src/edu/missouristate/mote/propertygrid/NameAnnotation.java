package edu.missouristate.mote.propertygrid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method annotation that provides the name of the property associated with the
 * method. For use with the PropertyGrid/PropertyTable controls.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NameAnnotation {
    /**
     * Get the value of the annotation.
     * @return annotation value
     */
    String value();
}
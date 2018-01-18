package engine.game_elements;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation for retaining the names of behavior object parameters at runtime, which is needed to aid the reflective
 * generation of game elements.
 *
 * @author Ben Schwennesen
 */
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface ElementProperty {
    /**
     * Get the name of the property.
     *
     * @return the name of the property, ideally corresponding to the name of the parameter in the beahvior object
     * constructor
     */
    String value();

    /**
     * Check if the property is part of a template. For example, movement speed should be part of a template whereas
     * starting position should not.
     *
     * @return true if the property is part of the template scheme for elements, false otherwise
     */
    boolean isTemplateProperty();
}

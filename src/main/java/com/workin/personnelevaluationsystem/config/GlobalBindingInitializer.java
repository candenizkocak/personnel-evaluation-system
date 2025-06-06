package com.workin.personnelevaluationsystem.config;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * This class provides global configuration for data binding across all controllers.
 * It ensures that common form submission issues are handled gracefully.
 */
@ControllerAdvice
public class GlobalBindingInitializer {

    /**
     * This method customizes the data binder for every web request.
     * The @InitBinder annotation marks this method to be called to initialize the WebDataBinder.
     *
     * @param binder the WebDataBinder for the current request
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // This is the key part.
        // It registers an editor for the String class.
        // The StringTrimmerEditor trims whitespace from strings.
        // The second argument 'true' tells the editor to convert an empty string to a null value.
        // This prevents NumberFormatExceptions when optional number fields are submitted as empty strings.
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
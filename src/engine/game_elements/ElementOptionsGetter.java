package engine.game_elements;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Translates properties of sprites to and from friendly descriptions that are displayed in the frontend.
 *
 * @author Ben Schwennesen
 */
public class ElementOptionsGetter {

    private final String PROPERTIES_EXTENSION = ".properties";
    private final String PARAMETER_TRANSLATIONS_FILE_NAME = "ParameterTranslations" + PROPERTIES_EXTENSION;
    private final String SPRITE_BASE_PARAMETER_NAME = GameElement.class.getName();
    private Properties parameterTranslationProperties;

    private Map<String, List<String>> spriteParameterSubclassOptions = new HashMap<>();

    private Map<String, String> classToDescription = new HashMap<>();
    private Map<String, String> descriptionToClass = new HashMap<>();
    //           spritep    param   descrip
    private Map<String, Map<String, Class>> spriteMemberParametersMap = new HashMap<>();
    private Map<String, String> parameterToDescription = new HashMap<>();
    private Map<String, String> descriptionToParameter = new HashMap<>();

    public ElementOptionsGetter() {
        loadTranslations();
    }

    private void loadTranslations() {
        initializeParameterTranslations();
        for (Parameter spriteParameter : GameElement.class.getConstructors()[0].getParameters()) {
            try {
                loadTranslationsForElementParameter(spriteParameter);
            } catch (IOException | ReflectiveOperationException failedToLoadTranslationsException) {
                // TODO - handle
            }
        }
    }

    private void initializeParameterTranslations() {
        parameterTranslationProperties = new Properties();
        InputStream parameterTranslationStream = getClass().getClassLoader()
                .getResourceAsStream(PARAMETER_TRANSLATIONS_FILE_NAME);
        if (parameterTranslationStream != null) {
            try {
                parameterTranslationProperties.load(parameterTranslationStream);
            } catch (IOException fileDoesntExistException) {
                // TODO - handle
            }
        }
    }

    // TODO - refactor (strongly needed)
    private void loadTranslationsForElementParameter(Parameter spriteParameter) throws IOException,
            ReflectiveOperationException {
        Properties spriteParameterSubclassProperties = new Properties();
        String parameterClassSimpleName = spriteParameter.getType().getSimpleName();
        InputStream parameterClassPossibilitiesStream = getClass().getClassLoader()
                .getResourceAsStream(parameterClassSimpleName + PROPERTIES_EXTENSION);
        if (parameterClassPossibilitiesStream != null) {
            processBehaviorObjectParameterTranslations(spriteParameter,
                    spriteParameterSubclassProperties, parameterClassPossibilitiesStream);
        } else {
            processBehaviorObjectWrapper(spriteParameter);
        }
    }

    private void processBehaviorObjectWrapper(Parameter spriteParameter) throws IOException, ReflectiveOperationException {
        Class parameterClass = spriteParameter.getType();
        Map<String, Class> baseSpriteParameterMap = spriteMemberParametersMap.getOrDefault
                (SPRITE_BASE_PARAMETER_NAME, new HashMap<>());
        ElementProperty elementProperty = spriteParameter.getAnnotation(ElementProperty.class);
        if (elementProperty != null && elementProperty.isTemplateProperty()) {
            processBaseElementParameter(spriteParameter, baseSpriteParameterMap, elementProperty);
        }
        if (parameterClass.getConstructors().length > 0) {
            for (Parameter subparameter : parameterClass.getConstructors()[0].getParameters()){
                loadTranslationsForElementParameter(subparameter);
            }
        }
    }

    private void processBaseElementParameter(Parameter spriteParameter, Map<String, Class> baseSpriteParameterMap, ElementProperty elementProperty) {
        String parameterName = elementProperty.value();
        String parameterDescription = parameterTranslationProperties.getProperty(parameterName);
        String parameterIdentifier = parameterName;
        if (parameterDescription != null) {
            parameterToDescription.put(parameterName, parameterDescription);
            descriptionToParameter.put(parameterDescription, parameterName);
            parameterIdentifier = parameterDescription;
        }
        baseSpriteParameterMap.put(parameterIdentifier, spriteParameter.getType());
        spriteMemberParametersMap.put(SPRITE_BASE_PARAMETER_NAME, baseSpriteParameterMap);
    }

    private void processBehaviorObjectParameterTranslations(Parameter spriteParameter, Properties spriteParameterSubclassProperties, InputStream parameterClassPossibilitiesStream) throws IOException, ReflectiveOperationException {
        spriteParameterSubclassProperties.load(parameterClassPossibilitiesStream);
        String parameterClassFullName = spriteParameter.getType().getName();
        List<String> subclassOptions = new ArrayList<>();
        String referenceClassDescription = null;
        for (String subclassOptionName : spriteParameterSubclassProperties.stringPropertyNames()) {
            String subclassDescription = spriteParameterSubclassProperties.getProperty(subclassOptionName);
            classToDescription.put(subclassOptionName, subclassDescription);
            descriptionToClass.put(subclassDescription, subclassOptionName);
            if (!subclassOptionName.equals(parameterClassFullName)) {
                subclassOptions.add(subclassDescription);
            } else {
                referenceClassDescription = subclassDescription;
            }
            Map<String, Class> parameterDescriptions = spriteMemberParametersMap.getOrDefault
                    (subclassOptionName, new HashMap<>());
            loadTranslationsForElementParameter(subclassOptionName, parameterDescriptions);
            spriteMemberParametersMap.put(subclassOptionName, parameterDescriptions);
        }
        spriteParameterSubclassOptions.put(referenceClassDescription != null ?
                referenceClassDescription : parameterClassFullName, subclassOptions);
    }

    // TODO - refactor
    private void loadTranslationsForElementParameter(String spriteParameterSubclassName, Map<String, Class>
            parameterDescriptionsToClasses) throws ReflectiveOperationException {
        Class spriteParameterSubclass = Class.forName(spriteParameterSubclassName);
        Constructor[] subclassConstructors = spriteParameterSubclass.getConstructors();
        if (subclassConstructors.length > 0) {
            Constructor desiredConstructor = subclassConstructors[0];
            Parameter[] constructorParameters = desiredConstructor.getParameters();
            for (Parameter constructorParameter : constructorParameters) {
                processElementParameter(parameterDescriptionsToClasses, constructorParameter);
            }
        }
    }

    private void processElementParameter(Map<String, Class> parameterDescriptionsToClasses, Parameter constructorParameter) {
        ElementProperty elementPropertyAnnotation = constructorParameter.getAnnotation(ElementProperty.class);
        if (elementPropertyAnnotation != null && elementPropertyAnnotation.isTemplateProperty()) {
            // property that needs to be set in the frontend
            addTemplatePropertyTranslation(parameterDescriptionsToClasses, constructorParameter, elementPropertyAnnotation);
        } else {
            // property that we need to supply ourselves so don't pass it to them
            String parameterTypeSimple = constructorParameter.getType().getSimpleName();
            parameterToDescription.put(parameterTypeSimple, parameterTypeSimple);
            descriptionToParameter.put(parameterTypeSimple, parameterTypeSimple);
            //parameterDescriptionsToClasses.put(parameterTypeSimple, constructorParameter.getType());
        }
    }

    private void addTemplatePropertyTranslation(Map<String, Class> parameterDescriptionsToClasses, Parameter constructorParameter, ElementProperty elementPropertyAnnotation) {
        String parameterName = elementPropertyAnnotation.value();
        String parameterDescription = parameterTranslationProperties.getProperty(parameterName);
        if (parameterDescription != null) {
            parameterToDescription.put(parameterName, parameterDescription);
            descriptionToParameter.put(parameterDescription, parameterName);
            // TODO - eliminate above?
            parameterDescriptionsToClasses.put(parameterDescription, constructorParameter.getType());
        } else {
            parameterToDescription.put(parameterName, parameterName);
            descriptionToParameter.put(parameterName, parameterName);
            parameterDescriptionsToClasses.put(parameterName, constructorParameter.getType());
        }
    }

    public List<String> getConstructorParameterIdentifiers(Parameter[] parameters) {
        // TODO - make sure getParameters() returns them in order
        return Arrays.stream(parameters).map(this::getParameterIdentifier).collect(Collectors.toList());
    }

    private String getParameterIdentifier(Parameter parameter) {
        if (parameter.isAnnotationPresent(ElementProperty.class)) {
            return parameter.getAnnotation(ElementProperty.class).value();
        } else {
            return parameter.getType().getName();
        }
    }

    public Map<String, List<String>> getSpriteParameterSubclassOptions() {
        return spriteParameterSubclassOptions;
    }

    public Map<String, Class> getAuxiliaryParametersFromSubclassChoices(Map<String, String> subclassChoices)
            throws IllegalArgumentException {
        Map<String, Class> auxiliaryParameters = new HashMap<>();
        for (String subclassChoiceDescription : subclassChoices.values()) {
            String subclassChoiceName = descriptionToClass.get(subclassChoiceDescription);
            if (subclassChoiceName == null || !spriteMemberParametersMap.containsKey(subclassChoiceName)) {
                throw new IllegalArgumentException();
            }
            auxiliaryParameters.putAll(spriteMemberParametersMap.get(subclassChoiceName));
        }
        auxiliaryParameters.putAll(spriteMemberParametersMap.getOrDefault(SPRITE_BASE_PARAMETER_NAME, new HashMap<>()));
        return auxiliaryParameters;
    }

    private String translateDescriptionToClass(String description) {
        return descriptionToClass.getOrDefault(description, description);
    }

    private String translateClassToDescription(String className) {
        return classToDescription.getOrDefault(className, className);
    }

    public String translateParameterToDescription(String parameterName) {
        return parameterToDescription.get(parameterName);
    }

    public String getChosenSubclassName(Class parameterClass, Map<String, ?> properties)
            throws IllegalArgumentException {
        String parameterClassDescription = translateClassToDescription(parameterClass.getName());
        Object chosenSubclassNameAsObject = properties.get(parameterClassDescription);
        return chosenSubclassNameAsObject == null ? null :
                translateDescriptionToClass(chosenSubclassNameAsObject.toString());
    }
}

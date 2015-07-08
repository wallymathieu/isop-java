package jisop.domain;

import jisop.help.HelpController;
import jisop.infrastructure.Objects;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mathieu.
 */
public class Controller {
    public final Class type;
    public final Boolean ignoreGlobalUnMatchedParameters;
    public final String name;


    public Controller(Class type, Boolean ignoreGlobalUnMatchedParameters){

        this.type = type;
        this.name = controllerName(type);
        this.ignoreGlobalUnMatchedParameters = ignoreGlobalUnMatchedParameters;
    }
    private static String controllerName(Class type)
    {
        Pattern p = Pattern.compile(Conventions.ControllerName + "$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(type.getSimpleName());
        return m.replaceAll("");
    }

    public Collection<Method> getControllerActionMethods()
    {
        return _GetControllerActionMethods(type).stream().map(m ->
        {
            Method mm = new Method(m);
            mm.controller = this;
            return mm;
        }).collect(Collectors.toList());
    }

    private static Collection<java.lang.reflect.Method> _GetControllerActionMethods(Class type)
    {
        return getOwnPublicMethods(type)
            .filter(m -> !m.getName().equalsIgnoreCase(Conventions.Help))
            .collect(Collectors.toList());
    }

    private static Stream<java.lang.reflect.Method> getOwnPublicMethods(Class type)
    {
        return Arrays.asList(type.getMethods())
                .stream()
                .filter(m -> !m.getDeclaringClass().equals(Object.class))
                .filter(m ->
                                !m.getName().startsWith("get") || !m.getName().startsWith("set")
                );
    }

    public boolean recognize(String controllerName)
    {
        return name.equalsIgnoreCase(controllerName);
    }

    public boolean recognize(String controllerName, String actionName)
    {
        return name.equalsIgnoreCase(controllerName)
                && getMethod(actionName) != null;
    }

    public Method getMethod(String name)
    {
        return getControllerActionMethods()
                .stream()
                .filter(m -> m.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public boolean isHelp()
    {
        return type == HelpController.class; // Is there a better way to do this?
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof Controller)
        {
            return Equals((Controller)obj);
        }
        return false;
    }
    public boolean Equals(Controller obj)
    {
        return type.equals(obj.type);
    }
    public int hashCode()
    {
        return Objects.hashCode(type);
    }
}

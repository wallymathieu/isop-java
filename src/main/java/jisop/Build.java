package jisop;

import jisop.api.ControllerExpression;
import jisop.command_line.ControllerRecognizer;
import jisop.command_line.lex.ArgumentLexer;
import jisop.command_line.parse.ArgumentParameter;
import jisop.command_line.parse.ArgumentParser;
import jisop.command_line.parse.ArgumentWithOptions;
import jisop.command_line.parse.ParsedArguments;
import jisop.domain.*;
import jisop.domain.Formatter;
import jisop.help.HelpController;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import jisop.help.HelpForArgumentWithOptions;
import jisop.help.HelpForControllers;

/**
 *
 * @author mathieu
 */
public class Build {
    private final Configuration _configuration = new Configuration();
    public boolean _allowInferParameter = true; // TODO:FIX
    private final TypeContainer _container = new TypeContainer();
    private HelpController _helpController;
    private HelpForControllers _helpForControllers;
    private HelpForArgumentWithOptions _helpForArgumentWithOptions;

    public Build() {
    }

    public Build parameter(String argument) {
        return parameter(argument, false, null, null);
    }

    public Build parameter(String argument, boolean required) {
        return parameter(argument, required, null, null);
    }

    public Build parameter(String argument,
                           boolean required,
                           String description,
                           Consumer<String> action) {
        _configuration.fields.add(new Field(argument, action, required, description, String.class));
        return this;
    }


    public Build formatObjectsAsTable()
    {
        _configuration.formatter = new TableFormatter();
        return this;
    }

    public Build setFormatter(Formatter formatter){
        _configuration.formatter = formatter;
        return this;
    }

    public ParsedArguments parse(String[] arg) {
        ArgumentParser argumentParser = new ArgumentParser(getGlobalParameters(), _allowInferParameter);
        ArgumentLexer lexed = ArgumentLexer.lex(arg);
        ParsedArguments parsedArguments = argumentParser.parse(lexed, Arrays.asList(arg));
        ControllerRecognizer controllerRecognizer = getControllerRecognizers()
                .values().stream()
                .map(cr -> cr.get())
                .filter(cr -> cr.recognize(arg))
                .findFirst()
                .orElse(null);
        if (null != controllerRecognizer)
        {
            return controllerRecognizer.parseArgumentsAndMerge(arg,
                    parsedArguments);
        }
        parsedArguments.assertFailOnUnMatched();
        return parsedArguments;
    }
    public Build recognizeClass(Class arg) {
        return recognizeClass(arg, false);
    }
    public Build recognizeClass(Class arg, boolean ignoreGlobalUnMatchedParameters) {
        _configuration.recognizes.add(new Controller(arg, ignoreGlobalUnMatchedParameters));
        return this;
    }
    public Build recognize(Object arg) {
        return recognize(arg,false);
    }
    public Build recognize(Object arg, boolean ignoreGlobalUnMatchedParameters) {
        _configuration.recognizes.add(new Controller(arg.getClass(),ignoreGlobalUnMatchedParameters));
        _container.add(arg.getClass(), arg);
        return this;
    }

    public String help() {
        throw new RuntimeException("Not implemented");
    }

    public Map<Class, Supplier<ControllerRecognizer>> getControllerRecognizers() {
        if (_configuration.recognizesHelp
                && !_configuration.recognizes
                    .stream()
                    .anyMatch(c->c.type==HelpController.class))
        {
            recognize(getHelpController(), true);
        }

        return _configuration.recognizes.stream()
                .collect(Collectors.toMap(
                        c -> c.type,
                        c -> getRecognizerSupplier(c)));
    }

    private Supplier<ControllerRecognizer> getRecognizerSupplier(Controller c){
        return ()->new ControllerRecognizer(c, _configuration, _container, _allowInferParameter);
    }

    private Object getHelpController() {
        if (_helpController == null && _configuration.recognizesHelp)
        {
            _helpForControllers = new HelpForControllers(_configuration.recognizes, _container);
            _helpForArgumentWithOptions = new HelpForArgumentWithOptions(getGlobalParameters());
            _helpController = new HelpController(_helpForArgumentWithOptions, _helpForControllers);
        }
        return _helpController;
    }

    public Collection<Argument> getGlobalParameters() {
        return _configuration.fields.stream().map(p ->
                new ArgumentWithOptions(
                        ArgumentParameter.parse(p.name),
                        p.action,
                        p.required,
                        p.description,
                        p.type)).collect(Collectors.toList());
    }

    public Build setFactory(Function<Class,Object> objectFactory) {
        _container.setFactory(objectFactory);
        return this;
    }

    public ControllerExpression controller(String controllerName) {
        return new ControllerExpression(controllerName, this);
    }

    /*
    public Build configurationFrom(String path) {
        throw new NotImplementedException();
    }

    public Build configuration(Object conf) {
        throw new NotImplementedException();
    }*/
}

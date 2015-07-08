package jisop.help;

import jisop.domain.Controller;
import jisop.domain.TypeContainer;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Created by mathieu.
 */
public class HelpForControllers {
    private final Collection<Controller> controllers;
    private final Function<Class, Object> typeContainer;

    public HelpForControllers(Collection<Controller> controllers, Function<Class, Object> typeContainer) {

        this.controllers = controllers;
        this.typeContainer = typeContainer;
    }
}

package isop.help;

/**
 * Created by mathieu.
 */
public class HelpController {
    private final HelpForArgumentWithOptions helpForArgumentWithOptions;
    private final HelpForControllers helpForControllers;

    public HelpController(HelpForArgumentWithOptions helpForArgumentWithOptions, HelpForControllers helpForControllers) {

        this.helpForArgumentWithOptions = helpForArgumentWithOptions;
        this.helpForControllers = helpForControllers;
    }
}

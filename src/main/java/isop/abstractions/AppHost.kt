package isop.abstractions

/**
 * AppHost contains the service provider and configuration needed to run a command line app
 */
interface AppHost {
    /**
     * Controllers exposed by the API
     */
    val controllers: Collection<Controller>
    /**
     * Parse command line arguments and return parsed arguments entity
     */
    fun parse(arg:Collection<String>):Parsed

    /**
     * Return help-text
     */
    fun help():String

    fun controller(controllerName:String): Controller
}
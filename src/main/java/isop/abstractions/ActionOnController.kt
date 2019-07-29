package isop.abstractions

import isop.domain.Argument

interface ActionOnController {
    /**
     *  Get arguments for controller action
     */
    val arguments:Collection<Argument>
    /**
     * Action name
     */
    val name: String
    /**
     * send parameters to controller actions
     */
    fun parameters(parameters:Map<String,String>) : Parsed
    /**
     * Get help for controller action
     */
    fun help(): String
}
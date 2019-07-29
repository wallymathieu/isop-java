package isop.api

import isop.AppHostBuilder

/**
 * Created by mathieu.
 */
class ControllerExpression(private val controllerName: String, private val build: AppHostBuilder) {
    fun action(actionName: String): ActionControllerExpression {
        return ActionControllerExpression(controllerName, actionName, build)
    }
}

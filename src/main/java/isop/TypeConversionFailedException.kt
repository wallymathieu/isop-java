/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isop

import isop.infrastructure.KeyValuePair

/**
 *
 * @author mathieu
 */
class TypeConversionFailedException : RuntimeException {
    constructor(message: String) : super(message) {}

    constructor(message: String, e: Exception) : super(message, e) {}

    constructor(message: String, e: Exception?, arg1: KeyValuePair<String, String?>, type: Class<*>?) : super(String.format("%s where %s: %s to %s", message, arg1.key, arg1.value, type?.simpleName), e) {}
}

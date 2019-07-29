package isop.domain

import java.util.function.Consumer

/**
 * Created by mathieu.
 */
class Field(val name: String,
            val action: Consumer<String>?,
            val required: Boolean,
            val description: String?)

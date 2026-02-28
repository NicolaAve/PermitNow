package configuration

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(name = "Encryption", strict = false)
object CipherConfiguration {
    @field:Element(name = "Key", required = false)
    var key: String = ""
}
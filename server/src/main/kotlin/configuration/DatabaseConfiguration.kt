package configuration

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(name = "Database", strict = false)
object DatabaseConfiguration {
    @field:Element(name = "DBConnectionString", required = false)
    var dbConnectionString: String = ""

    @field:Element(name = "DBDriver", required = false)
    var dbDriver: String = ""

    @field:Element(name = "DBUser", required = false)
    var dbUser: String = ""

    @field:Element(name = "DBPassword", required = false)
    var dbPassword: String = ""
}
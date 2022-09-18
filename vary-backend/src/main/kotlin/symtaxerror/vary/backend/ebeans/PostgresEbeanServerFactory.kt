package symtaxerror.vary.backend.ebeans

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.ebean.Database
import io.ebean.DatabaseFactory
import io.ebean.config.DatabaseConfig
import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
@Primary
class PostgresEbeanServerFactory(
    val mapper: ObjectMapper,
    @Value("\${spring.datasource.url}") val url: String,
    @Value("\${spring.datasource.driver-class-name}") val driverClassName: String,
    @Value("\${spring.datasource.username}") val username: String,
    @Value("\${spring.datasource.password}") val password: String,
    val dataSource: DataSource
) : FactoryBean<Database> {
    override fun getObject(): Database {
        return createDbConfig()
    }

    override fun getObjectType(): Class<*>? {
        return Database::class.java
    }

    override fun isSingleton(): Boolean {
        return true
    }

    fun createDbConfig(): Database {
        val config = DatabaseConfig().also {
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            it.addPackage("symtaxerror.vary.backend.entities")
            it.dataSource = DataSourceBuilder
                .create()
                .url(url)
                .driverClassName(driverClassName)
                .username(username)
                .password(password)
                .build()
            it.objectMapper = mapper
            it.isExpressionNativeIlike = true
            it.isDefaultServer = true
        }

        return DatabaseFactory.create(config)
    }
}
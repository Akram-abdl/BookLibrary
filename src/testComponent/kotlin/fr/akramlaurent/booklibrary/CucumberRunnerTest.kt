package fr.akramlaurent.booklibrary

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.cucumber.java.Before
import io.cucumber.java.Scenario
import io.cucumber.spring.CucumberContextConfiguration
import io.restassured.RestAssured
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.lifecycle.Startables


@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CucumberRunnerTest {

    @LocalServerPort
    private val port = 0

    companion object {
        @Container
        private val postgreSQLContainer: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:13-alpine")

        init {
            Startables.deepStart(postgreSQLContainer).join()
        }

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
        }
    }

    @Before
    fun setup(scenario: Scenario) {
        RestAssured.baseURI = "http://localhost:$port"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        performQuery("DELETE FROM book")
    }

    protected fun performQuery(sql: String) {
        val hikariConfig = HikariConfig()
        hikariConfig.setJdbcUrl(postgreSQLContainer.jdbcUrl)
        hikariConfig.username = postgreSQLContainer.username
        hikariConfig.password = postgreSQLContainer.password
        hikariConfig.setDriverClassName(postgreSQLContainer.driverClassName)
        val ds = HikariDataSource(hikariConfig)

        val statement = ds.getConnection().createStatement()
        statement.execute(sql)
    }
}
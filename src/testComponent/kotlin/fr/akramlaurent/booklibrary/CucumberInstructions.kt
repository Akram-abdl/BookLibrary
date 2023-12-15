package fr.akramlaurent.booklibrary

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured
import io.restassured.path.json.JsonPath
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse

class CucumberSteps {
    private lateinit var bookResponse: ValidatableResponse
    private lateinit var apiResponse: Response

    @Given("a book is created by the user with title {string} and author {string}")
    fun createBook(title: String, author: String) {
        RestAssured.given()
                .contentType("application/json")
                .body(
                        """
                {
                    "title": "$title",
                    "author": "$author"
                }
                """.trimIndent()
                )
                .`when`()
                .post("/books")
                .then()
                .statusCode(200)
    }

    @When("the user retrieves all books")
    fun retrieveAllBooks() {
        bookResponse = RestAssured.given()
                .`when`()
                .get("/books")
                .then()
                .statusCode(200)
    }

    @Then("the user should find the following books")
    fun verifyBooks(payload: List<Map<String, Any>>) {
        val expectedResponse: String = payload.joinToString(prefix = "[", postfix = "]", separator = ",") { line ->
            """
                {
                    "title": "${line["title"]}",
                    "author": "${line["author"]}",
                    "reserved": ${line["reserved"]}
            }
            """.trimIndent()
        }

        assertThat(bookResponse.extract().body().jsonPath().prettify())
                .isEqualTo(JsonPath(expectedResponse).prettify())
    }

    @When("the user reserves a book with title {string} and author {string}")
    fun reserveBook(title: String, author: String) {
        apiResponse = RestAssured.given()
                .contentType("application/json")
                .body(
                        """
                {
                    "title": "$title",
                    "author": "$author"
                }
                """.trimIndent()
                )
                .`when`()
                .post("/books/reserve")
    }

    @Then("the user should receive error message {int} {string}")
    fun verifyErrorMessage(errorStatus: Int, message: String) {
        apiResponse
            .then()
            .statusCode(errorStatus)
            .extract()
            .asString()
            .let {
                assertThat(it).isEqualTo(message)
            }
    }
}
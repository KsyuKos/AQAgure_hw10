package ksyu.kos;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;



public class SimpleTest {

    @BeforeEach
    void setUp() {
        Selenide.open("https://ya.ru");
    }

    @Disabled
    @Test
    @DisplayName("Test 1")
    void simpleTest () {
        String ex = returnOne();
        Assertions.assertEquals("1",ex,"Test is failed. Go to report");
    }

    String returnOne(){
        return "1";
    }


    @ValueSource(strings = {
            "QA","Allure","Selenide"
    })
    @ParameterizedTest(name = "Проверка выдачи результатов поиска яндекса по запросу {0}")
    @Blocker
    void SearchResultsShouldBeGreaterThen10 (String data) {
        $("#text").setValue(data).pressEnter();
        $$("li.serp-item").shouldHave(sizeGreaterThan(9));
    }

    @Disabled
    @Test
    @DisplayName("Проверка доступности перехода в режим поиска по голосу")
    void SearchPageShouldHaveImage () {
        $("svg.search3__svg_voice").click();
        $("div.alice-container").shouldHave(text("Чем я могу помочь?"));
    }

    @CsvSource(value = {
            "QA,    QA-инженер: кто это и какие навыки требуются в 2023 году",
            "Selenide,  Selenide: лаконичные и стабильные UI тесты на Java"
    })
    @ParameterizedTest(name = "Проверка наличия заголовка {1} в первом результате выдачи по запросу {0}")
    @Blocker
    void SearchResultsShouldBeGreaterThen10 (String testdata,String caption) {
        $("#text").setValue(testdata).pressEnter();
        $$("li.serp-item").first().shouldHave(text(testdata));
    }

}

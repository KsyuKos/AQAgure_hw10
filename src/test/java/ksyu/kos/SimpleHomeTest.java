package ksyu.kos;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class SimpleHomeTest {
    @BeforeEach
    void setUp() {
        Selenide.open("https://www.gosuslugi.ru/");
    }

    static Stream<Arguments> findServiceLabelAndNavigateToChat() {
        return Stream.of(
                Arguments.of("Запись к врачу",
                        List.of("Записаться к врачу", "Порядок записи", "Отмена или перенос записи", "Возникла проблема")),
                Arguments.of("Загранпаспорт",
                        List.of("Оформить загранпаспорт", "Порядок оформления", "Посмотреть данные загранпаспорта",
                                "Запись в МВД"))
        );
    }

    @ParameterizedTest
    @Tag("SMOKE")
    @DisplayName("При выборе категории из топ-5 должен отображаться соответствующий список услуг")
    @MethodSource
    void findServiceLabelAndNavigateToChat(String serviceLabel, List<String> dialogMenu) {
        $$("portal-plain-button.ng-star-inserted").findBy(text(serviceLabel)).click();
        $$("portal-search-message-button.ng-star-inserted").
                filterBy(visible).
                shouldHave(texts(dialogMenu));
    }

    @ParameterizedTest
    @ValueSource (strings = {"Популярные услуги","Здоровье","Справки Выписки"})
    @Tag("SMOKE")
    @DisplayName("При клике на категорию услуги должно открыться всплывающее окно с описанием")
    void serviceCategoryShouldHaveFullDescription (String category) {
        $("lib-cat-nav-categories div").$$("button").findBy(attribute("aria-label", category)).click();
        $("lib-cat-nav-tab.ng-star-inserted").shouldNotBe(empty);
    }

    @ParameterizedTest(name = "При выборе группы {0} топ лист услуг должен иметь заданный размер {1}")
    @CsvFileSource(resources = {"/testdata/targetGroupShouldHaveTopListServices.csv"},delimiter = '|')
    @Tag("SMOKE")
    void GroupShouldHaveFixedAmountServices (String targetGroup, int amountServices) {
        $("lib-header-select-role.ng-star-inserted").click();
        $(".list.ng-star-inserted").$$("button.ng-star-inserted").findBy(text(targetGroup)).click();
        $$("portal-plain-button.ng-star-inserted").shouldHave(size(amountServices));
    }

}

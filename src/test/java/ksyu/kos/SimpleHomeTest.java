package ksyu.kos;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
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
    @DisplayName("Проверка наличия топ-5 услуг в блоке поиска на главной странице и доступности")
    @MethodSource
    void findServiceLabelAndNavigateToChat(String serviceLabel, List<String> dialogMenu) {
        $$(".ng-star-inserted .preset__label").findBy(text(serviceLabel)).click();
        $$("portal-search-message-button.ng-star-inserted").
                filterBy(visible).
                shouldHave(texts(dialogMenu));
    }

}

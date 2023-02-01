package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $x("//*[@data-test-id='city']//input").setValue(validUser.getCity());
        $x("//*[@data-test-id='date']//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME, Keys.BACK_SPACE));
        $x("//*[@data-test-id='date']//input").setValue(firstMeetingDate);
        $x("//*[@data-test-id='name']//input").setValue(validUser.getName());
        $x("//*[@data-test-id='phone']//input").setValue(validUser.getPhone());
        $x("//*[@data-test-id='agreement']").click();
        $x("//div[contains (@class, 'grid-row')]//button").click();
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15));

        $x("//*[@data-test-id='date']//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME, Keys.BACK_SPACE));
        $x("//*[@data-test-id='date']//input").setValue(secondMeetingDate);
        $x("//div[contains (@class, 'grid-row')]//button").click();
        $("[data-test-id=replan-notification]").shouldBe(Condition.visible);
        $x("//span[contains(text(), 'Перепланировать')]").click();
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15));
    }
}

package pages;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import commons.annotaitions.Path;
import java.util.Random;

@Path("/uslugi-kompaniyam")
public class UslugiPage extends AbsBasePage {
  private String controlString = "Разработка индивидуальных программ обучения для бизнеса";

  public UslugiPage(Page page) {
    super(page);
  }

  public void checkUslugi() {
    Locator button = page.locator("xpath=//button[contains(text(),\"Подробнее\")]");
    Locator checkbox;
    Page newPage = page.waitForPopup(() -> {
      button.click();
    });
    newPage.waitForLoadState();
    Locator header = newPage.locator("h1");
    String text = header.innerText();
    assert newPage.title().contains(controlString) : String.format("Заголовок страницы не содержит %s", controlString);
    assert text.contains(controlString) : String.format("тег H1 не содержит ожидаемый текст: %s!", controlString);

    Locator linksCourses = newPage.locator("xpath=//div[contains(@class, \"t396__elem\")]/div/a");
    assert linksCourses.count() > 1 : "Направления обучения не отображаются на странице!";

    Random random = new Random();
    int randomNumber = random.nextInt(linksCourses.count());
    Locator link = linksCourses.nth(randomNumber);
    String href = link.getAttribute("href");

    link.click();
    String end = getLastPart(href);
    assertTrue(newPage.url().contains(end), String.format("URL не содержит %s", end));
    newPage.waitForLoadState();

    switch (end) {
      case "programming":
        String prog = "Программирование";
        checkbox = newPage.locator(getCheckboxByLabel(prog));
        assert checkbox.isChecked() : getCourse(prog);
        break;
      case "testing":
        String test = "Тестирование";
        checkbox = newPage.locator(getCheckboxByLabel(test));
        assert checkbox.isChecked() : getCourse(test);
        break;
      case "marketing-business":
        String business = "Управление";
        checkbox = newPage.locator(getCheckboxByLabel(business));
        assert checkbox.isChecked() : getCourse(business);
        break;
      case "analytics":
        String analytic = "Аналитика и анализ";
        checkbox = newPage.locator(getCheckboxByLabel(analytic));
        assert checkbox.isChecked() : getCourse(analytic);
        break;
      case "data-science":
        String data = "Data Science";
        checkbox = newPage.locator(getCheckboxByLabel(data));
        assert checkbox.isChecked() : getCourse(data);
        break;
      case "operations":
        String operation = "Инфраструктура";
        checkbox = newPage.locator(getCheckboxByLabel(operation));
        assert checkbox.isChecked() : getCourse(operation);
    }
    newPage.close();
  }

  private String getCourse(String course) {
    return String.format("Направление обучения %s, не выбрано!", course);
  }
}

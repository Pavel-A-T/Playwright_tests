package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import commons.annotaitions.Path;
import utils.ConfigReader;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Path("/subscription")
public class SubscriptionPage extends AbsBasePage {
  public SubscriptionPage(Page page) {
    super(page);
  }

  public void login() {
    String email = System.getProperty("email", ConfigReader.getProperty("email"));
    String pass = System.getProperty("pass", ConfigReader.getProperty("pass"));
    Locator enter = page.locator("xpath=//button[contains(text(),\"Войти\")]");
    Locator emailInput = page.locator("xpath=//input[@name=\"email\"]");
    Locator passInput = page.locator("xpath=//input[@type=\"password\"]");

    enter.click();
    emailInput.type(email);
    passInput.type(pass);
    Locator newEnter = page.locator("xpath=//div[contains(text(),\"Войти\")]");
    newEnter.click();
  }

  public void checkSubscription() {
    String change = "Текст кнопки не изменился!";
    String sectionWarning = "Дополнительная секция не отображается на странице!";
    String expectedText = "Варианты подписки";
    String toBuy = "Оплатить обучение";
    String h1Text = "Выберите способ оплаты";
    String period = "3 мес";
    List<String> subscriptions = Arrays.asList(new String[]{"Trial", "Standard", "Professional"});
    Locator h2 = page.locator("h2.fQdOXU");
    Locator tile = page.locator("div.bCFgUp");
    String more = "xpath=//button[contains(@class, \"coZrXf\") and contains(text(), \"Подробнее\")]";
    Locator trial = page.locator("#radio-base-trial");
    Locator price = page.locator("xpath=//span/div/div[contains(., \"₽\")]");
    Locator buy = page.locator("//button[contains(@class, \"hCtVHr\") and contains(text(), \"Купить\")]");
    Locator buyButton = page.locator("//button[contains(text(), \"" + toBuy + "\")]");
    Locator label = page.locator("xpath=//label[@for=\"radio-base-3_month\"]");

   assert h2.innerText().trim().equals(expectedText) : String.format("Заголовок \"%s\" отсутствует на странице!", expectedText);
    tile.all().forEach(element -> {
      String head = element.locator("div h4").innerText();
      assert subscriptions.contains(head) : String.format("Подписка \"%s \" отсутствует на странице!", head);
    });

    Locator locator = page.locator(more);
    locator.all().forEach(element -> {
      element.click();
      Locator button = page.locator("xpath=//button[contains(@class, \"coZrXf\") and contains(text(), \"Свернуть\")]");
      Locator div = page.locator("div.fnEuLc");
      assert getHeight(div.getAttribute("style").trim()) > 20 : sectionWarning;
      assert button.innerText().equals("Свернуть"): change;
      button.click();
      Locator second = page.locator("div.lgYWmG");
      second.all().forEach(el->{
        assert getHeight(el.getAttribute("style")) == 0 : "Дополнительные элементы не скрыты со страницы";
      });
    });

    buy.nth(1).click();
    String cost = price.innerText().trim();
    assert buyButton.innerText().equals(toBuy): String.format("Отсутствует кнопка \"%s\"", toBuy);
    assert page.locator("h1").innerText().equals(h1Text): "Отсутствует заголовок оплаты!";
    assert price.isVisible(): "Не отображается сумма оплаты";
    trial.click();
    label.waitFor();
    assert label.innerText().contains(period): "Срок на странице не изменился!";
    assert !price.innerText().trim().equals(cost): "Стоимость курса не изменилась!";
  }

  private Integer getHeight(String text) {
    Pattern pattern = Pattern.compile("\\d+");
    Matcher matcher = pattern.matcher(text);
    if (matcher.find()) {
      return Integer.parseInt(matcher.group());
    }
    return null;
  }
}

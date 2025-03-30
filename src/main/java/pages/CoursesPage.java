package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import commons.annotaitions.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Path("/catalog/courses")
public class CoursesPage extends AbsBasePage {
  public CoursesPage(Page page) {
    super(page);
  }

  public void checkFilter() {
    String architect = "Архитектура";
    String all = "Все направления";
    String every = "Любой уровень";
    String courses = "div.bwGwUO a.sc-zzdkm7-0";
    Locator clear = page.locator("xpath=//button[contains(text(),\"Очистить фильтры\")]");
    Locator allCheckBoxLocator = page.locator(getCheckboxByLabel(all));
    Locator everyCheckBoxLocator = page.locator(getCheckboxByLabel(every));
    Locator slider1 = page.locator("xpath=(//div[@role='slider'])[1]");
    Locator slider2 = page.locator("xpath=(//div[@role='slider'])[2]");
    Locator textDiv = page.locator("xpath=//div[contains(@class,\"cUgsii\")]");

    assert allCheckBoxLocator.isChecked() : "\"Все направления\" - фильтр, не отображается на странице!";
    assert everyCheckBoxLocator.isChecked() : "\"Любой уровень\" - фильтр, не отображается на странице!";

    moveSlider(slider1, textDiv, 2, 3, 1);
    moveSlider(slider2, textDiv, -2, 10, 3);
    page.waitForTimeout(1500);

    Locator tiles = page.locator(courses);
    assert tiles.count() > 0 : "Не отображаются плитки с названиями курсов!";
    tiles.all().forEach(element -> {
      String month = element.locator("div.jIBTjx div div.jEGzDf").innerText();
      int currentMonth = getNumberOfMonths(month);
      assert currentMonth >= 3 && currentMonth <= 10 : String.format("Не работает фильтр продолжительности курсов в месяцах! %s", month);
    });

    List<Locator> before = page.locator(courses).all();
    Locator architectLocator = page.locator(getCheckboxByLabel(architect));
    architectLocator.check();

    assert architectLocator.isChecked() : String.format("Не выбран checkBox %s", architect);
    List<Locator> after = page.locator(courses).all();
    assert before.containsAll(after) : "Состояние плиток не изменилось!";

    clear.click();
    allCheckBoxLocator = page.locator(getCheckboxByLabel(all));
    assert allCheckBoxLocator.isChecked() : "Не сброшены фильтры!";
  }

  private int getNumberOfMonths(String month) {
    Pattern pattern = Pattern.compile("\\s(\\d+)\\s+месяц");
    Matcher matcher = pattern.matcher(month);
    if (matcher.find()) {
      return Integer.parseInt(matcher.group(1));
    } else {
      throw new IllegalArgumentException("Не найдено число месяцев в строке: " + month);
    }
  }

  private int getNumber(String text, int part) {
    String[] words = text.split(" ");
    return Integer.parseInt(words[part]);
  }

  private void moveSlider(Locator slider, Locator textDiv, int step, int target, int index) {
    BoundingBox box = slider.boundingBox();
    int startX = (int) box.x;
    int startY = (int) box.y;

    slider.hover();
    page.mouse().down();
    int i = 0;

    while (!(getNumber(textDiv.innerText(), index) == target)) {
      int newX = startX + (i * step);
      page.mouse().move(newX, startY);
      i++;
    }
    page.mouse().up();
  }
}

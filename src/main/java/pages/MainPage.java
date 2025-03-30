package pages;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import commons.annotaitions.Path;

@Path("/lessons/ml-specialization/")
public class MainPage extends AbsBasePage {
  private String dragAndDropSelector = "div.fxMjmR";
  private String teachers = ".imGcoX";
  private String teacher = ".fxMjmR";
  private Locator next = page.locator("//*[@id=\"__PORTAL__\"]/div/div/div/div/div[2]/button[2]/div/div/div");
  private Locator prev = page.locator("#__PORTAL__ > div > div > div > div > div.sc-1bkbgbz-6.fEFAeG > button.sc-1bkbgbz-2.sc-1bkbgbz-4.dQreKk.biZjNh > div > div > div");
  private Locator close = page.locator("button.iNjapz");

  public MainPage(Page page) {
    super(page);
  }

  public void drugAndDropTeachers() {
    Locator firstSlide = page.locator(dragAndDropSelector).nth(2);
    BoundingBox initialPosition = firstSlide.boundingBox();
    Locator secondSlide = page.locator(dragAndDropSelector).nth(0);
    firstSlide.dragTo(secondSlide);
    BoundingBox newPosition = firstSlide.boundingBox();
    assertTrue(newPosition.x != initialPosition.x);
  }

  public void checkTeachersBlock() {
    Locator block = page.locator(teachers);
    Locator h2 = block.locator("h2");
    assertThat(h2).hasText("Преподаватели");
    Locator allParentBlocks = page.locator(teacher);
    assertTrue(allParentBlocks.count() > 1);

    allParentBlocks.all().forEach(parentBlock -> {
      Locator childBlocks = parentBlock.locator(".fjUCpx");
      assertThat(childBlocks).hasCount(1);

      Locator paragraphs = parentBlock.locator("p");
      assertThat(paragraphs).hasCount(3);
      paragraphs.all().forEach(p -> {
        assertTrue(!p.textContent().isBlank());
      });
    });
  }

  public void checkPopupTeacher() {
    Locator teacherTile = page.locator(teacher).nth(3);
    String teacherName = teacherTile.locator("p").first().innerText();
    teacherTile.click();
    Locator popup = page.locator("div.swiper-slide-active div h3");
    popup.waitFor();
    assertTrue(popup.isVisible(), "Popup не открылся!");
    assertEquals(teacherName.trim(), popup.innerText().trim(), "Имя преподавателя в popup не совпадает!");

    next.click();
    popup = page.locator("div.swiper-slide-active div h3");
    assert !teacherName.trim().equals(popup.innerText().trim()) : "Имена преподавателей совпадают!";
    prev.click();
    popup = page.locator("div.swiper-slide-active div h3");
    assert teacherName.trim().equals(popup.innerText().trim()) : "Имена преподавателей не совпадают!";

    prev.click();
    popup = page.locator("div.swiper-slide-active div h3");
    assert !teacherName.trim().equals(popup.innerText().trim()) : "Имена преподавателей совпадают!";
    next.click();
    close.click();
    page.waitForTimeout(400);
    assertFalse(popup.isVisible(), "Popup не закрылся!");
  }
}

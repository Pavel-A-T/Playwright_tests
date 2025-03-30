package tests;

import com.google.inject.Inject;
import extentions.UIExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.MainPage;


@ExtendWith(UIExtension.class)
public class MainPageTest {
  @Inject
  private MainPage mainPage;

  @Test
  public void test() {
    mainPage.open();
    mainPage.checkTeachersBlock();
    mainPage.drugAndDropTeachers();
    mainPage.checkPopupTeacher();
  }
}

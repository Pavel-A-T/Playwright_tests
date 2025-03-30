package tests;

import com.google.inject.Inject;
import extentions.UIExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.UslugiPage;

@ExtendWith(UIExtension.class)
public class UslugiPageTest {
  @Inject
  UslugiPage uslugiPage;

  @Test
  public void checkUslugiTest() {
    uslugiPage.open();
    uslugiPage.checkUslugi();
  }
}

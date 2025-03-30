package tests;

import com.google.inject.Inject;
import extentions.UIExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.CoursesPage;

@ExtendWith(UIExtension.class)
public class CoursePageTest {
  @Inject
  CoursesPage coursesPage;

  @Test
  public void coursesTest() {
    coursesPage.open();
    coursesPage.checkFilter();
  }
}

package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import commons.annotaitions.Path;
import utils.ConfigReader;

public abstract class AbsBasePage {
  protected Page page;
  private String baseURL = ConfigReader.getProperty("base.url");

  public AbsBasePage(Page page) {
    this.page = page;
  }

  public void open() {
    String url = baseURL + getPath();
    page.navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
  }

  protected String getLastPart(String url) {
    String str = url.replace("\\", "/");
    return str.substring(str.lastIndexOf("/") + 1);
  }

  protected String getCheckboxByLabel(String text) {
    return "//label[text()=\"" + text + "\"]/parent::div//div/input[@type='checkbox']";
  }

  private String getPath() {
    Class<? extends AbsBasePage> clazz = getClass();
    if (clazz.isAnnotationPresent(Path.class)) {
      Path path = clazz.getDeclaredAnnotation(Path.class);
      return path.value().startsWith("/") ? path.value() : "/" + path.value();
    }
    else {
      return "";
    }
  }
}

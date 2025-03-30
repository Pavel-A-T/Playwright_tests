package extentions;

import com.google.inject.Guice;
import com.microsoft.playwright.*;
import modules.GuiceModule;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import java.awt.*;
import java.nio.file.Path;
import java.util.List;

public class UIExtension implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback {
  private Playwright playwright;
  private BrowserContext browserContext;
  private Browser browser;
  private Page page;
  private int width;
  private int height;

  {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.width = (int) screenSize.getWidth();
    this.height = (int) screenSize.getHeight();
  }

  @Override
  public void beforeAll(ExtensionContext extensionContext) {
    playwright = Playwright.create();
    browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
        .setHeadless(false)
        .setArgs(List.of("--disable-extensions",
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--remote-allow-origins=*",
            "--remote-debugging-port=9222",
            "--disable-blink-features=AutomationControlled"))
    );
    browserContext = browser.newContext(new Browser.NewContextOptions()
        .setViewportSize(width, height));
    page = browserContext.newPage();
    browserContext.tracing().start(
        new Tracing.StartOptions()
            .setScreenshots(true)
            .setSnapshots(true)
            .setSources(true)
    );
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    extensionContext.getTestInstance().ifPresent(instance ->
        Guice.createInjector(new GuiceModule(page)).injectMembers(instance));
  }

  @Override
  public void afterAll(ExtensionContext extensionContext) {
    if (browserContext != null) {
      browserContext.tracing().stop(new Tracing.StopOptions().setPath(Path.of(System.getProperty("user.dir") + "/trace.zip")));
      browserContext.close();
    }
    if (browser != null) {
      browser.close();
    }
    if (playwright != null) {
      playwright.close();
    }
  }
}

package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.microsoft.playwright.Page;
import pages.*;

public class GuiceModule extends AbstractModule {
  private Page page;

  public GuiceModule(Page page) {
    this.page = page;
  }

  @Provides
  public Page getPage() {
    return this.page;
  }

  @Singleton
  @Provides
  public MainPage getMainPage() {
    return new MainPage(this.page);
  }

  @Singleton
  @Provides
  public UslugiPage getUslugiPage() {
    return new UslugiPage(this.page);
  }

  @Singleton
  @Provides
  public CoursesPage getCoursesPage() {
    return new CoursesPage(this.page);
  }

  @Singleton
  @Provides
  public SubscriptionPage getSubscriptionPage() {
    return new SubscriptionPage(this.page);
  }
}
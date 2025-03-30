package tests;

import com.google.inject.Inject;
import extentions.UIExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.SubscriptionPage;

@ExtendWith(UIExtension.class)
public class SubscriptionPageTest {
  @Inject
  SubscriptionPage subscriptionPage;

  @Test
  public void checkSubscriptionTest() {
    subscriptionPage.open();
    subscriptionPage.login();
    subscriptionPage.checkSubscription();
  }
}

package pageObject;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import java.awt.*;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Driver_Component {

    protected WebDriver driver;

    public Driver_Component(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        driver.manage().timeouts().implicitlyWait(10, SECONDS);
    }
}

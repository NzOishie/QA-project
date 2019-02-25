package Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;



public class Test1 {
    private WebDriver driver;
    private int count = 0;

    @BeforeMethod
    public void Beforetestcaserun(){

        //Driver setup and implicit wait
        System.setProperty("webdriver.chrome.driver", "E:\\chromedriver_win32\\chromedriver.exe");
        driver=new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(20000, TimeUnit.MILLISECONDS);

}
    @Test
    public void Testcase1() throws InterruptedException {

        // Go to the website
        driver.get("https://www.rentalhomes.com/listing?q=usa&utm_source");


        //WebdriverWait initialization
        WebDriverWait wait = new WebDriverWait(driver, 30);


        //Find if there is a overlay comes or not. If it comes, then close it.
        if(driver.findElements(By.xpath("//div[@class='IM_overlay_close']")).size() != 0){
            driver.findElement(By.xpath("//div[@class='IM_overlay_close']")).click();
        }


        //Click on the calender and select the dates
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("datepicker_start_details_overlay")));
        driver.findElement(By.id("datepicker_start_details_overlay")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[9]/div[1]/table[1]/tbody[1]/tr[4]/td[4]/div[1]")));
        driver.findElement(By.xpath("//div[9]/div[1]/table[1]/tbody[1]/tr[4]/td[4]/div[1]")).click();
        driver.findElement(By.xpath("//div[9]/div[1]/table[1]/tbody[1]/tr[5]/td[5]/div[1]")).click();


        //Click on the next button
        driver.findElement(By.id("start_now")).click();


        // Check if the cookie overlay comes or not. if it comes, click on the 'I accept' button.
        if ( driver.findElement(By.xpath("//button[@class='accept_gdpr']")).getSize() != null){
            driver.findElement(By.xpath("//button[@class='accept_gdpr']")).click();
        }
        Thread.sleep(4000);


        //The pagination loop
        while (true){


            //Find if there is a overlay comes or not. If it comes, then close it.
            if(driver.findElements(By.xpath("//div[@class='IM_overlay_close']")).size() != 0){
                driver.findElement(By.xpath("//div[@class='IM_overlay_close']")).click();
            }


            //Show the page number of current page
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("pagination-active")));
            String page_number_text = driver.findElement(By.className("pagination-active")).getText();
            System.out.println( "Page number: "+ page_number_text);


            //Parse the page number to string for next pagination purpose.
            int page_number = Integer.parseInt(page_number_text);


            //Find the total number of properties and check if it is 48 or not
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("item")));
            int property_count = driver.findElements(By.className("item ")).size();
            if( (property_count - 16) == 48){
                System.out.println("This page has 48 property!");
            }
            else{
                System.out.println("This page don't have 48 property!");
                //If 48 property doesn't exist then it is the last page and the loop should break
                break;
            }


            //take the number of pages from pagination list
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='js_pagination_links']//child::*//a")));
            List<WebElement> pagination =driver.findElements(By.xpath("//ul[@class='js_pagination_links']//child::*//a"));


            //Click on the next page
            if(pagination.size()>0) {
               if( count == 0){
                   count++;
                   pagination.get(page_number).click();
               }
               else{
                   //If page number is not on, the pagination list is increased with 'previous page'. So, addition of 1 in the index is needed.
                   pagination.get(page_number + 1).click();
               }
            }

            Thread.sleep(4000);
        }
    }

    @AfterMethod
    public void browserclose(){
        driver.quit();
    }

}
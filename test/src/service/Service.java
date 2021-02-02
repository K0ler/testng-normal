package service;

import org.testng.Assert;
import org.testng.annotations.Test;

import main.Main;
import pageObject.LoginPage_Component;
import pageObject.MainPage_Component;
import pageObject.ProcessesPage_Component;
import pageParam.pageConfig;
import java.awt.*;
import utils.DeCrypt;


public class Service extends pageConfig {
    @Test
    public void logToPF() throws InterruptedException {

		 //Thread.sleep(Long.parseLong(parameters.getParameter("wait")));
         
         //Assert.assertTrue(Boolean.parseBoolean(parameters.getParameter("assert")));
         
         Main.report.logPass("Process reasumed");
         
         Main.report.logInfo("Pobrany user(null jeśli nie pobrano klucza):" + parameters.getUserLogin());
         System.out.println("Pobrany user(null jeśli nie pobrano klucza):" + parameters.getUserLogin());
         Main.report.logInfo("Pobrany password(null jeśli nie pobrano klucza):" + parameters.getUserPassword());
         System.out.println("Pobrany password(null jeśli nie pobrano klucza):" + parameters.getUserPassword());

         

         DeCrypt login = new DeCrypt();
         
         

         System.out.println("zdekodowany password:" + login.deCrypt("7F094799", "key"));
         
         
         Main.report.logInfo("Pobrany param1:" + parameters.getParameter("param1"));
         System.out.println("Pobrany param1:" + parameters.getParameter("param1"));
         
         Main.report.logInfo("Pobrany param2:" + parameters.getParameter("param2"));
         System.out.println("Pobrany param2:" + parameters.getParameter("param2"));
         
    }
}

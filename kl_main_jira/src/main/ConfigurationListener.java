package main;


import org.testng.ITestResult;
import java.io.IOException;

import org.json.JSONException;
import org.testng.IConfigurationListener2;


public class ConfigurationListener implements IConfigurationListener2 {
	
	
	@Override
	public void onConfigurationSuccess(ITestResult itr) {
		// TODO
	}
	
	
	@Override
	public void onConfigurationFailure(ITestResult itr) {
		try {
			if (Main.driver != null) {
				PfUtils.getWebScreenShot(Main.driver);
				Main.driver.quit();
			}
			Main.report.logTechnicalFail(itr.getThrowable().toString());
			Main.parameters.setResult("failed", "Configuration error : " + itr.getThrowable().toString(), itr.getThrowable().toString(), null, null);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void onConfigurationSkip(ITestResult itr) {
		// TODO
	}
	
	
	@Override
	public void beforeConfiguration(ITestResult tr) {
		// TODO
	}

}
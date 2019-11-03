package test;

import sdp.injector.annotation.Config;
import sdp.injector.annotation.InjectFrom;

@InjectFrom
public class ConfigurationTest {

	@Config
	private String test() {
		System.out.println("creating configuration bean.....");
		return null;
	}
}

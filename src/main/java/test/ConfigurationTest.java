package test;

import sdp.injctr.anntn.Config;
import sdp.injctr.anntn.InjectFrom;

@InjectFrom
public class ConfigurationTest {

	@Config
	public String test() {
		System.out.println("creating configuration bean.....");
		return null;
	}
}

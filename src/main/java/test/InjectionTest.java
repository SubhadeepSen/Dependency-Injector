package test;

import sdp.injctr.anntn.Inject;
import sdp.injctr.anntn.InjectIn;
import sdp.injctr.scnnr.ClasspathScanningException;
import sdp.injctr.scnnr.DependencyInjector;

@InjectIn
public class InjectionTest {

	{
		DependencyInjector.inject(this);
	}

	@Inject
	private ClasspathScanningException exc;

	public void test() {
		System.out.println(exc);
	}
}

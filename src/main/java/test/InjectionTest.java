package test;

import sdp.injector.annotation.Inject;
import sdp.injector.annotation.InjectIn;
import sdp.injector.scanner.ClasspathScanningException;
import sdp.injector.scanner.DependencyInjector;

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

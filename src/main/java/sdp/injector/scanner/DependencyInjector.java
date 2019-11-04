package sdp.injector.scanner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import sdp.injector.annotation.Config;
import sdp.injector.annotation.Inject;
import sdp.injector.annotation.InjectFrom;
import sdp.injector.annotation.InjectIn;
import sdp.injector.util.InjectorLogger;

public class DependencyInjector {

	@SuppressWarnings("rawtypes")
	private static Map<Class, Object> container = new HashMap<>();

	public static void inject(Object obj) {
		container.put(obj.getClass(), obj);
		InjectorLogger.info("[Bean created] : " + obj.getClass().getCanonicalName());
		createInstanceOfAnnotatedClass();
		createInstanceFromAnnotatedMethod();
		injectInstanceToAnnotatedFields();
		return;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void createInstanceOfAnnotatedClass() {
		try {
			List<Class> classesInCurrentPath = new ClasspathScanner().getListOfClasses();
			classesInCurrentPath.forEach(cls -> {
				if (cls.isAnnotationPresent(InjectIn.class) || cls.isAnnotationPresent(InjectFrom.class)) {
					try {
						if (!container.containsKey(cls)) {
							container.put(cls, cls.newInstance());
							InjectorLogger.info("[Bean created] : " + cls.getCanonicalName());
						}
					} catch (InstantiationException | IllegalAccessException e) {
						InjectorLogger.error("Failed to create instance of " + cls.getCanonicalName()
								+ ", make sure your class has public no-arg constructor.");
					}
				}
			});
		} catch (ClasspathScanningException e) {
			InjectorLogger.error(e.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void createInstanceFromAnnotatedMethod() {
		Map<Class, Object> configBeans = new HashMap<>();
		Method[] methods;
		for (Entry<Class, Object> classObjectEntry : container.entrySet()) {
			if (classObjectEntry.getKey().isAnnotationPresent(InjectFrom.class)) {
				methods = classObjectEntry.getKey().getDeclaredMethods();
				for (Method method : methods) {
					if (method.isAnnotationPresent(Config.class)) {
						if (method.getReturnType().getName().equals("void")) {
							InjectorLogger.error("Failed to create configuration bean from declared method in "
									+ classObjectEntry.getKey().getName() + ": invalid return type: " + method.getName()
									+ "() ===> " + method.getReturnType().getName());
						}
						try {
							if (!configBeans.containsKey(method.getReturnType().getClass())) {
								configBeans.put(method.getReturnType().getClass(),
										method.invoke(classObjectEntry.getValue()));
								InjectorLogger.info("[Configuration bean created] : "
										+ method.getReturnType().getClass().getCanonicalName());
							}

						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							if (e instanceof IllegalAccessException) {
								InjectorLogger.error("Failed to create configuration bean from declared method in "
										+ classObjectEntry.getKey().getName() + ": " + method.getName() + "()");
							} else {
								InjectorLogger.error(e.getMessage());
							}
						}
					}
				}
			}
		}

		for (Entry<Class, Object> entry : configBeans.entrySet()) {
			container.put(entry.getKey(), entry.getValue());
		}
		configBeans.clear();
	}

	@SuppressWarnings("rawtypes")
	private static void injectInstanceToAnnotatedFields() {
		Map<Class, Object> fieldBeans = new HashMap<>();
		Field[] declaredFields = null;
		Class fieldClassType = null;
		for (Entry<Class, Object> classObjectEntry : container.entrySet()) {
			declaredFields = classObjectEntry.getKey().getDeclaredFields();
			for (Field field : declaredFields) {
				if (field.isAnnotationPresent(Inject.class)) {
					fieldClassType = field.getType();
					try {
						field.setAccessible(true);
						if (container.containsKey(fieldClassType)) {
							field.set(container.get(classObjectEntry.getKey()), container.get(fieldClassType));
							InjectorLogger.info("[Bean injected] : " + fieldClassType.getCanonicalName() + " >> "
									+ container.get(classObjectEntry.getKey()).getClass().getCanonicalName());
						} else {
							fieldBeans.put(fieldClassType, fieldClassType.newInstance());
							InjectorLogger.info("[Bean created] : " + fieldClassType.getCanonicalName());
							field.set(container.get(classObjectEntry.getKey()), fieldBeans.get(fieldClassType));
							InjectorLogger.info("[Bean injected] : " + fieldClassType.getCanonicalName() + " >> "
									+ container.get(classObjectEntry.getKey()).getClass().getCanonicalName());
						}
						field.setAccessible(false);
					} catch (InstantiationException | IllegalAccessException e) {
						InjectorLogger.error("Failed to create instance of " + fieldClassType.getCanonicalName()
								+ ", make sure your class has public no-arg constructor.");
					}
				}
			}
		}
		for (Entry<Class, Object> entry : fieldBeans.entrySet()) {
			container.put(entry.getKey(), entry.getValue());
		}
		fieldBeans.clear();
	}
}

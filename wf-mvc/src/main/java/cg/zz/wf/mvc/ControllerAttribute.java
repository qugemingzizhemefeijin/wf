package cg.zz.wf.mvc;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface ControllerAttribute {

	Set<Annotation> getAnnotations();

	Class<?> getControllerClass();

}

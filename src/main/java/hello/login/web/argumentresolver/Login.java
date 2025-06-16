package hello.login.web.argumentresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Target: 이 어노테이션이 적용될 수 있는 위치를 지정합니다.
 * - ElementType.PARAMETER: 메소드의 파라미터에 적용됩니다.
 * @Retention: 이 어노테이션이 언제까지 유지될지를 지정합니다.
 * - RetentionPolicy.RUNTIME: 런타임 동안 유지되며, 리플렉션을 통해 접근할 수 있습니다.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
}

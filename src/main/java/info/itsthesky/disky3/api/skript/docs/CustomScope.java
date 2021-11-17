package info.itsthesky.disky3.api.skript.docs;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomScope {

    String[] value();

}

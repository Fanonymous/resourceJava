import java.lang.annotation.*;

/**
 * 空指针验证类
 *
 * @author zhy
 * @date 2017/2/22
 */
@Documented
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsEmptyAnnotation {
    public boolean isEmpty() default true;

    public String message() default "字段不能为空！";

}
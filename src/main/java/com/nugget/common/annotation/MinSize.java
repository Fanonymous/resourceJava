import java.lang.annotation.*;

/**
 * 最小长度验证类
 *
 * @author zhy
 * @date 2017/2/22
 */
@Documented
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MinSize {
    public int min() default 0;

    public String message() default "长度太短";
}

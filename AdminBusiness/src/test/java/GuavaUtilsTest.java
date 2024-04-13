import com.google.common.base.MoreObjects;
import org.junit.jupiter.api.Test;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/13
 */
public class GuavaUtilsTest {
    private Integer testIntegerField = 123;
    private String testStringField = "test";
    private Boolean testBooleanField = true;

    @Test
    public void test() {
        System.out.println(this);
    }


    @Override
    public String toString() {
        return "GuavaUtilsTest{" +
                "testIntegerField=" + testIntegerField +
                ", testStringField='" + testStringField + '\'' +
                ", testBooleanField=" + testBooleanField +
                '}';
    }
}

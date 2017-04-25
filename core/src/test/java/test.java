import com.dodoca.datamagic.utils.ConfigUtils;
import com.dodoca.datamagic.core.DataMagicUtil;
import org.junit.Test;

/**
 * Created by admin on 2017/3/14.
 */
public class test {
    @Test
    public void test() throws Exception {
        ConfigUtils.load("D:\\shence_project\\data-magic\\common\\src\\main\\resources\\datamagic.properties");

        System.err.print(DataMagicUtil.getDashboardById("93").getData());

    }
}

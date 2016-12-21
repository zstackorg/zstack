import org.junit.Test
import org.zstack.utils.GroovyUtils

/**
 * Created by xing5 on 2016/12/21.
 */
class TestG {
    @Test
    void test() {
        def generator = GroovyUtils.newInstance("RestDocumentationGenerator1.class", "/root/zstack")
        generator.generate()

        //RestDocumentationGenerator1 r1 = new RestDocumentationGenerator1("/root/zstack")
        //r1.generate()
    }
}

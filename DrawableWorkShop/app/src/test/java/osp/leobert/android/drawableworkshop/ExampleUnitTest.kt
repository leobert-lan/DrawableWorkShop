package osp.leobert.android.drawableworkshop

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        println(randomString(-229985452) + " " + randomString(-147909649))
    }

    fun randomString(seed: Long): String {
        val ran = Random(seed)
        val sb = StringBuilder()
        while (true) {
            val k = ran.nextInt(27);
            if (k == 0) {
                break
            }
            sb.append(('`' + k))
        }
        return sb.toString()
    }
}
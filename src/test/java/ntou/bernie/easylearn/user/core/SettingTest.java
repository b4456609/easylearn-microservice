package ntou.bernie.easylearn.user.core;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by bernie on 2016/3/19.
 */
public class SettingTest {

    @Test
    public void testIsConflict() throws Exception {
        Setting setting = new Setting(true, true, 40, true, 55L);
        assertTrue(setting.isConflict(41));
        assertFalse(setting.isConflict(40));
        assertTrue(setting.isConflict(39));
        assertTrue(setting.isConflict(42));
    }
}
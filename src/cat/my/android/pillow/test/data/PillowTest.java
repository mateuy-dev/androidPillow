package cat.my.android.pillow.test.data;

import android.test.InstrumentationTestCase;
import cat.my.android.pillow.Pillow;

public class PillowTest  extends InstrumentationTestCase{
	public void testPillowCreation(){
		Pillow pillow = Pillow.getInstance(getInstrumentation().getContext());
		assertNotNull(pillow);
	}
}

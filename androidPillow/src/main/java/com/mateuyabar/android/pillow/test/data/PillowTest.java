package com.mateuyabar.android.pillow.test.data;

import android.test.InstrumentationTestCase;
import com.mateuyabar.android.pillow.Pillow;

public class PillowTest  extends InstrumentationTestCase{
	public void testPillowCreation(){
		Pillow pillow = Pillow.getInstance(getInstrumentation().getContext());
		assertNotNull(pillow);
	}
}

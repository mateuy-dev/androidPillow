package cat.my.android.restvolley.utils;

import cat.my.android.restvolley.IdentificableModel;
import android.os.Bundle;

public class BundleUtils {
	public static final String ID_ATTRIBUTE ="id"; 
	
	public static Bundle createIdBundle(IdentificableModel model){
		Bundle bunlde = new Bundle();
		bunlde.putString(ID_ATTRIBUTE, model.getId());
		return bunlde;
	}
	
}

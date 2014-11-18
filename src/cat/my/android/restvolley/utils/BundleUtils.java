package cat.my.android.restvolley.utils;

import cat.my.android.restvolley.IdentificableModel;
import android.os.Bundle;

public class BundleUtils {
	public static final String ID_ATTRIBUTE ="id"; 
	public static final String CLASS_ATTRTRIBUTE = "class";
	
	public static Bundle createIdBundle(Bundle bundle){
		return createIdBundle(getId(bundle), getModelClass(bundle));
	}
	
	public static Bundle createIdBundle(IdentificableModel model){
		return createIdBundle(model.getId(), model.getClass());
	}
	
	public static Bundle createIdBundle(Class<?> clazz){
		return createIdBundle(null, clazz);
	}
	
	public static Bundle createIdBundle(String id, Class<?> clazz){
		Bundle bunlde = new Bundle();
		if(id!=null)
			bunlde.putString(ID_ATTRIBUTE, id);
		bunlde.putSerializable(CLASS_ATTRTRIBUTE, clazz);
		return bunlde;
	}
	
	public static String getId(Bundle bundle){
		return bundle.getString(ID_ATTRIBUTE);
	}
	
	public static <T> Class<T> getModelClass(Bundle bundle){
		return (Class<T> ) bundle.getSerializable(BundleUtils.CLASS_ATTRTRIBUTE);
	}
	
}

package cat.my.android.pillow.util;

import android.os.Bundle;
import cat.my.android.pillow.IdentificableModel;

import com.google.gson.Gson;

/**
 * Functions to store and retrive a model to/from a bundle.
 * We can use createIdBundle to refer to a model in the database
 * or createModelBundle to send directly a model
 *
 */
public class BundleUtils {
	public static final String ID_ATTRIBUTE ="id"; 
	public static final String CLASS_ATTRTRIBUTE = "class";
	public static final String MODEL_ATTRTRIBUTE = "model";
	public static final String ATTS_ATTRTRIBUTE = "atts";
	
	public static Bundle copyBundle(Bundle toCopy){
		Bundle result = new Bundle();
		String id = getId(toCopy);
		Class<?> clazz = getModelClass(toCopy);
		String modelJson = getStringModel(toCopy);
		String[] atts = getShownAtts(toCopy);
		if(id!=null)
			setId(result, id);
		if(clazz!=null)
			setClass(result, clazz);
		if(modelJson!=null)
			setModel(result, modelJson);
		if(atts != null){
			setShownAtts(result, atts);
		}
		return result;
	}
	
	public static Bundle createIdBundle(IdentificableModel model){
		return createIdBundle(model.getId(), model.getClass());
	}
	
	public static Bundle createIdBundle(Class<?> clazz){
		return createIdBundle(null, clazz);
	}
	
	public static Bundle createIdBundle(String id, Class<?> clazz){
		Bundle bundle = new Bundle();
		if(id!=null)
			setId(bundle, id);
		setClass(bundle, clazz);
		return bundle;
	}
	
	private static void setId(Bundle bundle, String id){
		bundle.putString(ID_ATTRIBUTE, id);
	}
	private static void setClass(Bundle bundle, Class<?> clazz){
		bundle.putSerializable(CLASS_ATTRTRIBUTE, clazz);
	}
	private static void setModel(Bundle bundle, IdentificableModel model){
		String modelJson = new Gson().toJson(model);
		setModel(bundle, modelJson);
	}
	private static void setModel(Bundle bundle, String modelJson){
		bundle.putString(MODEL_ATTRTRIBUTE, modelJson);
	}
	public static void setShownAtts(Bundle bundle, String[] atts){
		bundle.putStringArray(ATTS_ATTRTRIBUTE, atts);
	}
	
	public static String getId(Bundle bundle){
		return bundle.getString(ID_ATTRIBUTE, null);
	}
	
	public static boolean containsId(Bundle bundle){
		return bundle.containsKey(ID_ATTRIBUTE);
	}
	
	public static <T> Class<T> getModelClass(Bundle bundle){
		return (Class<T> ) bundle.getSerializable(BundleUtils.CLASS_ATTRTRIBUTE);
	}
	
	public static String[] getShownAtts(Bundle bundle){
		return bundle.getStringArray(ATTS_ATTRTRIBUTE);
	}
	
	/**
	 * Sets the whole model to the bundle.
	 * Use in case that the model has not been stored to the database.
	 * 
	 * @param model
	 * @return
	 */
	public static Bundle createModelBundle(IdentificableModel model){
		Bundle bundle = new Bundle();
		setClass(bundle, model.getClass());
		setModel(bundle, model);
		return bundle;
	}
	
	private static String getStringModel(Bundle bundle){
		String modelJson = bundle.getString(MODEL_ATTRTRIBUTE);
		return modelJson;
	}
	
	public static <T> T getModel(Bundle bundle){
		String modelJson = getStringModel(bundle);
		T result = (T) new Gson().fromJson(modelJson, getModelClass(bundle));
		return result;
	}
	
	

	public static void setEnum(Bundle bundle, String key, Enum<?> value){
		//TODO New type of bundle utils. Need to sort Bundle utils. 
		bundle.putSerializable(key, value);
	}
	
	public static <T> T getEnum(Bundle bundle, String key){
		return (T) bundle.getSerializable(key);
	}
}

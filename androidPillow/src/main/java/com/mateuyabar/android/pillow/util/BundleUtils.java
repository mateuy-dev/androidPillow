package com.mateuyabar.android.pillow.util;

import android.os.Bundle;
import com.mateuyabar.android.pillow.IdentificableModel;

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
	public static final String HIDE_BUTTONS_ATTRIBUTE = "hideButtonsAttribute";
	
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
		//TODO change name at least!
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
		if(bundle.containsKey(ID_ATTRIBUTE))
			return bundle.getString(ID_ATTRIBUTE);
		else
			return null;
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
		if(!bundle.containsKey(MODEL_ATTRTRIBUTE))
			return null;
		String modelJson = getStringModel(bundle);
		T result = (T) new Gson().fromJson(modelJson, getModelClass(bundle));
		return result;
	}
	
	public static void setHideButtons(Bundle bundle){
		bundle.putBoolean(HIDE_BUTTONS_ATTRIBUTE, true);
	}
	
	public static boolean getHideButtons(Bundle bundle){
		return bundle.containsKey(HIDE_BUTTONS_ATTRIBUTE) && bundle.getBoolean(HIDE_BUTTONS_ATTRIBUTE);
	}

	public static void setEnum(Bundle bundle, String key, Enum<?> value){
		//TODO New type of bundle utils. Need to sort Bundle utils. 
		bundle.putSerializable(key, value);
	}
	
	public static <T> T getEnum(Bundle bundle, String key){
		return (T) bundle.getSerializable(key);
	}
	
	
//	public static class BundleModel<T> implements Parcelable{
//		Gson gson = new Gson();
//		T model;
//		Class<T> modelClass;
//		
//		public BundleModel(T model){
//			this.model = model;
//			this.modelClass = (Class<T>) model.getClass();
//		}
//		
//		public BundleModel(Parcel parcel){
//			this.modelClass = (Class<T>) parcel.readSerializable();
//			this.model = modelFromString(parcel.readString());
//		}
//
//		public static final Parcelable.Creator<BundleModel> CREATOR = new Parcelable.Creator<BundleModel>() {
//			@Override
//			public BundleModel createFromParcel(Parcel parcel) {
//				return new BundleModel(parcel);
//			}
//
//			@Override
//			public BundleModel[] newArray(int size) {
//				return new BundleModel[size];
//			}
//		};
//
//		@Override
//		public int describeContents() {
//			return 0;
//		}
//
//		@Override
//		public void writeToParcel(Parcel dest, int flags) {
//			dest.writeSerializable(modelClass);
//			dest.writeString(modelToString());
//		}
//		
//		private String modelToString(){
//			return gson.toJson(model);
//		}
//		
//		private T modelFromString(String readString) {
//			return gson.fromJson(readString, modelClass);
//		}
//		
//	}
	
}

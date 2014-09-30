package cat.my.lib.restvolley.pathbuilders;



import java.lang.reflect.Type;

import com.google.gson.Gson;

public interface IRestMap<T> {

	public Route getCollectionRoute(int method, String operation);
	
	public Route getMemberRoute(T model, int method, String operation);
	
	public Route getIndexPath();

	public Route getCreatePath(T model);

	public Route getShowPath(T model);

	public Route getUpdatePath(T model);

	public Route getDestroyPath(T model);
	
	public Gson getSerializer();

	public String getModelName();
	
	public Class<T> getModelClass();
	
	public Type getCollectionType();

}
package cat.my.lib.restvolley.pathbuilders;

import cat.my.lib.restvolley.models.IdentificableModel;

import com.google.gson.Gson;

public interface IPathBuilder {

	public Route getCollectionRoute(Class<?> clazz, int method, String operation);
	
	public Route getMemberRoute(IdentificableModel model, int method, String operation);
	
	public Route getIndexPath(Class<?> clazz);

	public Route getCreatePath(IdentificableModel model);

	public Route getShowPath(IdentificableModel model);

	public Route getUpdatePath(IdentificableModel model);

	public Route getDestroyPath(IdentificableModel model);
	
	public Gson getSerializer();
	
	public String getModelName(Class<?> clazz);

}
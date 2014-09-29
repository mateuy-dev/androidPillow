package cat.my.lib.orm;


import cat.my.lib.restvolley.models.IdentificableModel;

public interface IDBModelControllerManager {
	public <T extends IdentificableModel> DBModelController<T> getDBModelController(Class<T> clazz);
}

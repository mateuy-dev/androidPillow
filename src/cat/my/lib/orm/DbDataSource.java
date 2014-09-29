package cat.my.lib.orm;

import java.lang.reflect.Type;
import java.util.Collection;

import android.database.sqlite.SQLiteOpenHelper;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import cat.my.lib.mydata.IDataSource;
import cat.my.lib.restvolley.models.IdentificableModel;

public class DbDataSource implements IDataSource{
	IMapperController mapperController;
	SQLiteOpenHelper dbHelper;
	IDBModelControllerManager dbModelControllerManager;

	public DbDataSource(IMapperController mapperController, SQLiteOpenHelper dbHelper) {
		super();
		//TODO check dbModelControllerManager -null delete entitites
		
		this.mapperController = mapperController;
		this.dbHelper = dbHelper;
		this.dbModelControllerManager = new DBModelControllerManager(dbHelper, mapperController, null);
	}

	@Override
	public <T extends IdentificableModel> void index(Class<T> clazz, Type collectionType,
			Listener<Collection<T>> listener, ErrorListener errorListener) {
		DBModelController<T> db = getDbModelController(clazz);
		listener.onResponse(db.getAll());
	}

	@Override
	public <T extends IdentificableModel> void show(Class<T> clazz, T model, Listener<T> listener,
			ErrorListener errorListener) {
		DBModelController<T> db =getDbModelController(clazz);
		T result = db.get(model.getId());
		listener.onResponse(result);
	}

	@Override
	public <T extends IdentificableModel> void create(Class<T> clazz, T model, Listener<T> listener,
			ErrorListener errorListener) {
		DBModelController<T> db = getDbModelController(clazz);
		db.create(model);
		model = db.get(model.getId());
		listener.onResponse(model);
	}

	@Override
	public <T extends IdentificableModel> void update(Class<T> clazz, T model, Listener<T> listener,
			ErrorListener errorListener) {
		DBModelController<T> db =getDbModelController(clazz);
		db.update(model);
		//refresh model
		model = db.get(model.getId());
		listener.onResponse(model);
	}

	@Override
	public <T extends IdentificableModel> void destroy(Class<T> clazz, T model, Listener<Void> listener,
			ErrorListener errorListener) {
		DBModelController<T> db =getDbModelController(clazz);
		db.delete(model);
		listener.onResponse(null);
	}
	
	private <T extends IdentificableModel> DBModelController<T> getDbModelController(Class<T> clazz){
		return dbModelControllerManager.getDBModelController(clazz);
	}

}

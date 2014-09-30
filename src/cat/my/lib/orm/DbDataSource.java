package cat.my.lib.orm;

import java.util.Collection;

import android.database.sqlite.SQLiteOpenHelper;
import cat.my.lib.mydata.DeletedEntries;
import cat.my.lib.mydata.IDataSource;
import cat.my.lib.restvolley.models.IdentificableModel;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public class DbDataSource<T extends IdentificableModel> implements IDataSource<T>{
	
	SQLiteOpenHelper dbHelper;
	IDBModelControllerManager dbModelControllerManager;
	IDBModelFunctions<T> funcs;
	DBModelController<T> dbModelController;

	public DbDataSource(IDBModelFunctions<T> funcs, SQLiteOpenHelper dbHelper, DeletedEntries<T> deletedEntries) {
		super();
		//TODO check dbModelControllerManager -null delete entitites
		this.funcs=funcs;
		this.dbHelper = dbHelper;
		dbModelController = new DBModelController<T>(dbHelper, funcs, deletedEntries);
	}

	@Override
	public void index(Listener<Collection<T>> listener, ErrorListener errorListener) {
		DBModelController<T> db = getDbModelController();
		listener.onResponse(db.getAll());
	}

	@Override
	public void show(T model, Listener<T> listener,
			ErrorListener errorListener) {
		DBModelController<T> db =getDbModelController();
		T result = db.get(model.getId());
		listener.onResponse(result);
	}

	@Override
	public void create(T model, Listener<T> listener, ErrorListener errorListener) {
		DBModelController<T> db = getDbModelController();
		db.create(model);
		model = db.get(model.getId());
		listener.onResponse(model);
	}

	@Override
	public void update(T model, Listener<T> listener, ErrorListener errorListener) {
		DBModelController<T> db =getDbModelController();
		db.update(model);
		//refresh model
		model = db.get(model.getId());
		listener.onResponse(model);
	}

	@Override
	public void destroy(T model, Listener<Void> listener,
			ErrorListener errorListener) {
		DBModelController<T> db =getDbModelController();
		db.delete(model);
		listener.onResponse(null);
	}
	
	private DBModelController<T> getDbModelController(){
		return dbModelController;
	}

}

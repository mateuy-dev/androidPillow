package cat.my.android.restvolley.db;

import java.util.Collection;

import android.database.sqlite.SQLiteOpenHelper;
import cat.my.android.restvolley.IDataSource;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.Listeners.CollectionListener;
import cat.my.android.restvolley.Listeners.ErrorListener;
import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.sync.DeletedEntries;


public class DbDataSource<T extends IdentificableModel> implements IDataSource<T>{
	
	SQLiteOpenHelper dbHelper;
	IDbMapping<T> funcs;
	DBModelController<T> dbModelController;

	public DbDataSource(IDbMapping<T> funcs, SQLiteOpenHelper dbHelper, DeletedEntries<T> deletedEntries) {
		super();
		//TODO check dbModelControllerManager -null delete entitites
		this.funcs=funcs;
		this.dbHelper = dbHelper;
		dbModelController = new DBModelController<T>(dbHelper, funcs, deletedEntries);
	}

	@Override
	public void index(CollectionListener<T> listener, ErrorListener errorListener) {
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
	
	public DBModelController<T> getDbModelController(){
		return dbModelController;
	}

}

package cat.my.android.restvolley.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import cat.my.android.restvolley.IDataSource;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.Listeners.ErrorListener;
import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.listeners.EventDispatcher;
import cat.my.android.restvolley.listeners.IModelUpdatedListener;
import cat.my.android.restvolley.sync.DeletedEntries;


public class DbDataSource<T extends IdentificableModel> implements IDBDataSource<T>{
	EventDispatcher<T> eventDispatcher = new EventDispatcher<T>();
	SQLiteOpenHelper dbHelper;
	IDbMapping<T> funcs;
	DBModelController<T> dbModelController;
	Context context;

	public DbDataSource(Context context, IDbMapping<T> funcs, SQLiteOpenHelper dbHelper, DeletedEntries<T> deletedEntries) {
		super();
		//TODO check dbModelControllerManager -null delete entitites
		this.funcs=funcs;
		this.dbHelper = dbHelper;
		this.context = context;
		dbModelController = new DBModelController<T>(dbHelper, funcs, deletedEntries);
		
	}

	@Override
	public void index(Listener<Collection<T>> listener, ErrorListener errorListener) {
		DBModelController<T> db = getDbModelController();
		listener.onResponse(db.index());
	}
	
	@Override
	public void index(String selection, String[] selectionArgs, String order, Listener<Collection<T>> listener, ErrorListener errorListener) {
		DBModelController<T> db = getDbModelController();
		listener.onResponse(db.index(selection, selectionArgs, order));
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
		eventDispatcher.notifyModelUpdate(model);
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

	public EventDispatcher<T> getEventDispatcher() {
		return eventDispatcher;
	}

	public Context getContext() {
		return context;
	}

}

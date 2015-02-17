package cat.my.android.pillow.data.db;

import java.util.Collection;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.PillowError;
import cat.my.android.pillow.Listeners.ErrorListener;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.data.sync.DeletedEntries;


public class DbDataSource<T extends IdentificableModel> implements IDBDataSource<T>{
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
	public void index(T model, Listener<Collection<T>> listener, ErrorListener errorListener) {
		DBModelController<T> db = getDbModelController();
		listener.onResponse(db.index(model));
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
		try{
			DBModelController<T> db = getDbModelController();
			db.create(model);
			model = db.get(model.getId());
			listener.onResponse(model);
		} catch (SQLiteException exception){
			errorListener.onErrorResponse(new PillowError(exception));
		}
	}

	@Override
	public void update(T model, Listener<T> listener, ErrorListener errorListener) {
		try{
			DBModelController<T> db =getDbModelController();
			db.update(model);
			//refresh model
			model = db.get(model.getId());
			listener.onResponse(model);
		} catch (SQLiteException exception){
			errorListener.onErrorResponse(new PillowError(exception));
		}
	}

	@Override
	public void destroy(T model, Listener<Void> listener,
			ErrorListener errorListener) {
		try{
			DBModelController<T> db =getDbModelController();
			db.delete(model);
			listener.onResponse(null);
		} catch (SQLiteException exception){
			errorListener.onErrorResponse(new PillowError(exception));
		}
	}
	
	@Override
	public void count(String selection, String[] selectionArgs, Listener<Integer> listener,
			ErrorListener errorListener) {
		DBModelController<T> db =getDbModelController();
		int result = db.getCount(selection, selectionArgs);
		listener.onResponse(result);
	}
	
	public DBModelController<T> getDbModelController(){
		return dbModelController;
	}

	public Context getContext() {
		return context;
	}

	

}

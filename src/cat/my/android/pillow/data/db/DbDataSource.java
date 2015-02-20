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
import cat.my.android.pillow.data.core.IPillowResult;
import cat.my.android.pillow.data.core.PillowResult;
import cat.my.android.pillow.data.sync.DeletedEntries;


public class DbDataSource<T extends IdentificableModel> implements IDBDataSourceForSynch<T>{
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
	public IPillowResult<Collection<T>> index() {
		DBModelController<T> db = getDbModelController();
		return new PillowResult<Collection<T>>(context, db.index());
	}
	
	@Override
	public IPillowResult<Collection<T>> index(T model) {
		DBModelController<T> db = getDbModelController();
		return new PillowResult<Collection<T>>(context, db.index(model));
	}
	
	@Override
	public IPillowResult<Collection<T>> index(String selection, String[] selectionArgs, String order) {
		DBModelController<T> db = getDbModelController();
		return new PillowResult<Collection<T>>(context, db.index(selection, selectionArgs, order));
	}

	@Override
	public IPillowResult<T> show(T model) {
		DBModelController<T> db =getDbModelController();
		T result = db.get(model.getId());
		return new PillowResult<T>(context, result);
	}

	@Override
	public IPillowResult<T> create(T model) {
		try{
			DBModelController<T> db = getDbModelController();
			db.create(model);
			model = db.get(model.getId());
			return new PillowResult<T>(context, model);
		} catch (SQLiteException exception){
			return new PillowResult<T>(context, new PillowError(exception));
		}
	}

	@Override
	public PillowResult<T> update(T model) {
		try{
			DBModelController<T> dbController =getDbModelController();
			dbController.update(model);
			//refresh model
			model = dbController.get(model.getId());
			return new PillowResult<T>(context, model);
		} catch (SQLiteException exception){
			return new PillowResult<T>(context, new PillowError(exception));
		}
	}

	@Override
	public PillowResult<Void> destroy(T model) {
		try{
			DBModelController<T> db =getDbModelController();
			db.delete(model);
			return PillowResult.newVoidResult(context);
		} catch (SQLiteException exception){
			return new PillowResult<Void>(context, new PillowError(exception));
		}
	}
	
	@Override
	public PillowResult<Integer> count(String selection, String[] selectionArgs) {
		DBModelController<T> db =getDbModelController();
		int result = db.getCount(selection, selectionArgs);
		return new PillowResult<Integer>(context, result);
	}
	
	@Override
	public IPillowResult<T> setAsNotDirty(T model){
		DBModelController<T> db =getDbModelController();
		db.markAsClean(model);
		model = db.get(model.getId());
		return new PillowResult<T>(context, model);
	}
	
	public DBModelController<T> getDbModelController(){
		return dbModelController;
	}

	public Context getContext() {
		return context;
	}

	

}

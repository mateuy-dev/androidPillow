package cat.my.android.pillow.data.sync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.Listeners.Listener;
import cat.my.android.pillow.data.core.IPillowResult;
import cat.my.android.pillow.data.core.MultiTaskVoidResult;
import cat.my.android.pillow.data.core.PillowResultListener;
import cat.my.android.pillow.data.db.DBUtil;
import cat.my.android.pillow.data.rest.RestDataSource;
import cat.my.android.util.CursorUtil;
import cat.my.util.exceptions.BreakFastException;



/**
 * Keeps track of entries deleted on the database but no yet in the server.
 *
 */
public class DeletedEntries<T extends IdentificableModel> {
	//TODO this class may be reorganized!!
	public static final String TABLE = "deleted_instances";
	public static final String ID_COLUMN = "id";
	public static final String CLASS_COLUMN = "class";
	
	
	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE
			+ " (" + ID_COLUMN + DBUtil.STRING_TYPE+ DBUtil.COMMA_SEP +
					CLASS_COLUMN + DBUtil.STRING_TYPE +
			");";

	SQLiteOpenHelper dbHelper;
	RestDataSource<T> dataSource;
	Context context;

	public static final String WHERE_ID_SELECTION = ID_COLUMN + " == ?";

	public DeletedEntries(Context context, RestDataSource<T> dataSource) {
		this.dbHelper = Pillow.getInstance(context).getDbHelper();
		this.dataSource = dataSource;
		this.context= context;
	}

	public <T extends IdentificableModel> void setToDelete(SQLiteDatabase db, T model) {
		ContentValues values = new ContentValues();
		values.put(ID_COLUMN, model.getId());
		values.put(CLASS_COLUMN, model.getClass().getName());
		db.insert(TABLE, null, values);
		
	}
//	
//	public <T extends IdentificableModel> void setToDelete(T model) {
//		SQLiteDatabase db = dbHelper.getWritableDatabase();
//		setToDelete(db, model);
//		db.close();
//	}

	public void setAsDeleted(String id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		setAsDeleted(db, id);
		db.close();
	}
	
	public IPillowResult<Void> setAllreadyDeleted(T model){
		final String id = model.getId();
		final PillowResultListener<Void> result = new PillowResultListener<Void>(context);
		Listener<Void> onServerDeletedListener = new Listener<Void>(){
			@Override
			public void onResponse(Void response) {
				setAsDeleted(id);
				result.setResult(null);
			}
		};
		dataSource.destroy(model).setListeners(onServerDeletedListener, result);
		
		return result;
	}
	
	public void setAsDeleted(SQLiteDatabase db, String id) {
		String[] selectionArgs = { id };
		db.delete(TABLE, WHERE_ID_SELECTION, selectionArgs);
	}
	
	public IPillowResult<Void> synchronize(){
		MultiTaskVoidResult result = new MultiTaskVoidResult(context);
		Cursor cursor = getCursor();
		while(cursor.moveToNext()){
			String id = CursorUtil.getString(cursor, ID_COLUMN);
			//Not needed, the cursor only selects current classes.
			//String className = CursorUtil.getString(cursor, CLASS_COLUMN);
			try {
				Class<T> clazz = getModelClass();
				T model = clazz.newInstance();
				model.setId(id);
				IPillowResult<Void> subOperation = setAllreadyDeleted(model);
				result.addOperation(subOperation);
			} catch (Exception e) {
				throw new BreakFastException(e);
			}
		}
		result.setLastOperationAdded();
		return result;
	}
	
	
	
	private Cursor getCursor(){
		//TODO order may be important!!!
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String[] projection = {ID_COLUMN, CLASS_COLUMN};
		String selection = CLASS_COLUMN +" == ?";
		String[] values = {getModelClass().getName()};
		Cursor cursor = db.query(TABLE,	projection, selection, values, null, null,  null);
		return cursor;
	}
	
	public Class<T> getModelClass(){
		return dataSource.getRestMapping().getModelClass();
	}

	public boolean isDeleted(String id) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String[] projection = {ID_COLUMN, CLASS_COLUMN};
		String[] selectionArgs = { id };
		Cursor cursor = db.query(TABLE,	projection, WHERE_ID_SELECTION, selectionArgs, null, null,  null);
		return cursor.moveToNext();
	}

}

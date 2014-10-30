package cat.my.android.restvolley.sync;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.db.DBUtil;
import cat.my.android.restvolley.rest.RestDataSource;
import cat.my.android.util.CursorUtil;



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
	RestDataSource<T> restVolley;

	public static final String WHERE_ID_SELECTION = ID_COLUMN + " == ?";

	public DeletedEntries(RestDataSource<T> restVolley, SQLiteOpenHelper dbHelper) {
		this.dbHelper = dbHelper;
		this.restVolley = restVolley;
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
	
	public void setAllreadyDeleted(T model){
		restVolley.destroy(model, new OnServerDeletedListener(model.getId()), CommonListeners.dummyErrorListener);
	}
	
	public void setAsDeleted(SQLiteDatabase db, String id) {
		String[] selectionArgs = { id };
		db.delete(TABLE, WHERE_ID_SELECTION, selectionArgs);
	}
	
	public void synchronize(){
		Cursor cursor = getCursor();
		while(cursor.moveToNext()){
			String id = CursorUtil.getString(cursor, ID_COLUMN);
			//Not needed, the cursor only selects current classes.
			//String className = CursorUtil.getString(cursor, CLASS_COLUMN);
			try {
				Class<T> clazz = getModelClass();
				T model = clazz.newInstance();
				model.setId(id);
				setAllreadyDeleted(model);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				//TODO this means that the model does not have a default constructor!
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public class OnServerDeletedListener implements Listener<Void>{
		String id;
		
		public OnServerDeletedListener(String id) {
			super();
			this.id = id;
		}

		@Override
		public void onResponse(Void response) {
			setAsDeleted(id);
		}
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
		return restVolley.getRestMapping().getModelClass();
	}

	public boolean isDeleted(String id) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String[] projection = {ID_COLUMN, CLASS_COLUMN};
		String[] selectionArgs = { id };
		Cursor cursor = db.query(TABLE,	projection, WHERE_ID_SELECTION, selectionArgs, null, null,  null);
		return cursor.moveToNext();
	}

}

package cat.my.lib.mydata;

import java.util.List;

import com.android.volley.Response.Listener;

import cat.my.lib.android.CursorUtil;
import cat.my.lib.orm.DBModelController;
import cat.my.lib.orm.DBUtil;
import cat.my.lib.restvolley.RestVolleyDataSource;
import cat.my.lib.restvolley.models.IdentificableModel;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Keeps track of entries deleted on the database but no yet in the server.
 *
 */
public class DeletedEntries {
	//TODO this class may be reorganized!!
	public static final String TABLE = "deleted_instances";
	public static final String ID_COLUMN = "id";
	public static final String CLASS_COLUMN = "class";
	
	
	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE
			+ " (" + ID_COLUMN + DBUtil.STRING_TYPE+ DBUtil.COMMA_SEP +
					CLASS_COLUMN + DBUtil.STRING_TYPE +
			");";

	SQLiteOpenHelper dbHelper;
	RestVolleyDataSource restVolley;

	public static final String WHERE_ID_SELECTION = ID_COLUMN + " == ?";

	public DeletedEntries(RestVolleyDataSource restVolley, SQLiteOpenHelper dbHelper) {
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

	public void setAllreadyDeleted(String id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		setAsDeleted(db, id);
		db.close();
	}
	
	public <T extends IdentificableModel>void setAllreadyDeleted(Class<T> clazz, T model){
		restVolley.destroy(clazz, model, new OnServerDeletedListener(model.getId()), SynchDataSource.dummyErrorListener);
	}
	
	public void setAsDeleted(SQLiteDatabase db, String id) {
		String[] selectionArgs = { id };
		db.delete(TABLE, WHERE_ID_SELECTION, selectionArgs);
	}
	
	public void synchronize(){
		Cursor cursor = getCursor();
		while(cursor.moveToNext()){
			String id = CursorUtil.getString(cursor, ID_COLUMN);
			String className = CursorUtil.getString(cursor, CLASS_COLUMN);
			try {
				Class<IdentificableModel> clazz = (Class<IdentificableModel>) Class.forName(className);
				IdentificableModel model = (IdentificableModel) clazz.newInstance();
				model.setId(id);
				
				setAllreadyDeleted(clazz, model);
			} catch (ClassNotFoundException e) {
				//TODO log error
				e.printStackTrace();
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
			setAllreadyDeleted(id);
		}
	}
	
	private Cursor getCursor(){
		//TODO order may be important!!!
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String[] projection = {ID_COLUMN, CLASS_COLUMN};
		Cursor cursor = db.query(TABLE,	projection, null, null, null, null,  null);
		return cursor;
	}

	public boolean isDeleted(String id) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String[] projection = {ID_COLUMN, CLASS_COLUMN};
		String[] selectionArgs = { id };
		Cursor cursor = db.query(TABLE,	projection, WHERE_ID_SELECTION, selectionArgs, null, null,  null);
		return cursor.moveToNext();
	}

}

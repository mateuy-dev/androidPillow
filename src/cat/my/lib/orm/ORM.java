//package cat.my.lib.orm;
//
//import java.lang.reflect.Type;
//import java.util.Collection;
//
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import cat.my.lib.restvolley.models.IdentificableModel;
//
//import com.android.volley.Response.ErrorListener;
//import com.android.volley.Response.Listener;
//
//
//public class ORM {
//	SQLiteOpenHelper dbHelpert;
//	ModelMapper dbMapper;
//	
//	public ORM(SQLiteOpenHelper dbHelpert) {
//		super();
//		this.dbHelpert = dbHelpert;
//	}
//
//	public <T extends IdentificableModel> Collection<T> index(Class<T> clazz, Type collectionType) {
//		return null;
//	}
//	
//	public <T extends IdentificableModel> T show(T model) {
//		return null;
//	}
//	
//	public <T extends IdentificableModel> T get(T model) {
//		String selection = DBUtil.WHERE_ID_SELECTION;
//		// Specify arguments in placeholder order.
//		String[] selectionArgs = { model.getId() };
//
//		Cursor cursor = getCursor(selection, selectionArgs, null);
//
//		if (!cursor.moveToFirst()) {
//			// not present
//			return null;
//		}
//
//		T result = dbMapper.createModel(model, cursor);
//		cursor.close();
//		
//		return result;
//	}
//	
//	/**
//	 * Gets a database curser with all the attributes and the given selection and selectionArgs
//	 * @param selection
//	 * @param selectionArgs
//	 * @return
//	 */
//	protected Cursor getCursor(Class clazz, String selection, String[] selectionArgs, String orderBy) {
//		SQLiteDatabase db = dbHelpert.getReadableDatabase();
//
//		// Define a projection that specifies which columns from the database
//		// you will actually use after this query.
//		String[] projection = merge(new String[]{DBUtil.COLUMN_NAME_ID}, dbMapper.getModelAttributesForProjection());
//
//		Cursor cursor = db.query(dbMapper.getTableName(), // The table to query
//				projection, // The columns to return
//				selection, // The columns for the WHERE clause
//				selectionArgs, // The values for the WHERE clause
//				null, // don't group the rows
//				null, // don't filter by row groups
//				orderBy // The sort order
//				);
//		return cursor;
//	}
//
//	public <T extends IdentificableModel> T create(T model) {
//		SQLiteDatabase db = dbHelpert.getWritableDatabase();
//		
//		ContentValues values = new ContentValues();
//		values.put(DBUtil.COLUMN_NAME_DIRTY, DBUtil.BOOLEAN_TRUE);
//		
//		long milis = System.currentTimeMillis();
//		T existing = get(model);
//		if(existing==null){
//			//create
//			dbMapper.addModelContentValues(model, values);
//			values.put(DBUtil.COLUMN_NAME_ID, model.getId());
//			values.put(DBUtil.COLUMN_CREATED_AT, milis);
//			values.put(DBUtil.COLUMN_UPDATED_AT, milis);
//			
//			db.insert(dbMapper.getTableName(model), null, values);
//		} else {
//			//update
////			model = merge(model, existing);
//			dbMapper.addModelContentValues(model, values);
//			values.put(DBUtil.COLUMN_UPDATED_AT, milis);
//			db.update(dbMapper.getTableName(model), values,DBUtil.WHERE_ID_SELECTION, new String[]{model.getId()});
//		}
//		
//		T result = get(model);
//		db.close();
//		
//		return result;
//	}
//	
//	
//	
//	public <T extends IdentificableModel> T update(Class<T> clazz, T model) {
//		return null;
//	}
//	
//	public <T extends IdentificableModel> void destroy(Class<T> clazz, T model) {
//		
//	}
//}

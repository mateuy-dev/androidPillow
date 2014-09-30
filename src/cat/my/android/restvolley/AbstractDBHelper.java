package cat.my.android.restvolley;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import cat.my.android.restvolley.db.DBUtil;
import cat.my.android.restvolley.db.IDbMapping;
import cat.my.android.restvolley.sync.DeletedEntries;

public abstract class AbstractDBHelper extends SQLiteOpenHelper{
	public AbstractDBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
		
	public abstract List<IDbMapping<?>> getMappings();
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
	}
	
	public void resetTables(SQLiteDatabase db){
		createTables(db);
		dropTables(db);
	}
	
	public void createTables(SQLiteDatabase db){
		db.execSQL(DeletedEntries.CREATE_TABLE);
		for(IDbMapping<?> mapping: getMappings()){
			db.execSQL(DBUtil.createTable(mapping));
		}
	}
	
	public void dropTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS "+DeletedEntries.TABLE);
		for(IDbMapping<?> mapping: getMappings()){
			db.execSQL(DBUtil.dropTable(mapping));
		}
	}
		
}

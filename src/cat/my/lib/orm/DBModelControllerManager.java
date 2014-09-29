package cat.my.lib.orm;

import android.database.sqlite.SQLiteOpenHelper;
import cat.my.lib.mydata.DeletedEntries;
import cat.my.lib.restvolley.models.IdentificableModel;

public class DBModelControllerManager implements IDBModelControllerManager{
	SQLiteOpenHelper dbHelper;
	IMapperController mapperController;
	DeletedEntries deletedEntries;
	
	public DBModelControllerManager(SQLiteOpenHelper dbHelper, IMapperController mapperController, DeletedEntries deletedEntries){
		this.dbHelper = dbHelper;
		this.mapperController = mapperController;
		this.deletedEntries = deletedEntries;
	}
	

	@Override
	public <T extends IdentificableModel> DBModelController<T> getDBModelController(Class<T> clazz) {
		ModelMapper<T> mapper = mapperController.getMapper(clazz);
		return new DBModelController<T>(dbHelper, mapper, deletedEntries);
	}
}

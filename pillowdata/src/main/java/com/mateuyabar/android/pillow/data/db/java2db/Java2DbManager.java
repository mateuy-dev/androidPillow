package com.mateuyabar.android.pillow.data.db.java2db;

import java.util.ArrayList;
import java.util.List;

public class Java2DbManager {
    List<Java2DbType> java2DbTypes = new ArrayList<>();

    public Java2DbManager(){
        java2DbTypes.add(new BooleanJava2Db());
    }

    public Java2DbType get(Class<?> clazz){
        for(int i=0; i< java2DbTypes.size(); ++i){
            Java2DbType conversor = java2DbTypes.get(i);
            if(conversor.accepts(clazz))
                return conversor;
        }
        throw new UnsupportedOperationException("Class not supported"+clazz);
    }

}

#Android Pillow

##Data

###Data Sources

- IDataSource
  - IExtendedDataSource : index(T filter)
    -IDBDataSource: index(String selection, String[] selectionArgs, String order), count(String selection, String[] selectionArgs).
        -ISynchDataSource: sendDirty() download()
  - ILocalSynchDataSource: getDirty(int dirtyType), cacheAll(List<T> models), etc
/*
 * Copyright (c) Mateu Yabar Valles (http://mateuyabar.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.mateuyabar.android.pillow.data.rest;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mateuyabar.android.pillow.data.models.IdentificableModel;
import com.mateuyabar.android.pillow.Listeners;
import com.mateuyabar.android.pillow.PillowError;
import com.mateuyabar.android.pillow.data.core.IPillowResult;
import com.mateuyabar.android.pillow.data.core.PillowResult;
import com.mateuyabar.util.exceptions.UnimplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * List that will auto-load data when required.
 *
 * Known issues: if the reomote list is modified items may appear duplicated (if added in a allready loaded position) or skipped (if allready displayed position deleted).
 * size() is not calculated now. Returnring Integer.MAX_VALUE. When not more items found on server, we return null items;
 */
public abstract class  AbstractDataSourceCursoredList<T extends IdentificableModel> implements List<IPillowResult<T>> {
    public static final int LOAD_SIZE = 10;

    List<IPillowResult<T>> data = new ArrayList<>();
    Context context;

    public AbstractDataSourceCursoredList(Context context){
        this.context = context;
    }

    private synchronized void checkLoaded(int position){
        if(data.size()<=position){
            load(position);
        }
    }

    @Override
    public int size() {
        return Integer.MAX_VALUE;
    }

    /**
     * Loads the given position and the following LOAD_SIZE items
     * @param position
     */
    private synchronized void load(int position){
        final List<PillowResult> items = new ArrayList<>();
        for(int i =position; i<position+LOAD_SIZE; ++i){
            while(data.size()<=i){
                data.add(null);
            }
            PillowResult item = new PillowResult<T>();
            items.add(item);
            data.set(i, item);
        }
        IPillowResult<Collection<T>> subList = getData(LOAD_SIZE, position);
        subList.addListeners(new Listeners.Listener<Collection<T>>() {
            @Override
            public void onResponse(Collection<T> response) {
                Iterator<T> it = response.iterator();
                for (PillowResult<T> item : items) {
                    T value;
                    if (it.hasNext()) {
                        value = it.next();
                    } else {
                        //There are no more elements on the list. size() is not defined yet, so this will occur. We set null value.
                        value = null;
                    }
                    item.setResult(value);
                }
            }
        }, new Listeners.ErrorListener() {
            @Override
            public void onErrorResponse(PillowError error) {
                for (PillowResult<T> item : items) {
                    item.setError(error);
                }
            }
        });
    }

    public abstract IPillowResult<Collection<T>> getData(int size, int offset);

    @Override
    public IPillowResult<T> get(int location) {
        checkLoaded(location);
        return data.get(location);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @NonNull
    @Override
    public Iterator<IPillowResult<T>> iterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator<IPillowResult<T>> listIterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator<IPillowResult<T>> listIterator(int location) {
        return null;
    }



    @NonNull
    @Override
    public List<IPillowResult<T>> subList(int start, int end) {
        return null;
    }

    //-------------

    @Override
    public void add(int location, IPillowResult<T> object) {
        throw new UnimplementedException();
    }

    @Override
    public boolean add(IPillowResult<T> object) {
        throw new UnimplementedException();
    }

    @Override
    public boolean addAll(int location, Collection<? extends IPillowResult<T>> collection) {
        throw new UnimplementedException();
    }

    @Override
    public boolean addAll(Collection<? extends IPillowResult<T>> collection) {
        throw new UnimplementedException();
    }

    @Override
    public void clear() {
        throw new UnimplementedException();
    }

    @Override
    public boolean contains(Object object) {
        throw new UnimplementedException();
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        throw new UnimplementedException();
    }



    @Override
    public int indexOf(Object object) {
        throw new UnimplementedException();
    }

    @Override
    public int lastIndexOf(Object object) {
        throw new UnimplementedException();
    }

    @Override
    public IPillowResult<T> remove(int location) {
        throw new UnimplementedException();
    }

    @Override
    public boolean remove(Object object) {
        throw new UnimplementedException();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new UnimplementedException();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new UnimplementedException();
    }

    @Override
    public IPillowResult<T> set(int location, IPillowResult<T> object) {
        throw new UnimplementedException();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        throw new UnimplementedException();
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] array) {
        throw new UnimplementedException();
    }
}

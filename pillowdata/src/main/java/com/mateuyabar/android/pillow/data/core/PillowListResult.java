package com.mateuyabar.android.pillow.data.core;

import com.mateuyabar.android.pillow.Listeners;
import com.mateuyabar.android.pillow.PillowError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PillowListResult<T> extends PillowResult<List<T>> implements Listeners.ErrorListener {
    List<IPillowResult<T>> pillowResults;
    Map<Integer, T> finishedResults;
    List<T> results = new ArrayList<>();
    int size;
    Integer resultsFinishedCount = 0;
    PillowError error;

    public PillowListResult(List<IPillowResult<T>> pillowResults) {
        this.pillowResults = pillowResults;
        this.size = pillowResults.size();
        finishedResults = new HashMap<>();

        for(int i=0; i<size; ++i){
            IPillowResult<T> pillowResult = pillowResults.get(i);
            final int currentPosition = i;
            pillowResult.addListeners(new Listeners.Listener<T>() {
                @Override
                public void onResponse(T response) {
                    finishedResults.put(currentPosition, response);
                    resultFinished();
                }
            }, this);
        }
    }

    @Override
    public void onErrorResponse(PillowError error) {
        this.error = error;
        resultFinished();
    }

    private synchronized void resultFinished(){
        resultsFinishedCount++;
        if(resultsFinishedCount == size){
            if(error==null) {
                for (int i = 0; i < size; ++i) {
                    results.add(finishedResults.get(i));
                }
                setResult(results);
            } else {
                setError(error);
            }
        }
    }
}

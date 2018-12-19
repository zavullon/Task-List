package project.util;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request
{
    Map<String , List<ProgressListener>> progressListeners;

    public Request(String... eventTypes)
    {
        this.progressListeners = new HashMap<>();
        for(String eventType : eventTypes)
        {
            this.progressListeners.put(eventType , new ArrayList<>());
        }
    }

    public void subscribe(String eventType , ProgressListener progressListener)
    {
        this.progressListeners.get(eventType).add(progressListener);
    }

    public void unsubscribe(String eventType)
    {
        this.progressListeners.remove(eventType);
    }

    public void notifyListeners(String eventType)
    {
        this.progressListeners.get(eventType).forEach((l) -> l.update());
    }

    public void addUpdate()
    {
        this.notifyListeners("added");
    }

    public void doneUpdate()
    {
        this.notifyListeners("done");
    }
}

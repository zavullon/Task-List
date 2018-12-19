package project.util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

@FunctionalInterface
public interface ProgressListener
{
    public void update();
}

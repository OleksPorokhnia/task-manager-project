package com.alex.project.taskmanagerproject.util;

import java.util.List;

public interface RingBuffer<T> {
    public T addToBuffer(T t, int projId);
    public void removeFromBuffer(List<Integer> ids);
    public void takeFromBuffer(Integer id);
    public T updateInBuffer(T t, int projId, int taskId);
}

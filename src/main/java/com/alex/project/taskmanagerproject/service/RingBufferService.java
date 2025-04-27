package com.alex.project.taskmanagerproject.service;

import com.alex.project.taskmanagerproject.dto.DtoLayer;
import com.alex.project.taskmanagerproject.service.interfaces.ServiceLayer;
import com.alex.project.taskmanagerproject.util.RingBuffer;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;


public abstract class RingBufferService<V, T extends DtoLayer, S extends ServiceLayer<V, T>> implements RingBuffer<T> {

    protected static final int BUFFER_SIZE = 3;

    protected final S service;
    protected final ConcurrentHashMap<Integer, T> bufferMap = new ConcurrentHashMap<>();
    protected final Queue<Integer> orderQueue = new LinkedList<>();

    public RingBufferService(S service) {
        this.service = service;
    }
}

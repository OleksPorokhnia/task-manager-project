package com.alex.project.taskmanagerproject.service.interfaces;

import com.alex.project.taskmanagerproject.dto.DtoLayer;

import java.util.List;

public interface ServiceLayer<T, S extends DtoLayer> {
    public T create(S dto, int projId);
    public void deleteAll(List<Integer> ids);
    public T update(S dto, int projId, int id);
    public List<T> getAll(int projId);
    public void delete(int id);
}

package com.alex.project.taskmanagerproject.service.ringBuffer;

import com.alex.project.taskmanagerproject.dto.TaskDto;
import com.alex.project.taskmanagerproject.entity.Task;
import com.alex.project.taskmanagerproject.mappers.TaskMapper;
import com.alex.project.taskmanagerproject.service.RingBufferService;
import com.alex.project.taskmanagerproject.service.TaskService;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskRingBufferService extends RingBufferService<Task, TaskDto, TaskService> {


    private final TaskService taskService;

    private final TaskMapper taskMapper;

    private int counter = 1;

    public TaskRingBufferService(TaskService service, TaskService taskService, TaskMapper taskMapper) {
        super(service);
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskDto addToBuffer(TaskDto taskDto, int projId) {
        if(taskDto.getId() == 0){
            taskDto.setId(-(bufferMap.size() + counter));
        }
        taskDto.setProject_id(projId);

        if(bufferMap.size() >= BUFFER_SIZE){
            Integer oldestTaskId = orderQueue.poll();
            if(oldestTaskId != null){
                TaskDto oldestTaskDto = bufferMap.remove(oldestTaskId);
                oldestTaskDto.setId(0);
                taskService.create(oldestTaskDto, projId);
                bufferMap.put(taskDto.getId(), taskDto);
                orderQueue.offer(taskDto.getId());
                counter++;
                return taskDto;
            }
        }
        bufferMap.put(taskDto.getId(), taskDto);
        orderQueue.offer(taskDto.getId());
        return taskDto;
    }

    @Override
    public void removeFromBuffer(List<Integer> taskIds) {
        taskIds.stream()
                .forEach(id -> {
                    if(id > 0){
                        taskService.delete(id);
                    }else{
                        bufferMap.remove(id);
                    }
                });
    }

    @Override
    public void takeFromBuffer(Integer taskId) {

    }

    @Override
    public TaskDto updateInBuffer(TaskDto taskDto, int projId, int taskId) {
        if(taskId > 0){
            return taskMapper.toTaskDtoFromTask(taskService.update(taskDto, projId, taskId));
        }else{
            bufferMap.compute(taskId, (k, v) -> {
                if(v != null){
                    v = taskDto;
                }
                return v;
            });
            return taskDto;
        }
    }

    public List<TaskDto> getBufferCopy(int projId){
        return new ArrayList<>(bufferMap.values());
    }

    @EventListener(ContextClosedEvent.class)
    public void flushAllTasks(){
        taskService.saveAllTasks(new ArrayList<>(bufferMap.values()));
    }
}

package auca.ac.rw.RestfullApi.controller.taskmanagement;


import auca.ac.rw.RestfullApi.model.taskmanagement.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private List<Task> tasks = new ArrayList<>();
    private Long nextId = 7L; 

    
    public TaskController() {
        
        tasks.add(new Task(1L, "Complete project report", "Finish the quarterly project report for client", 
                          false, "HIGH", "2024-03-15"));
        tasks.add(new Task(2L, "Buy groceries", "Milk, eggs, bread, vegetables", 
                          false, "MEDIUM", "2024-02-20"));
        tasks.add(new Task(3L, "Schedule team meeting", "Set up weekly sync with development team", 
                          true, "MEDIUM", "2024-02-10"));
        tasks.add(new Task(4L, "Update resume", "Add recent projects and skills to resume", 
                          false, "LOW", "2024-03-01"));
        tasks.add(new Task(5L, "Pay electricity bill", "Due before end of month", 
                          true, "HIGH", "2024-02-05"));
        tasks.add(new Task(6L, "Read book chapter", "Read chapter 5 of Clean Code", 
                          false, "LOW", "2024-02-25"));
    }

   
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }


    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        Task task = findTaskById(taskId);
        if (task != null) {
            return new ResponseEntity<>(task, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<List<Task>> getTasksByStatus(@RequestParam boolean completed) {
        List<Task> filteredTasks = tasks.stream()
                .filter(task -> task.isCompleted() == completed)
                .collect(Collectors.toList());
        
        if (!filteredTasks.isEmpty()) {
            return new ResponseEntity<>(filteredTasks, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable String priority) {
        
        if (!priority.equalsIgnoreCase("LOW") && !priority.equalsIgnoreCase("MEDIUM") 
            && !priority.equalsIgnoreCase("HIGH")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        List<Task> tasksByPriority = tasks.stream()
                .filter(task -> task.getPriority().equalsIgnoreCase(priority))
                .collect(Collectors.toList());
        
        if (!tasksByPriority.isEmpty()) {
            return new ResponseEntity<>(tasksByPriority, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

   
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        
        if (task.getPriority() != null && 
            !task.getPriority().equalsIgnoreCase("LOW") && 
            !task.getPriority().equalsIgnoreCase("MEDIUM") && 
            !task.getPriority().equalsIgnoreCase("HIGH")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        task.setTaskId(nextId++);
        task.setCompleted(false); 
        tasks.add(task);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

   
    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task updatedTask) {
        Task existingTask = findTaskById(taskId);
        
        if (existingTask != null) {
            
            if (updatedTask.getPriority() != null && 
                !updatedTask.getPriority().equalsIgnoreCase("LOW") && 
                !updatedTask.getPriority().equalsIgnoreCase("MEDIUM") && 
                !updatedTask.getPriority().equalsIgnoreCase("HIGH")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setPriority(updatedTask.getPriority());
            existingTask.setDueDate(updatedTask.getDueDate());
            
            
            return new ResponseEntity<>(existingTask, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

   
    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<Task> markTaskAsCompleted(@PathVariable Long taskId) {
        Task task = findTaskById(taskId);
        
        if (task != null) {
            task.setCompleted(true);
            return new ResponseEntity<>(task, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        Task task = findTaskById(taskId);
        
        if (task != null) {
            tasks.remove(task);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    private Task findTaskById(Long taskId) {
        return tasks.stream()
                .filter(task -> task.getTaskId().equals(taskId))
                .findFirst()
                .orElse(null);
    }
}
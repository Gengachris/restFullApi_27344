package auca.ac.rw.question5_task_api.controller.task;



import auca.ac.rw.question5_task_api.model.task.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private List<Task> tasks = new ArrayList<>();

    public TaskController() {
        // Initialize with sample tasks
        tasks.add(new Task(1L, "Complete project ", "Finish the REST API of the project ", false, "HIGH", "2024-06-24"));
        tasks.add(new Task(2L, "Review code changes by other employee", "Review pull requests from other employees", false, "MEDIUM", "2024-06-31"));
        tasks.add(new Task(3L, "Update documentation", "Update API documentation", true, "LOW", "2024-01-10"));
        tasks.add(new Task(4L, "Fix bug reports", "Address critical bug reports", false, "HIGH", "2024-01-18"));
        tasks.add(new Task(5L, "Team meeting preparation", "Prepare agenda for weekly team meeting", false, "Low", "2024-01-13"));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        Optional<Task> task = tasks.stream()
                .filter(t -> t.getTaskId().equals(taskId))
                .findFirst();

        if (task.isPresent()) {
            return new ResponseEntity<>(task.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<List<Task>> getTasksByStatus(@RequestParam boolean completed) {
        List<Task> tasksByStatus = tasks.stream()
                .filter(task -> task.isCompleted() == completed)
                .collect(Collectors.toList());

        return new ResponseEntity<>(tasksByStatus, HttpStatus.OK);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable String priority) {
        List<Task> tasksByPriority = tasks.stream()
                .filter(task -> task.getPriority().equalsIgnoreCase(priority))
                .collect(Collectors.toList());

        return new ResponseEntity<>(tasksByPriority, HttpStatus.OK);
    }
    @GetMapping("/title/{title}")
    public ResponseEntity<List<Task>> getTasksByTitle(@PathVariable String title) {
        List<Task> tasksByTitle = tasks.stream()
                .filter(task -> task.getTitle().equalsIgnoreCase(title))
                .collect(Collectors.toList());
        return new ResponseEntity<>(tasksByTitle, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        // Generate new ID
        long newId = tasks.stream()
                .mapToLong(Task::getTaskId)
                .max()
                .orElse(0) + 1;
        task.setTaskId(newId);

        tasks.add(task);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task updatedTask) {
        Optional<Task> existingTask = tasks.stream()
                .filter(t -> t.getTaskId().equals(taskId))
                .findFirst();

        if (existingTask.isPresent()) {
            Task task = existingTask.get();
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setCompleted(updatedTask.isCompleted());
            task.setPriority(updatedTask.getPriority());
            task.setDueDate(updatedTask.getDueDate());

            return new ResponseEntity<>(task, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<Task> markTaskAsCompleted(@PathVariable Long taskId) {
        Optional<Task> task = tasks.stream()
                .filter(t -> t.getTaskId().equals(taskId))
                .findFirst();

        if (task.isPresent()) {
            Task t = task.get();
            t.setCompleted(true);
            return new ResponseEntity<>(t, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        boolean removed = tasks.removeIf(task -> task.getTaskId().equals(taskId));

        if (removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}



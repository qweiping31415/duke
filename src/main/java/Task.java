public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        //return (isDone ? "✓" : "✗" );
        return (isDone ? "\u2713" : "\u2718");
    }

    public String getDescription() {
        return description;
    }
    public String toString() {
        return String.format("[%s] %s", getStatusIcon(), getDescription());
    }

    public void doTask() {
        this.isDone = true;
    }
}
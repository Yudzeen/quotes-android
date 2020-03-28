package ebj.yujinkun.quotes.model;

public class Result<T> {

    private final Status status;
    private final Event event;
    private final T resource;

    public enum Status {
        IN_PROGRESS,
        SUCCESS,
        ERROR
    }

    public enum Event {
        GET_ALL,
        INSERT,
        UPDATE,
        DELETE
    }

    public Result(Status status, Event event, T resource) {
        this.status = status;
        this.event = event;
        this.resource = resource;
    }

    public Status getStatus() {
        return status;
    }

    public Event getEvent() {
        return event;
    }

    public T getResource() {
        return resource;
    }

    public static class Builder<T> {
        private Status status;
        private Event event;
        private T resource;

        public Builder<T> setStatus(Status status) {
            this.status = status;
            return this;
        }

        public Builder<T> setEvent(Event event) {
            this.event = event;
            return this;
        }

        public Builder<T> setResource(T resource) {
            this.resource = resource;
            return this;
        }

        public Result<T> build() {
            return new Result<>(status, event, resource);
        }
    }

    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", event=" + event +
                ", resource=" + resource +
                '}';
    }
}

package ch.fhnw.wodss.webapplication.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MemoryRepository<T> {

    private final Map<Long, T> entries;

    private long counter;

    public MemoryRepository() {
        this.entries = new HashMap<>();
        this.counter = 1;
    }

    public List<T> getEntries() {
        return new ArrayList<>(entries.values());
    }

    public T getEntry(Long id) {
        return entries.get(id);
    }

    public T saveEntry(T entry) {
        entries.put(counter, entry);
        setEntityId(entry, counter);
        counter++;
        return entry;
    }

    public T updateEntry(Long id, T entry) {
        entries.put(id, entry);
        setEntityId(entry, id);
        return entry;
    }

    public void deleteEntry(Long id) {
        entries.remove(id);
    }

    public abstract void setEntityId(T entry, Long value);
}

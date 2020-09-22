package pl.wrona.equinix.repository;

import org.apache.commons.collections4.map.LinkedMap;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheRepository<E> {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private LinkedMap<String, E> caches = new LinkedMap<>();

    public void put(String key, E value) {
        writeLock.lock();
        try {
            caches.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public E get(String key) {
        readLock.lock();
        try {
            return caches.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public void remove(String key) {
        readLock.lock();
        try {
            caches.remove(key);
        } finally {
            readLock.unlock();
        }
    }

    public void removeFirst() {
        readLock.lock();
        try {
            caches.remove(0);
        } finally {
            readLock.unlock();
        }
    }

    public int size() {
        return caches.size();
    }


}

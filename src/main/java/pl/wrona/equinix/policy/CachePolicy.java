package pl.wrona.equinix.policy;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.map.LinkedMap;
import pl.wrona.equinix.cache.Cache;
import pl.wrona.equinix.repository.CacheRepository;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.util.Objects.nonNull;

public class CachePolicy<E extends Cache> {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private CacheRepository cacheRepository = new CacheRepository();
    //    private LinkedMap<String, E> caches = new LinkedMap<>();
    @Setter
    @Getter
    private CachePolicy cachePolicy;

    public CachePolicy(CachePolicy cachePolicy) {
        super();
        this.cachePolicy = cachePolicy;
    }

    public void put(String key, E value) {
        this.checkPolicy(key);
        this.cacheRepository.put(key, value);
    }

    public E get(String key) {
        this.checkPolicy(key);
        return (E) cacheRepository.get(key);
    }

    public void checkPolicy(String key) {
        if (nonNull(this.cachePolicy)) {
            this.cachePolicy.checkPolicy(key);
        }
    }

    public CacheRepository getCaches() {
        if (nonNull(this.cacheRepository)) {
            return this.cacheRepository;
        }
        return this.cachePolicy.getCaches();
    }

    public int getSize() {
        return getCaches().size();
    }

    public void removeFirstElement() {
        this.cacheRepository.removeFirst();
    }

    public void remove(String key) {
        this.cacheRepository.remove(key);
    }
}

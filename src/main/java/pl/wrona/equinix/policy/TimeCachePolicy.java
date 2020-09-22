package pl.wrona.equinix.policy;

import pl.wrona.equinix.cache.Cache;
import pl.wrona.equinix.cache.DateCache;

import java.util.Optional;

public class TimeCachePolicy<E extends DateCache> extends CachePolicy<E> {

    private final long duration;

    public TimeCachePolicy(CachePolicy<Cache> cacheCachePolicy, Long duration) {
        super(cacheCachePolicy);
        this.duration = duration;
    }

    @Override
    public void checkPolicy(String key) {
        if (getCacheRemoveDate(key) <= System.currentTimeMillis()) {
            super.remove(key);
        }
        super.checkPolicy(key);
    }

    private long getCacheRemoveDate(String key) {
        return Optional.ofNullable(getCaches().get(key))
                .map(cache -> ((DateCache) cache).getCreateDate())
                .map(createDate -> createDate + duration)
                .orElse(0l);
    }
}

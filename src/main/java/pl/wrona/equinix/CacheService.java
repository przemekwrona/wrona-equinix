package pl.wrona.equinix;

import lombok.AllArgsConstructor;
import pl.wrona.equinix.cache.Cache;
import pl.wrona.equinix.policy.CachePolicy;

import java.util.Optional;

@AllArgsConstructor
public class CacheService<E> {

    private CachePolicy<Cache<E>> cachePolicy;

    public void put(String key, E value) {
        cachePolicy.put(key, new Cache<>(value));
    }

    public void put(String key, Cache<E> value) {
        cachePolicy.put(key, value);
    }

    public E get(String key) {
        return Optional.ofNullable(cachePolicy.get(key))
                .map(Cache::getValue)
                .orElse(null);
    }

}

package pl.wrona.equinix.policy;

import pl.wrona.equinix.cache.Cache;

public class SizeCachePolicy<E extends Cache> extends CachePolicy<E> {

    private int maxSize;

    public SizeCachePolicy(CachePolicy policy, int maxSize) {
        super(policy);
        this.maxSize = maxSize;
    }

    @Override
    public void checkPolicy(String key) {
        if (super.getSize() > maxSize) {
            super.removeFirstElement();
        }
        super.checkPolicy(key);
    }
}

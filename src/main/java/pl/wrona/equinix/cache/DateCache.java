package pl.wrona.equinix.cache;

import lombok.Getter;

@Getter
public class DateCache<E> extends Cache<E> {

    private long createDate;

    public DateCache(E value) {
        super(value);
        this.createDate = System.currentTimeMillis();
    }
}

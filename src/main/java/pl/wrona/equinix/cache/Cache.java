package pl.wrona.equinix.cache;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Cache<E> {

    private E value;

    public E getValue() {
        return this.value;
    }
}

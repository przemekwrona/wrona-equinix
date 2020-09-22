package pl.wrona.equinix

import pl.wrona.equinix.cache.Cache
import pl.wrona.equinix.cache.DateCache
import pl.wrona.equinix.policy.CachePolicy
import pl.wrona.equinix.policy.SizeCachePolicy
import pl.wrona.equinix.policy.TimeCachePolicy
import spock.lang.Specification
import spock.lang.Unroll

class CacheServiceSpec extends Specification {

    @Unroll
    def 'should add cache'() {
        given:
            def policy = new CachePolicy<Cache>()
            def cacheService = new CacheService(policy)
        when:
            cacheService.put(KEY, VALUE)
        then:
            cacheService.get(KEY) == VALUE
        where:
            KEY               | VALUE
            'pl.cache.domain' | 'value-1'
    }

    @Unroll
    def 'should add 2 caches'() {
        given:
            def policy = new CachePolicy<Cache>()
            def cacheService = new CacheService(policy)
        when:
            cacheService.put(KEY_1, VALUE_1)
            cacheService.put(KEY_2, VALUE_2)
        then:
            cacheService.get(KEY_1) == VALUE_1
            cacheService.get(KEY_2) == VALUE_2
        where:
            KEY_1             | VALUE_1   | KEY_2              | VALUE_2
            'pl.cache.domain' | 'value-1' | 'pl.cache.equinix' | 'value-2'
    }

    @Unroll
    def 'should add 2 caches with size policy 2 element in collection'() {
        given:
            def policy = new CachePolicy<Cache>()
            def sizeCachePolicy = new SizeCachePolicy(policy, 2)
            def cacheService = new CacheService(sizeCachePolicy)
        when:
            cacheService.put(KEY_1, VALUE_1)
            cacheService.put(KEY_2, VALUE_2)
            cacheService.put(KEY_3, VALUE_3)
        then:
            cacheService.get(KEY_1) == null
            cacheService.get(KEY_2) == VALUE_2
            cacheService.get(KEY_3) == VALUE_3
        where:
            KEY_1             | VALUE_1   | KEY_2              | VALUE_2   | KEY_3            | VALUE_3
            'pl.cache.domain' | 'value-1' | 'pl.cache.equinix' | 'value-2' | 'pl.cache.jlabs' | 'value-3'
    }

    @Unroll
    def 'should add 3 caches with time policy 1s in collection with sleep after 1st element'() {
        given:
            def policy = new CachePolicy<Cache>()
            def timeCachePolicy = new TimeCachePolicy(policy, 1000l)
            def cacheService = new CacheService(timeCachePolicy)
        when:
            cacheService.put(KEY_1, new DateCache(VALUE_1))
            sleep(2000)
            cacheService.put(KEY_2, new DateCache(VALUE_2))
            cacheService.put(KEY_3, new DateCache(VALUE_3))
        then:
            cacheService.get(KEY_1) == null
            cacheService.get(KEY_2) == VALUE_2
            cacheService.get(KEY_3) == VALUE_3
        where:
            KEY_1             | VALUE_1   | KEY_2              | VALUE_2   | KEY_3            | VALUE_3
            'pl.cache.domain' | 'value-1' | 'pl.cache.equinix' | 'value-2' | 'pl.cache.jlabs' | 'value-3'
    }

    @Unroll
    def 'should add 3 caches with time policy 1s in collection with sleep after 2nd element'() {
        given:
            def policy = new CachePolicy<Cache>()
            def timeCachePolicy = new TimeCachePolicy(policy, 1000l)
            def cacheService = new CacheService(timeCachePolicy)
        when:
            cacheService.put(KEY_1, new DateCache(VALUE_1))
            cacheService.put(KEY_2, new DateCache(VALUE_2))
            sleep(2000)
            cacheService.put(KEY_3, new DateCache(VALUE_3))
        then:
            cacheService.get(KEY_1) == null
            cacheService.get(KEY_2) == null
            cacheService.get(KEY_3) == VALUE_3
        where:
            KEY_1             | VALUE_1   | KEY_2              | VALUE_2   | KEY_3            | VALUE_3
            'pl.cache.domain' | 'value-1' | 'pl.cache.equinix' | 'value-2' | 'pl.cache.jlabs' | 'value-3'
    }

    @Unroll
    def 'should add 3 caches with size policy and time policy'() {
        given:
            def policy = new CachePolicy<Cache>()
            def timeCachePolicy = new TimeCachePolicy(policy, 1000l)
            def sizePolicy = new SizeCachePolicy(timeCachePolicy, 2)
            def cacheService = new CacheService(sizePolicy)
        when:
            cacheService.put(KEY_1, new DateCache(VALUE_1))
            cacheService.put(KEY_2, new DateCache(VALUE_2))
            cacheService.put(KEY_3, new DateCache(VALUE_3))
            sleep(DELAY)
        then:
            cacheService.get(KEY_1) == null
            cacheService.get(KEY_2) == EXPECTED_VALUE_2
            cacheService.get(KEY_3) == EXPECTED_VALUE_3
        where:
            KEY_1             | VALUE_1   | KEY_2              | VALUE_2   | EXPECTED_VALUE_2 | KEY_3            | VALUE_3   | EXPECTED_VALUE_3 | DELAY
            'pl.cache.domain' | 'value-1' | 'pl.cache.equinix' | 'value-2' | 'value-2'        | 'pl.cache.jlabs' | 'value-3' | 'value-3'        | 500
    }
}

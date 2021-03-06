    @Override public void clear() {
        for (Object readerKey : seenReaders.keySet()) {
            Boolean removed = seenReaders.remove(readerKey);
            if (removed == null) {
                return;
            }
            seenReadersCount.decrementAndGet();
            ConcurrentMap<FilterCacheKey, FilterCacheValue<DocSet>> cache = cache();
            for (FilterCacheKey key : cache.keySet()) {
                if (key.readerKey() == readerKey) {
                    FilterCacheValue<DocSet> removed2 = cache.remove(key);
                    if (removed2 != null) {
                        totalCount.decrementAndGet();
                        totalSizeInBytes.addAndGet(-removed2.value().sizeInBytes());
                    }
                }
            }
        }
    }

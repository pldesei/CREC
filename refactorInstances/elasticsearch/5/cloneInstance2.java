    synchronized public void merge(IndexWriter writer) throws CorruptIndexException, IOException {
        while (true) {
            MergePolicy.OneMerge merge = writer.getNextMerge();
            if (merge == null)
                break;

            if (logger.isTraceEnabled()) {
                logger.trace("merge [{}] starting...", merge.info.name);
            }

            long time = System.currentTimeMillis();
            currentMerges.incrementAndGet();
            try {
                writer.merge(merge);
            } finally {
                currentMerges.decrementAndGet();
                totalMerges.incrementAndGet();
                long took = System.currentTimeMillis() - time;
                totalMergeTime.addAndGet(took);
                if (took > 20000) { // if more than 20 seconds, DEBUG log it
                    logger.debug("merge [{}] done, took [{}]", merge.info.name, TimeValue.timeValueMillis(took));
                } else if (logger.isTraceEnabled()) {
                    logger.trace("merge [{}] done, took [{}]", merge.info.name, TimeValue.timeValueMillis(took));
                }
            }
        }
    }

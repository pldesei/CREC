        } else if (randomBoolean()) {
            logger.info("Index [{}] docs async: [{}] bulk: [{}]", builders.size(), false, false);
            for (IndexRequestBuilder indexRequestBuilder : builders) {
                indexRequestBuilder.execute().actionGet();
                if (rarely()) {
                    if (rarely()) {
                        client().admin().indices().prepareRefresh(indices).setIndicesOptions(IndicesOptions.lenient()).execute(new LatchedActionListener<RefreshResponse>(newLatch(latches)));
                    } else if (rarely()) {
                        client().admin().indices().prepareFlush(indices).setIndicesOptions(IndicesOptions.lenient()).execute(new LatchedActionListener<FlushResponse>(newLatch(latches)));
                    } else if (rarely()) {
                        client().admin().indices().prepareOptimize(indices).setIndicesOptions(IndicesOptions.lenient()).setMaxNumSegments(between(1, 10)).setFlush(random.nextBoolean()).execute(new LatchedActionListener<OptimizeResponse>(newLatch(latches)));
                    }
                }
            }
        } else {

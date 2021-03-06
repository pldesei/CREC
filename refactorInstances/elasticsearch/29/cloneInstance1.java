        if (frequently()) {
            logger.info("Index [{}] docs async: [{}] bulk: [{}]", builders.size(), true, false);
            final CountDownLatch latch = new CountDownLatch(builders.size());
            latches.add(latch);
            for (IndexRequestBuilder indexRequestBuilder : builders) {
                indexRequestBuilder.execute(new PayloadLatchedActionListener<IndexResponse, IndexRequestBuilder>(indexRequestBuilder, latch, errors));
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

        } else if (randomBoolean()) {

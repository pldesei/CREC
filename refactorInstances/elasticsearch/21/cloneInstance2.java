    public void testWaitForAliasCreationSingleShard() throws Exception {
        // delete all indices
        client().admin().indices().prepareDelete().execute().actionGet();

        logger.info("--> creating index [test]");
        client().admin().indices().create(createIndexRequest("test").settings(settingsBuilder().put("index.numberOfReplicas", 0).put("index.numberOfShards", 1))).actionGet();

        logger.info("--> running cluster_health");
        ClusterHealthResponse clusterHealth = client().admin().cluster().health(clusterHealthRequest().waitForGreenStatus()).actionGet();
        logger.info("--> done cluster_health, status " + clusterHealth.getStatus());
        assertThat(clusterHealth.isTimedOut(), equalTo(false));
        assertThat(clusterHealth.getStatus(), equalTo(ClusterHealthStatus.GREEN));

        for (int i = 0; i < 10; i++) {
            assertThat(client().admin().indices().prepareAliases().addAlias("test", "alias" + i).execute().actionGet().isAcknowledged(), equalTo(true));
            client().index(indexRequest("alias" + i).type("type1").id("1").source(source("1", "test")).refresh(true)).actionGet();
        }
    }

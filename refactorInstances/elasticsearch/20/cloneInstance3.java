    public void simpleMatchedQueryFromTopLevelFilterAndFilteredQuery() throws Exception {

        client().admin().indices().prepareCreate("test").execute().actionGet();
        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForGreenStatus().execute().actionGet();

        client().prepareIndex("test", "type1", "1").setSource(jsonBuilder().startObject()
                .field("name", "test")
                .field("title", "title1")
                .endObject()).execute().actionGet();

        client().prepareIndex("test", "type1", "2").setSource(jsonBuilder().startObject()
                .field("name", "test")
                .field("title", "title2")
                .endObject()).execute().actionGet();

        client().prepareIndex("test", "type1", "3").setSource(jsonBuilder().startObject()
                .field("name", "test")
                .field("title", "title3")
                .endObject()).execute().actionGet();

        client().admin().indices().prepareRefresh().execute().actionGet();

        SearchResponse searchResponse = client().prepareSearch()
                .setQuery(filteredQuery(matchAllQuery(), termsFilter("title", "title1", "title2", "title3").filterName("title")))
                        .setFilter(termFilter("name", "test").filterName("name"))
                        .execute().actionGet();

        assertNoFailures(searchResponse);
        assertThat(searchResponse.getHits().totalHits(), equalTo(3l));

        for (SearchHit hit : searchResponse.getHits()) {
            if (hit.id().equals("1") || hit.id().equals("2") || hit.id().equals("3")) {
                assertThat(hit.matchedQueries().length, equalTo(2));
                assertThat(hit.matchedQueries(), hasItemInArray("name"));
                assertThat(hit.matchedQueries(), hasItemInArray("title"));
            } else {
                fail("Unexpected document returned with id " + hit.id());
            }
        }

        searchResponse = client().prepareSearch()
                .setQuery(termsQuery("title", "title1", "title2", "title3").queryName("title"))
                .setFilter(queryFilter(matchQuery("name", "test").queryName("name")))
                .execute().actionGet();

        assertNoFailures(searchResponse);
        assertThat(searchResponse.getHits().totalHits(), equalTo(3l));

        for (SearchHit hit : searchResponse.getHits()) {
            if (hit.id().equals("1") || hit.id().equals("2") || hit.id().equals("3")) {
                assertThat(hit.matchedQueries().length, equalTo(2));
                assertThat(hit.matchedQueries(), hasItemInArray("name"));
                assertThat(hit.matchedQueries(), hasItemInArray("title"));
            } else {
                fail("Unexpected document returned with id " + hit.id());
            }
        }
    }

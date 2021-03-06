    public void withShardSize_double_singleShard() throws Exception {

        client().admin().indices().prepareCreate("idx")
                .addMapping("type", "key", "type=double")
                .execute().actionGet();

        indexData();

        SearchResponse response = client().prepareSearch("idx").setTypes("type").setRouting("1")
                .setQuery(matchAllQuery())
                .addFacet(termsFacet("keys").field("key").size(3).shardSize(5).order(TermsFacet.ComparatorType.COUNT))
                .execute().actionGet();

        Facets facets = response.getFacets();
        TermsFacet terms = facets.facet("keys");
        List<? extends TermsFacet.Entry> entries = terms.getEntries();
        assertThat(entries.size(), equalTo(3)); // we still only return 3 entries (based on the 'size' param)
        Map<Integer, Integer> expected = ImmutableMap.<Integer, Integer>builder()
                .put(1, 5)
                .put(2, 4)
                .put(3, 3)
                .build();
        for (TermsFacet.Entry entry : entries) {
            assertThat(entry.getCount(), equalTo(expected.get(entry.getTermAsNumber().intValue())));
        }
    }

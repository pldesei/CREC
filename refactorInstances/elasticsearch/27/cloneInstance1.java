    public void noShardSize_string() throws Exception {

        client().admin().indices().prepareCreate("idx")
                .addMapping("type", "key", "type=string,index=not_analyzed")
                .execute().actionGet();

        indexData();

        SearchResponse response = client().prepareSearch("idx").setTypes("type")
                .setQuery(matchAllQuery())
                .addFacet(termsFacet("keys").field("key").size(3).order(TermsFacet.ComparatorType.COUNT))
                .execute().actionGet();

        Facets facets = response.getFacets();
        TermsFacet terms = facets.facet("keys");
        List<? extends TermsFacet.Entry> entries = terms.getEntries();
        assertThat(entries.size(), equalTo(3));
        Map<String, Integer> expected = ImmutableMap.<String, Integer>builder()
                .put("1", 8)
                .put("3", 8)
                .put("2", 4)
                .build();
        for (TermsFacet.Entry entry : entries) {
            assertThat(entry.getCount(), equalTo(expected.get(entry.getTerm().string())));
        }
    }

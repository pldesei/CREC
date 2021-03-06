    public Map<String, String> load(String source) throws IOException {
        // replace tabs with whitespace (yaml does not accept tabs, but many users might use it still...)
        source = source.replace("\t", "  ");
        Yaml yaml = new Yaml();
        Map<Object, Object> yamlMap = (Map<Object, Object>) yaml.load(source);
        StringBuilder sb = new StringBuilder();
        Map<String, String> settings = newHashMap();
        if (yamlMap == null) {
            return settings;
        }
        List<String> path = newArrayList();
        serializeMap(settings, sb, path, yamlMap);
        return settings;
    }

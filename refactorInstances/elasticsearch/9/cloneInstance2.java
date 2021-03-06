    public Map<String, String> load(byte[] source) throws IOException {
        Yaml yaml = new Yaml();
        Map<Object, Object> yamlMap = (Map<Object, Object>) yaml.load(new FastByteArrayInputStream(source));
        StringBuilder sb = new StringBuilder();
        Map<String, String> settings = newHashMap();
        if (yamlMap == null) {
            return settings;
        }
        List<String> path = newArrayList();
        serializeMap(settings, sb, path, yamlMap);
        return settings;
    }

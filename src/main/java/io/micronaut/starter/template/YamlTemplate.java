package io.micronaut.starter.template;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class YamlTemplate implements Template {

    private static final Pattern DOT_PATTERN = Pattern.compile("\\.");
    private final String path;
    private final Map<String, Object> config;

    public YamlTemplate(String path, Map<String, Object> config) {
        this.path = path;
        this.config = transform(config);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);

        Yaml yaml = new Yaml(options);
        yaml.dump(config, new OutputStreamWriter(outputStream));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> transform(Map<String, Object> config) {
        Map<String, Object> transformed = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry: config.entrySet()) {
            Map<String, Object> finalMap = transformed;
            String k = entry.getKey();
            Object value = entry.getValue();
            int index = k.indexOf('.');
            if (index != -1) {
                String[] keys = DOT_PATTERN.split(k);
                for (int i = 0; i < keys.length - 1; i++) {
                    if (!finalMap.containsKey(keys[i])) {
                        finalMap.put(keys[i], new HashMap<>());
                    }
                    Object next = finalMap.get(keys[i]);
                    if (next instanceof Map) {
                        finalMap = ((Map<String,Object>) next);
                    }
                }
                finalMap.put(keys[keys.length - 1], value);
            } else {
                finalMap.put(k, value);
            }
        }
        return transformed;
    }
}

/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.template;

import io.micronaut.starter.feature.config.Configuration;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class TomlTemplate extends DefaultTemplate {

    private final Map<DottedKey, Map<DottedKey, Object>> tables;

    public TomlTemplate(String path, Configuration config) {
        this(DEFAULT_MODULE, path, config);
    }

    public TomlTemplate(String module, String path, Configuration config) {
        super(module, path);

        // normalize config
        Map<DottedKey, Object> normalized = normalizeTopLevel(config);

        // collect table keys we want
        List<DottedKey> tableKeys = new ArrayList<>();
        tableKeys.add(DottedKey.EMPTY);
        tableKeys.addAll(suggestTables(normalized.keySet()));

        // avoid empty keys
        tableKeys.removeAll(normalized.keySet());

        // sort (normalized) config into tables using SortedMap
        SortedMap<DottedKey, Map<DottedKey, Object>> tables = new TreeMap<>();
        for (DottedKey tableKey : tableKeys) {
            tables.put(tableKey, new LinkedHashMap<>());
        }
        for (Map.Entry<DottedKey, Object> entry : normalized.entrySet()) {
            sortIntoTables(tables, entry.getKey(), entry.getValue());
        }

        // transform SortedMap back to tableKeys order
        this.tables = new LinkedHashMap<>();
        for (DottedKey tableKey : tableKeys) {
            this.tables.put(tableKey, tables.get(tableKey));
        }
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        for (Map.Entry<DottedKey, Map<DottedKey, Object>> table : tables.entrySet()) {
            if (!table.getKey().equals(DottedKey.EMPTY)) {
                writer.append("\n[");
                emitKey(writer, table.getKey());
                writer.append("]\n");
            }
            for (Map.Entry<DottedKey, Object> entry : table.getValue().entrySet()) {
                emitKey(writer, entry.getKey());
                writer.write(" = ");
                emitValue(writer, entry.getValue());
                writer.write('\n');
            }
        }
        writer.flush();
    }

    private static Map<DottedKey, Object> normalizeTopLevel(Object here) {
        Map<DottedKey, Object> target = new LinkedHashMap<>();
        normalizeTopLevel(target, DottedKey.EMPTY, here);
        return target;
    }

    private static void normalizeTopLevel(Map<DottedKey, Object> target, DottedKey prefix, Object here) {
        if (here instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                normalizeTopLevel(target, prefix.resolve(((String) entry.getKey()).split("\\.")), entry.getValue());
            }
        } else {
            target.put(prefix, here);
        }
    }

    private static Collection<DottedKey> suggestTables(Collection<DottedKey> keys) {
        // we suggest any tables that will have at least two child keys.

        Set<DottedKey> tables = new HashSet<>();
        SortedSet<DottedKey> remainingKeys = new TreeSet<>(keys);
        for (int prefixLength = keys.stream().mapToInt(k -> k.parts.size()).max().orElse(0); prefixLength > 0; ) {
            boolean createdTable = false;
            for (DottedKey key : remainingKeys) {
                if (key.parts.size() <= prefixLength) {
                    continue;
                }
                // proposed table key
                DottedKey proposed = new DottedKey(key.parts.subList(0, prefixLength));
                // find first key that won't be part of this table
                DottedKey end = null;
                for (DottedKey following : remainingKeys.tailSet(proposed)) {
                    if (!following.startsWith(proposed)) {
                        end = following;
                        break;
                    }
                }
                SortedSet<DottedKey> tableSet = end == null ? remainingKeys.tailSet(proposed) : remainingKeys.subSet(proposed, end);
                // is the table eligible?
                if (tableSet.size() < 2 || tableSet.first().equals(proposed)) {
                    continue;
                }

                createdTable = true;
                tables.add(proposed);
                // remove keys in this table from further consideration
                tableSet.clear();
                // try again, but with a new iterator (we changed the remainingKeys set)
                break;
            }
            if (!createdTable) {
                prefixLength--;
            }
        }
        // we've decided which tables to use, now get them back into the order they came in.
        Set<DottedKey> tablesInOrder = new LinkedHashSet<>();
        for (DottedKey key : keys) {
            // find the closest ancestor
            while (!key.equals(DottedKey.EMPTY)) {
                key = key.parent();
                if (tables.contains(key)) {
                    tablesInOrder.add(key);
                    break;
                }
            }
        }
        return tablesInOrder;
    }

    private static void sortIntoTables(SortedMap<DottedKey, Map<DottedKey, Object>> tables, DottedKey key, Object value) {
        // `tables` is sorted in lexicographical order (by DottedKey.compareTo). We want to find the longest prefix p_l
        // of `key` that is present in `tables`. Algorithm is as follows:
        //  - If the current prefix p_c is in `tables`, stop.
        //  - Else, find the key p_k just before the positions where p_c would be. Two possibilities:
        //     1. p_k is a prefix of p_c.
        //     2. p_k is placed between p_l, and the insertion position of p_c (p_l < p_k < p_c). In lexicographical
        //        order, this means that p_l is also a prefix of p_k.
        //    Because of these two cases, we can set p_c = sharedPrefix(p_k, p_l) and the p_l will still be a prefix of
        //    the new p_c.

        DottedKey prefix = key;
        while (true) {
            Map<DottedKey, Object> table = tables.get(prefix);
            if (table != null) {
                table.put(key.removePrefix(prefix.parts.size()), value);
                break;
            }
            // no table found yet
            SortedMap<DottedKey, Map<DottedKey, Object>> headMap = tables.headMap(prefix);
            prefix = prefix.sharedPrefix(headMap.lastKey());
        }
    }

    private static void emitKey(Appendable to, DottedKey key) throws IOException {
        for (int i = 0; i < key.parts.size(); i++) {
            if (i != 0) {
                to.append('.');
            }
            emitKey(to, key.parts.get(i));
        }
    }

    private static void emitKey(Appendable to, String key) throws IOException {
        emitStringImpl(to, TomlStringOutputUtil.MASK_SIMPLE_KEY, key);
    }

    private static void emitValue(Appendable to, Object value) throws IOException {
        if (value instanceof Number || value instanceof Boolean) {
            to.append(value.toString());
        } else if (value instanceof Collection collection) {
            to.append('[');
            for (Iterator<?> iterator = collection.iterator(); iterator.hasNext(); ) {
                emitValue(to, iterator.next());
                if (iterator.hasNext()) {
                    to.append(", ");
                }
            }
            to.append(']');
        } else if (value instanceof Map map) {
            to.append('{');
            for (Iterator<? extends Map.Entry<?, ?>> iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<?, ?> entry = iterator.next();
                emitKey(to, (String) entry.getKey());
                to.append(" = ");
                emitValue(to, entry.getValue());
                if (iterator.hasNext()) {
                    to.append(", ");
                }
            }
            to.append('}');
        } else {
            // String and other types
            emitStringImpl(to, TomlStringOutputUtil.MASK_STRING, value.toString());
        }
    }

    /**
     * From jackson-dataformats-text
     */
    private static void emitStringImpl(Appendable to, int categoryMask, String name) throws IOException {
        int cat = TomlStringOutputUtil.categorize(name) & categoryMask;
        if ((cat & TomlStringOutputUtil.UNQUOTED_KEY) != 0) {
            to.append(name);
        } else if ((cat & TomlStringOutputUtil.LITERAL_STRING) != 0) {
            to.append('\'');
            to.append(name);
            to.append('\'');
        } else if ((cat & TomlStringOutputUtil.BASIC_STRING_NO_ESCAPE) != 0) {
            to.append('"');
            to.append(name);
            to.append('"');
        } else if ((cat & TomlStringOutputUtil.BASIC_STRING) != 0) {
            to.append('"');
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                String escape = TomlStringOutputUtil.getBasicStringEscape(c);
                if (escape == null) {
                    to.append(c);
                } else {
                    to.append(escape);
                }
            }
            to.append('"');
        } else {
            throw new IOException("Key contains unsupported characters");
        }
    }

    private static class DottedKey implements Comparable<DottedKey> {
        static final DottedKey EMPTY = new DottedKey(Collections.emptyList());

        private final List<String> parts;

        DottedKey(List<String> parts) {
            this.parts = parts;
        }

        DottedKey resolve(String... suffix) {
            List<String> combined = new ArrayList<>(this.parts.size() + suffix.length);
            combined.addAll(this.parts);
            Collections.addAll(combined, suffix);
            return new DottedKey(combined);
        }

        DottedKey sharedPrefix(DottedKey o) {
            int i = 0;
            for (; i < this.parts.size() && i < o.parts.size(); i++) {
                if (!this.parts.get(i).equals(o.parts.get(i))) {
                    break;
                }
            }
            return new DottedKey(this.parts.subList(0, i));
        }

        DottedKey removePrefix(int length) {
            return new DottedKey(this.parts.subList(length, this.parts.size()));
        }

        DottedKey parent() {
            return new DottedKey(this.parts.subList(0, this.parts.size() - 1));
        }

        boolean startsWith(DottedKey ancestor) {
            return ancestor.parts.size() <= this.parts.size() &&
                    ancestor.parts.equals(this.parts.subList(0, ancestor.parts.size()));
        }

        @Override
        public int compareTo(DottedKey o) {
            for (int i = 0; i < this.parts.size() && i < o.parts.size(); i++) {
                int cmp = this.parts.get(i).compareTo(o.parts.get(i));
                if (cmp != 0) {
                    return cmp;
                }
            }
            return Integer.compare(this.parts.size(), o.parts.size());
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof DottedKey dk && this.parts.equals(dk.parts);
        }

        @Override
        public int hashCode() {
            return Objects.hash(parts);
        }
    }
}

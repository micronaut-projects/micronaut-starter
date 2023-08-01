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
package io.micronaut.starter.analytics.postgres;

import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Slice;
import io.micronaut.data.model.Sort;
import io.micronaut.starter.options.JdkVersion;
import jakarta.inject.Singleton;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Generates Excel spreadsheets.
 */
@Singleton
public class ExcelGenerator {

    private static final String SHEET_NAME = "Applications";

    private static final Function<Workbook, CellStyle> CELL_STYLE_DATE_TIME = workbook -> {
        final CellStyle style = workbook.createCellStyle();
        style.setDataFormat((short) BuiltinFormats.getBuiltinFormat("m/d/yy h:mm"));
        return style;
    };

    private static final Function<Workbook, CellStyle> CELL_STYLE_NULL = workbook -> null;

    private final ApplicationRepository applicationRepository;
    private final StarterAnalyticsConfiguration configuration;

    public ExcelGenerator(ApplicationRepository applicationRepository, StarterAnalyticsConfiguration configuration) {
        this.applicationRepository = applicationRepository;
        this.configuration = configuration;
    }

    /**
     * Generates an Excel spreadsheet containing all the given applications.
     *
     * @return An Excel spreadsheet.
     */
    public Workbook generateExcel() {
        final Workbook workbook = new XSSFWorkbook();
        final Sheet sheet = workbook.createSheet(SHEET_NAME);
        int rownum = 0;
        // Headers
        final CellStyle locked = workbook.createCellStyle();
        locked.setLocked(true);
        final Row headers = sheet.createRow(rownum++);
        headers.setRowStyle(locked);
        for (ExcelColumn column : ExcelColumn.values()) {
            headers.createCell(column.ordinal(), CellType.STRING).setCellValue(column.title);
        }
        // Data
        Slice<Application> applications = applicationRepository.list(Pageable.from(0, configuration.getPageSize(), Sort.of(Sort.Order.asc("id"))));
        while (!applications.isEmpty()) {
            for (Application app : applications) {
                Row data = sheet.createRow(rownum++);
                for (ExcelColumn column : ExcelColumn.values()) {
                    final Cell cell = data.createCell(column.ordinal(), column.type);
                    column.setter.accept(app, cell);
                    Optional.ofNullable(column.styler.apply(workbook)).ifPresent(cell::setCellStyle);
                }
            }
            applications = applicationRepository.list(applications.nextPageable());
        }
        return workbook;
    }

    private enum ExcelColumn {
        DATE_CREATED("date_created", CellType.NUMERIC, date(Application::getDateCreated), CELL_STYLE_DATE_TIME),
        TYPE("type", CellType.STRING, symbol(Application::getType)),
        LANGUAGE("language", CellType.STRING, symbol(Application::getLanguage)),
        BUILD_TOOL("build_tool", CellType.STRING, symbol(Application::getBuildTool)),
        TEST_FRAMEWORK("test_framework", CellType.STRING, symbol(Application::getTestFramework)),
        JDK_VERSION("jdk_version", CellType.NUMERIC, jdkVersion(Application::getJdkVersion)),
        MICRONAUT_VERSION("micronaut_version", CellType.STRING, string(Application::getMicronautVersion)),
        ID("id", CellType.NUMERIC, numeric(Application::getId)),
        FEATURES("features", CellType.STRING, features(Application::getFeatures));

        final String title;
        final CellType type;
        final BiConsumer<Application, Cell> setter;
        final Function<Workbook, CellStyle> styler;

        ExcelColumn(String title, CellType type, BiConsumer<Application, Cell> setter, Function<Workbook, CellStyle> styler) {
            this.title = title;
            this.type = type;
            this.setter = setter;
            this.styler = styler;
        }

        ExcelColumn(String title, CellType type, BiConsumer<Application, Cell> setter) {
            this(title, type, setter, CELL_STYLE_NULL);
        }

        private static BiConsumer<Application, Cell> numeric(Function<Application, Number> getter) {
            return (a, c) -> Optional.ofNullable(getter.apply(a)).map(Number::doubleValue).ifPresent(c::setCellValue);
        }

        private static BiConsumer<Application, Cell> string(Function<Application, String> getter) {
            return (a, c) -> Optional.ofNullable(getter.apply(a)).ifPresent(c::setCellValue);
        }

        private static BiConsumer<Application, Cell> date(Function<Application, LocalDateTime> getter) {
            return (a, c) -> c.setCellValue(getter.apply(a));
        }

        private static BiConsumer<Application, Cell> jdkVersion(Function<Application, JdkVersion> getter) {
            return numeric(app -> Optional.ofNullable(getter.apply(app)).map(JdkVersion::majorVersion).orElse(null));
        }

        private static BiConsumer<Application, Cell> symbol(Function<Application, Enum<?>> getter) {
            return string(getter.andThen(Object::toString));
        }

        private static BiConsumer<Application, Cell> features(Function<Application, Set<Feature>> getter) {
            return string(app -> Optional.ofNullable(getter.apply(app)).stream().flatMap(Collection::stream).map(Feature::getName).collect(Collectors.joining(", ")));
        }
    }
}

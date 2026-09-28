/*
 * Copyright 2017-2026 original authors
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
package io.micronaut.starter.feature.build.pyronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.PythonSpecificFeature;
import io.micronaut.starter.template.StringTemplate;
import io.micronaut.starter.template.Template;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.pyronaut.agent.skills.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class PyronautAgentSkills implements DefaultFeature, PythonSpecificFeature {

    @Override
    @NonNull
    public String getName() {
        return "pyronaut-agent-skills";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addTemplate("pyronautProjectSkill", new StringTemplate(
                Template.ROOT,
                ".agents/skills/pyronaut-project/SKILL.md",
                pyronautProjectSkill()));
        generatorContext.addTemplate("pyronautCliSkill", new StringTemplate(
                Template.ROOT,
                ".agents/skills/pyronaut-cli/SKILL.md",
                pyronautCliSkill()));
        generatorContext.addTemplate("pyronautCodingSkill", new StringTemplate(
                Template.ROOT,
                ".agents/skills/pyronaut-coding/SKILL.md",
                pyronautCodingSkill()));
    }

    private static String pyronautProjectSkill() {
        return """
            ---
            name: pyronaut-project
            description: Understand and modify Pyronaut project structure, pyproject.toml metadata, dependency scopes, resources, generated files, and IDE schema/stub support.
            ---

            # Pyronaut Project

            Use this skill when working in a Python project generated for Pyronaut, especially when editing `pyproject.toml`, changing project layout, adding resources, or interpreting generated files under `__pyronaut__/`.

            ## Project Shape

            A Pyronaut project is identified by `pyproject.toml` with `[tool.pyronaut]` sections. The normal generated layout is:

            - `src/`: application Python sources.
            - `tests/`: pytest tests and JUnit 5 Python module tests.
            - `config/`: application resources, including `application.toml`.
            - `tests-config/`: test-only resources.
            - `__pyronaut__/`: generated dependency manifests, processed classes, IDE stubs, schemas, reports, and local caches. Do not edit this directory by hand.
            - `.micronaut/test-resources/`: test-resources client state when a reusable server is active.

            Pyronaut reads directory names from `[tool.pyronaut.sources]` in `pyproject.toml`. Directory values are relative to the project root. Keep generated projects on the default layout unless the user asks to reorganize them.

            ## pyproject.toml

            Keep the file structured around these sections:

            - `[project]`: Python package name, version, and metadata.
            - `[build-system]`: Python build backend metadata.
            - `[tool.pyronaut]`: repositories and top-level Pyronaut options.
            - `[tool.pyronaut.core]`: Micronaut Core version used by Pyronaut.
            - `[tool.pyronaut.platform]`: Micronaut platform version used for managed dependencies.
            - `[tool.pyronaut.sources]`: source and resource directories.
            - `[tool.pyronaut.processor]`: processing mode.
            - `[tool.pyronaut.ide-stubs]`: Java-backed Python stub generation.
            - `[tool.pyronaut.validation]`: lifecycle configuration validation.
            - `[tool.pyronaut.test-resources]`: test resources support when enabled.
            - `[tool.pyronaut.dependencies]`: runtime, build, and test dependency coordinates.

            Prefer kebab-case option names, such as `python-test`, `test-resources`, and `additional-modules`.

            ## Dependencies

            Add JVM dependency coordinates in `[tool.pyronaut.dependencies]`:

            - `runtime`: application runtime libraries, such as HTTP server, serde, data, security, cloud, messaging, views, and logging modules.
            - `build`: annotation processors and compile-time support, such as `micronaut-serde-processor`, `micronaut-security-processor`, or `micronaut-data-processor`.
            - `test`: pytest integration, JUnit 5 support, test clients, test-resources modules, and test-only libraries.
            - `development-runtime`: modules wanted while developing but not in a production run, such as development-only tooling.

            Use managed coordinates without versions when the Micronaut platform manages them. Add explicit versions only for unmanaged third-party artifacts.

            Every coordinate is resolved as a jar by default, so a POM-only artifact needs the four-part form `group:artifact:pom:version`. A three-part coordinate for an aggregator POM fails to resolve. When a POM-only coordinate is still rejected in a given scope, declare the concrete jars it aggregates instead.

            Run `pyronaut install` after changing dependencies, repositories, source layout, or IDE stub settings. It refreshes scoped manifests and local schema/stub support.

            ## Configuration

            Application configuration belongs in `config/application.toml`. Test-only configuration belongs in `tests-config/` or test environment files. Use TOML tables rather than flattened dotted keys when practical:

            ```toml
            [micronaut.application]
            name = "demo"
            ```

            Avoid `bootstrap.properties` and `bootstrap.toml`; Pyronaut projects should use normal application configuration and exclude features that require bootstrap configuration.

            Two configuration traps are worth knowing before they cost a debugging session:

            - **Do not let Test Resources resolve a property it does not own.** Its resolver re-enters itself and the stack overflows. It only bites beans built lazily during a request, so the application starts cleanly and fails later. Give such a property an explicit value in configuration rather than leaving it to be resolved.
            - **A JWT signature secret must be at least 256 bits** for HS256. A shorter one passes startup and fails only at login, reported as `Cannot obtain an access token`, which does not point at the secret.

            ## Generated Files

            `pyronaut install` creates local JSON schemas and schema directives for `pyproject.toml` and `application.toml` when those files exist. It also writes IDE stubs under `__pyronaut__/ide-stubs` so imports such as `micronaut.http.annotation` and `jakarta.inject` resolve in editors. Treat those files as generated output and rerun `pyronaut install` instead of editing them.
            """;
    }

    private static String pyronautCliSkill() {
        return """
            ---
            name: pyronaut-cli
            description: Run, test, validate, install, process, and troubleshoot Pyronaut projects with the pyronaut CLI and its delegated toolchain, including reading test reports and clearing stale test-resources state.
            ---

            # Pyronaut CLI

            Use this skill when running Pyronaut commands, validating configuration, debugging generated manifests, or working with test resources.

            ## Command Model

            The `pyronaut` command is a Python orchestrator. It delegates to focused tools for install, processing, run, test, native build, config validation, and test-resources server management.

            From the project root, `--project-dir .` is unnecessary because the CLI defaults to the current directory.

            ## Common Workflow

            Use this sequence after creating or changing a project:

            ```bash
            pyronaut install
            pyronaut validate-config
            pyronaut process
            pyronaut test
            ```

            Use `pyronaut run` to launch the application.

            ## Commands

            - `pyronaut install`: resolves dependencies, writes scoped manifests under `__pyronaut__/`, materializes TOML schemas, and generates IDE stubs.
            - `pyronaut process`: processes `src/` and `tests/` sources into `__pyronaut__/classes` and `__pyronaut__/test-classes`.
            - `pyronaut run`: validates config for the `run` scenario, performs install/process preflight, starts the app, and manages test resources when enabled.
            - `pyronaut test`: validates config for the `test` scenario, performs install/process preflight, runs the configured JUnit and/or pytest engines, and writes reports under `__pyronaut__/reports/tests`.
            - `pyronaut test <file.py>`: directly compiles and runs a Python test module in memory. Direct execution currently supports JUnit 5 modules only; pytest tests require files on disk and must be run with project-mode `pyronaut test`.
            - `pyronaut validate-config --scenario run|test|production`: validates lifecycle configuration for a specific scenario and writes reports under `__pyronaut__/reports/config-validation/<scenario>`.
            - `pyronaut test-resources-server start|status|stop`: manages a reusable test-resources server. `run` and `test` can also start an owned server automatically when test resources are enabled.

            `pyronaut run` and `pyronaut test` validate by default. Use `--no-validate` only for short diagnostics where validation itself is the blocker.

            ## Compiling Is Not Running

            `pyronaut process` catches type errors, unresolvable annotations and bad generics, and reports them with a file and line. It does not catch a wrong import, a circular import, a missing bean, or an annotation argument that was silently dropped. Those need a running context.

            So a green `pyronaut process` is not evidence that anything works. Run `pyronaut run` or `pyronaut test` before reporting a change as done.

            ## Read The Test Report, Not The Exit Code

            A test task that runs zero tests still exits zero, so an exit code cannot distinguish a passing suite from a suite that never ran. Read the count out of the report:

            ```bash
            pyronaut test
            grep -o 'tests="[0-9]*"' __pyronaut__/reports/tests/junit.xml
            ```

            Check failures, errors and skips too. A suite whose tests were all skipped is a suite that proved nothing, and that is the shape a misconfigured engine or a missing prerequisite takes.

            ## Troubleshooting

            - If imports from Micronaut or Jakarta packages fail in the editor, run `pyronaut install` so `__pyronaut__/ide-stubs` and editor settings are refreshed.
            - If dependency changes are not reflected, run `pyronaut install --refresh`.
            - If cache state looks corrupt, run `pyronaut install --no-cache`.
            - For delegation diagnostics, set `PYRONAUT_TRACE_DELEGATION=true`.
            - Do not edit generated manifests, processed classes, schemas, or reports under `__pyronaut__/`; change source files or `pyproject.toml` and rerun the relevant command.
            - If a start fails with `Test resource service is not available`, a previous run was killed and left client state behind. Clear `.micronaut/test-resources` and `__pyronaut__/test-resources-session.json`, then start again.
            - The CLI launches the application in a separate JVM, so killing the CLI process by name does not necessarily free the HTTP port. A later start then fails with `BindException: Address already in use`, and anything testing against that port is talking to the previous process without any sign of it. Stop it by port instead, for example `lsof -ti :8080 | xargs kill -9`.
            - When a dependency change is not picked up, remember that `pyronaut install` resolves dependencies but the tool runtime is cached separately; a stale cache is what `--refresh` and `--no-cache` are for.
            """;
    }

    private static String pyronautCodingSkill() {
        return """
            ---
            name: pyronaut-coding
            description: Write idiomatic Pyronaut application code with typed Python, dataclasses, classless route modules, Micronaut annotations, Java-backed imports, dependency injection, bean mappers, JUnit 5 Python modules, pytest tests, and reflection-free JSON/data patterns. Use when an annotation, import or constraint that looks correct has no effect at runtime.
            ---

            # Pyronaut Coding

            Use this skill when adding or changing Python application code in a Pyronaut project.

            ## Source Style

            - Write typed Python. Add parameter and return type annotations for route handlers, services, clients, repositories, DTOs, entities, configuration objects, and tests.
            - Prefer Python dataclasses for structured data: request bodies, response models, DTOs, configuration properties, JSON schema inputs, and data entities.
            - Keep required values non-optional. Use `None` and `| None` only for values that are genuinely optional or framework-populated, such as generated IDs and timestamps.
            - Use `@Serdeable` on dataclasses that cross JSON or HTTP boundaries. Add `@JsonSchema` when schema generation is part of the feature.
            - Use `@MappedEntity` plus `typing.Annotated` metadata for Micronaut Data entities.
            - Keep module names snake_case under `src/<package>/`.

            ## Annotations Are Read From Source, Not Evaluated

            The processor reads annotation arguments from the source text. An argument it cannot read is discarded **without a warning**, so the code compiles, starts and serves with the constraint or default simply missing.

            A bare name for a scalar argument does resolve, which is what makes the other two forms easy to reach for:

            ```python
            # WRONG -- the constraint disappears and the endpoint accepts anything
            PASSWORD = Size(min=8, max=128)
            password: Annotated[str, NotBlank, PASSWORD]

            # WRONG -- the default is dropped and the parameter is published as required
            size: Annotated[int, QueryValue(defaultValue=str(DEFAULT_SIZE))] = DEFAULT_SIZE

            # RIGHT -- repetitive, and correct
            password: Annotated[str, NotBlank, Size(min=8, max=128)]
            size: Annotated[int, QueryValue(defaultValue="100")] = DEFAULT_SIZE
            ```

            Write every annotation argument out as a literal. Do not factor an annotation into a constant and do not compute an argument. Add a test asserting the constraint rejects bad input, because nothing else will reveal a dropped one.

            ## Controllers And Beans

            Prefer classless route modules for simple HTTP routes:

            ```python
            from typing import Annotated

            from jakarta.inject import Inject
            from micronaut.http.annotation import Body, Get, Post

            from .services import MessageService, Person

            message_service: Annotated[MessageService, Inject]

            @Get(value="/hello/{name}", produces="application/json")
            def hello(name: str) -> dict[str, str]:
                return {"message": message_service.say_hello(name)}

            @Post(value="/hello")
            def create(person: Annotated[Person, Body]) -> Person:
                return person
            ```

            A type-level annotation is written as a bare call at module scope, which is how a route module takes the annotations a controller class would carry. A module with routes and no `Controller` call is mounted at `/`:

            ```python
            Controller("/api/v1/items")
            Secured(SecurityRule.IS_AUTHENTICATED)
            Requires(env=["dev"])

            items: Annotated[ItemService, Inject]
            ```

            Move business logic into injected services. Use classes when the Micronaut feature requires a type, such as clients, filters, repositories, configuration properties, MCP tools, or framework interfaces.

            Constructor injection is preferred for class beans. Module-level injection with `Annotated[Type, Inject]` is idiomatic for classless route modules. A route module has no constructor, so when several routes need the same derived value, put it in an injected bean rather than recomputing it per handler.

            Use `@EventListener` for startup work rather than implementing a framework listener interface. It takes the event from the parameter type:

            ```python
            from micronaut.context.event import StartupEvent
            from micronaut.runtime.event.annotation import EventListener

            @Singleton
            class Bootstrap:
                @EventListener
                def on_startup(self, event: StartupEvent) -> None:
                    ...
            ```

            Operation ids must be unique across the whole API. A collision is not an error: Micronaut OpenAPI appends a number, and a generated client grows a `signup1` whose digit depends on declaration order. Give a page handler and an API handler for the same concept distinct function names.

            ## Imports

            Import Java-backed Micronaut APIs through the generated Python package names:

            - Use `from micronaut.http.annotation import Get`, not `from io.micronaut.http.annotation import Get`.
            - Use `from micronaut.serde.annotation import Serdeable`.
            - Use `from jakarta.inject import Singleton` or `Inject`.
            - Use `import java` and `java.type("fully.qualified.JavaType")` when a Java class is not exposed as a Python stub or needs an exact JVM type.

            The Java `io.micronaut` package is exposed as the Python package `micronaut` because Python already has a standard `io` module. When a Java package segment conflicts with a Python keyword, use the generated trailing-underscore package segment, such as `from micronaut.core.async_.annotation import SingleResult`. The same escape applies to a member whose name is a Python keyword, so `Pageable.from(page, size)` is written `Pageable.from_(page, size)`.

            Reach a nested Java annotation or enum through the type that declares it, since there is no top-level type of its own: `@Mapper.Mapping(...)`, not `Mapping`, and `Introspected.AccessKind`, not `AccessKind`.

            ### Find the module before guessing it

            The package mapping tells you the package but not which module a class ended up in. `__pyronaut__/ide-stubs/` is generated from the real classpath and is the authoritative answer, so grep it:

            ```bash
            grep -rn "class StartupEvent" __pyronaut__/ide-stubs/
            ```

            `StartupEvent` is in `micronaut.context.event` rather than `micronaut.runtime.event`, while the `@EventListener` that consumes it is in `micronaut.runtime.event.annotation` -- neighbouring concepts in different modules, which is exactly the case guessing gets wrong. A wrong import compiles and fails at context startup with `ModuleNotFoundError`, usually reported against whichever bean was being constructed rather than the module holding the bad import.

            ### Packages import eagerly, so keep dependencies one-directional

            Do not write your own `__init__.py`. Micronaut generates them for the GraalPy VFS and rejects custom ones.

            A generated `__init__.py` imports **every** module in its package, eagerly. Two packages that import from each other therefore deadlock at startup even when the individual modules would be fine. Establish a direction and hold it: let `controllers` and `security` import `services`, and have `services` import neither. Put a helper both sides need at the top level rather than inside one of them.

            ## Dependencies And Unsupported Patterns

            Add Micronaut and Java dependencies in `pyproject.toml`, not Gradle or Maven build files. Put annotation processors in the `build` dependency scope.

            Avoid Java reflection-dependent features in Pyronaut code. In particular, do not use `micronaut-jackson-databind`, `hibernate-jpa`, or `hibernate-validator` as implementation shortcuts. Prefer Micronaut Serialization, typed dataclasses, compile-time data/introspection support, and Pyronaut-compatible validation.

            ## Data And Projections

            Annotate an entity id with the Python type. `uuid.UUID` maps to `java.util.UUID`, and an id read back off an entity is an ordinary Python `uuid.UUID` that a repository accepts, so no conversion is needed on the way into a query.

            Map an entity to a DTO with a compile-time bean mapper rather than by hand. Declare it as a `Protocol` and let the processor generate the implementation, which also converts the field types the DTO declares:

            ```python
            @Singleton
            class ItemMapper(Protocol):
                @Mapper.Mapping(to="ownerId", from_="#{item.owner.id}")
                @Mapper
                def to_public(self, item: Item) -> ItemPublic: ...
            ```

            ## Errors

            An `ExceptionHandler` type parameter must be a Java `Throwable`; a Python exception class cannot be used. Catch Python exceptions in the route and convert them, and keep one error response shape across the API so the contract lives in a single handler.

            ## Tests

            Write normal pytest tests under `tests/`. Use `pyronaut.test` fixtures when a Micronaut application context is needed, and use the Pyronaut `requests` integration for HTTP requests when available.

            For tests that must be run directly (for example, `pyronaut test test_main.py`), write a JUnit 5 Python module. Do not wrap the tests in a class, use `self`, add a `@Test` decorator, or add a `-> None` return annotation. Import and call `MicronautTest()` at module scope, then define ordinary `test_*` functions:

            ```python
            from typing import Annotated

            from jakarta.inject import Inject
            from micronaut.context import ApplicationContext
            from micronaut.test.extensions.junit5.annotation import MicronautTest

            MicronautTest()
            context: Annotated[ApplicationContext, Inject]

            def test_root():
                assert context is not None
            ```

            JUnit modules use JUnit lifecycle and dependency-injection semantics (`@BeforeAll`, `@BeforeEach`, and Micronaut injection). Pytest modules use pytest fixtures and hooks. Keep the styles separate; a pytest module cannot be executed through direct in-memory source execution.

            A JUnit-Python lifecycle method is an instance method, so a module using `@BeforeAll` or `@AfterAll` must also declare `TestInstance(TestInstance.Lifecycle.PER_CLASS)` at module scope. Without it JUnit refuses to discover the module at all, reporting that the method must be static.

            Do not write a test that depends on data another test can change. With a reusable test-resources server the database also outlives the run, so generate unique values rather than relying on a clean table, and assert on shape rather than on a specific row another test may rename.

            Put the evidence in the assertion message. A bare `assert response.status() == 200` reports nothing useful, and the server-side exception goes to the test report rather than the console:

            ```python
            assert response.status() == 200, f"GET /login returned {response.status()}: {page.content()[:600]}"
            ```

            Run:

            ```bash
            pyronaut test
            ```

            For configuration-sensitive changes, also run:

            ```bash
            pyronaut validate-config --scenario test
            ```
            """;
    }
}

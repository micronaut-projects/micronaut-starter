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
package io.micronaut.starter.cli.feature;

import io.micronaut.starter.application.Project;
import io.micronaut.starter.template.StringTemplate;

public final class PythonTemplates {

    private PythonTemplates() {
    }

    public static StringTemplate repository(Project project, boolean jdbcRepository, String dialect) {
        String annotation;
        String annotationImport;
        if (jdbcRepository) {
            annotation = dialect == null ? "@JdbcRepository" : "@JdbcRepository(dialect=\"" + dialect + "\")";
            annotationImport = "from micronaut.data.jdbc.annotation import JdbcRepository\n";
        } else {
            annotation = "@Repository";
            annotationImport = "from micronaut.data.annotation import Repository\n";
        }
        String entityType = project.getPackageName() + "." + project.getClassName();
        String className = project.getClassName() + "Repository";
        return new StringTemplate(path(project, "Repository"), """
            import java

            %sfrom micronaut.data.repository import CrudRepository

            Entity = java.type("%s")

            %s
            class %s(CrudRepository[Entity, int]):
                pass
            """.formatted(annotationImport, entityType, annotation, className));
    }

    public static StringTemplate jmsProducer(Project project, String configClass) {
        return jms(project, configClass, "JMSProducer", "send");
    }

    public static StringTemplate jmsConsumer(Project project, String configClass) {
        return jms(project, configClass, "JMSListener", "receive");
    }

    private static StringTemplate jms(Project project, String configClass, String annotation, String method) {
        String className = project.getClassName();
        return new StringTemplate(path(project, ""), """
            from abc import ABC, abstractmethod
            from typing import Annotated

            import java
            from micronaut.jms.annotations import %s, Queue
            from micronaut.messaging.annotation import MessageBody

            Configuration = java.type("%s")

            @%s(Configuration.CONNECTION_FACTORY_BEAN_NAME)
            class %s(ABC):

                @Queue("demo_queue")
                @abstractmethod
                def %s(self, body: Annotated[str, MessageBody]) -> None:
                    ...
            """.formatted(annotation, configClass, annotation, className, method));
    }

    public static StringTemplate mqttPublisher(Project project, String version) {
        return mqtt(project, version, "MqttPublisher", "publish");
    }

    public static StringTemplate mqttSubscriber(Project project, String version) {
        return mqtt(project, version, "MqttSubscriber", "receive");
    }

    private static StringTemplate mqtt(Project project, String version, String annotation, String method) {
        return new StringTemplate(path(project, ""), """
            from abc import ABC, abstractmethod

            from micronaut.mqtt.annotation import Topic
            from micronaut.mqtt.annotation.%s import %s

            @%s
            class %s(ABC):

                @Topic("topic")
                @abstractmethod
                def %s(self, data: bytes) -> None:
                    ...
            """.formatted(version, annotation, annotation, project.getClassName(), method));
    }

    public static StringTemplate client(Project project) {
        return new StringTemplate(path(project, "Client"), """
            from abc import ABC, abstractmethod

            from micronaut.http.annotation import Get
            from micronaut.http.client.annotation import Client

            @Client
            class %sClient(ABC):

                @Get("/")
                @abstractmethod
                def get(self) -> str:
                    ...
            """.formatted(project.getClassName()));
    }

    public static StringTemplate websocketServer(Project project) {
        return new StringTemplate(path(project, "Server"), """
            from micronaut.websocket import WebSocketBroadcaster, WebSocketSession
            from micronaut.websocket.annotation import OnClose, OnMessage, OnOpen, ServerWebSocket

            @ServerWebSocket("/ws/{topic}/{username}")
            class %sServer:

                def __init__(self, broadcaster: WebSocketBroadcaster):
                    self.broadcaster = broadcaster

                @OnOpen
                def on_open(self, topic: str, username: str, session: WebSocketSession) -> None:
                    self.broadcaster.broadcastSync(f"[{username}] Joined {topic}!", lambda _: True)

                @OnMessage
                def on_message(self, topic: str, username: str, message: str, session: WebSocketSession) -> None:
                    self.broadcaster.broadcastSync(f"[{username}] {message}", lambda _: True)

                @OnClose
                def on_close(self, topic: str, username: str, session: WebSocketSession) -> None:
                    self.broadcaster.broadcastSync(f"[{username}] Leaving {topic}!", lambda _: True)
            """.formatted(project.getClassName()));
    }

    public static StringTemplate websocketClient(Project project) {
        return new StringTemplate(path(project, "Client"), """
            from abc import ABC, abstractmethod

            from micronaut.websocket.annotation import ClientWebSocket, OnMessage

            @ClientWebSocket("/ws/{topic}/{username}")
            class %sClient(ABC):

                @OnMessage
                def on_message(self, message: str) -> None:
                    pass

                @abstractmethod
                def send(self, message: str) -> None:
                    ...
            """.formatted(project.getClassName()));
    }

    private static String path(Project project, String suffix) {
        return "src/" + project.getPackagePath() + "/" + project.getClassName() + suffix + ".py";
    }
}

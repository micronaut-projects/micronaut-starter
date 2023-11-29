package io.micronaut.starter.feature.chatbots

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture

abstract class BaseChatBotSpec extends ApplicationContextSpec implements CommandOutputFixture {

    abstract Class<ChatBotsFeature> getFeature()

    abstract String getFeatureName()
}

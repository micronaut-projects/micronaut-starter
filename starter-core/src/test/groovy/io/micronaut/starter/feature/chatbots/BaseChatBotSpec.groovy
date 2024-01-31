package io.micronaut.starter.feature.chatbots

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options

abstract class BaseChatBotSpec extends ApplicationContextSpec implements CommandOutputFixture {

    abstract List<ApplicationType> getSupportedApplicationTypes()

    abstract Class<ChatBotsFeature> getFeature()

    abstract String getFeatureName()
}

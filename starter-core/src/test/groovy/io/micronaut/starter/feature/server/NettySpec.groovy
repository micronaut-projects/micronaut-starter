package io.micronaut.starter.feature.server

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture

class NettySpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test default netty server contains netty config for max order'() {
        when:
        Map<String, String> output = generate(['yaml'])
        String config = output["src/main/resources/application.yml"]

        then:
        config
        config.contains('''\
netty:
  default:
    allocator:
      max-order: 3''')
    }

}


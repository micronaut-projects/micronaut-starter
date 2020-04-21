package foo

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("foo")
                .mainClass(Application.javaClass)
                .start()
    }
}

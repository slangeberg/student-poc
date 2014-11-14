package demo

import spock.lang.Specification

/**
 * Created with love, on 11/13/14.
 */
class StudentSpec extends Specification {

    def "can test"() {
       Student student = new Student()

        expect:
        student
    }
}

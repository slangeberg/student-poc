package demo

import grails.plugin.spock.IntegrationSpec
import groovyx.gpars.GParsPool
import org.hibernate.StaleObjectStateException
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import spock.lang.Timeout

import java.util.concurrent.TimeUnit

/**
 * Created with love, on 11/13/14.
 */
class StudentIntSpec extends IntegrationSpec {

    static transactional = false //need data across threads

    StudentService studentService

    @Timeout(value=120, unit=TimeUnit.MILLISECONDS)
    def "can test"() {
        Student student = new Student(username: 'test-guy', status: 1)
        student.save(flush: true, failOnError: true)

        println "student.id: " + student.id

        when:
        Student.load(student.id)

        then:
        notThrown(StaleObjectStateException)
    }

    def "Concurrent update fails"() {

        final Student student = new Student(username: 'test-guy', status: -1)
        student.save(flush: true, failOnError: true)

        final long targetId = student.id

        println "student.id: $targetId"

        when:
        GParsPool.withPool {
            (1..10).eachParallel { int index ->
                studentService.update(targetId, index)
            }
        }

        then:
        thrown(Exception) //i've seen three types thrown, so far ;)
    }

    @Timeout(value=120, unit=TimeUnit.MILLISECONDS)
    def "Concurrent update with lock"() {

        final Student student = new Student(username: 'test-guy', status: -1)
        student.save(flush: true, failOnError: true)

        final long targetId = student.id

        println "student.id: $targetId"

        when:
        GParsPool.withPool {
            (1..10).eachParallel { int index ->
                studentService.updateWithLock(targetId, index)
            }
        }

        then:
        notThrown(HibernateOptimisticLockingFailureException)
    }

    @Timeout(value=120, unit=TimeUnit.MILLISECONDS)
    def "Concurrent update with transaction lock"() {

        final Student student = new Student(username: 'test-guy', status: -1)
        student.save(flush: true, failOnError: true)

        final long targetId = student.id

        println "student.id: $targetId"

        when:
        GParsPool.withPool {
            (1..10).eachParallel { int index ->
                studentService.updateWithTransactionLock(targetId, index)
            }
        }

        then:
        notThrown(HibernateOptimisticLockingFailureException)
    }
}

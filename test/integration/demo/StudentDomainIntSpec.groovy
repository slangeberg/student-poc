package demo

import grails.plugin.spock.IntegrationSpec
import spock.lang.Shared

/**
 * Generated with love, on 12/19/14.
 */
class StudentDomainIntSpec extends IntegrationSpec {

    @Shared
    Student student1, student2, student3, orphan

    def setupSpec() {
        student1 = new Student(name: 'student1')
            .save(failOnError: true, flush:true)
        student2 = new Student(name: 'student2', survey: new Survey(
            items: [
                new SurveyItem(
                    attributes: [
                        new ItemAttribute(key: 'myKey', value: 'myVal'),
                        new ItemAttribute(key: 'myKey2', value: 'myVal2')
                    ]
                )
            ]
        ))
            .save(failOnError: true, flush:true)
        student3 = new Student(name: 'student3', survey: new Survey(
            items: [
                new SurveyItem(
                    attributes: [
                        new ItemAttribute(key: 'myKey', value: 'student3Val'),
                        new ItemAttribute(key: 'myKey2', value: 'student3Val2'),
                        new ItemAttribute(key: 'anotherKey', value: 'student3Val3')
                    ]
                )
            ]
        ))
            .save(failOnError: true, flush:true)
        //no profile
        orphan = new Student(name: 'orphan1')
            .save(failOnError: true, flush:true)

        // AP Type
        new Profile(student: student1, type: 'AP')
            .save(failOnError: true, flush:true)

        // RP type
        new Profile(student: student2, type: 'RP')
            .save(failOnError: true, flush:true)
        new Profile(student: student3, type: 'RP')
            .save(failOnError: true, flush:true)

        // no student
        new Profile(type: 'BAD')
            .save(failOnError: true, flush:true)
    }

    def "Can find students"() {
        expect:
        Student.count() == 4
    }

    def "Can find profiles"() {
        expect:
        Profile.count() == 4
    }

    def "Can find profile by type"() {
        List p = Profile.where {
            type == 'AP'
        }
        .list()

        expect:

        p.size() == 1
        p[0].student.id == student1.id
    }

    def "Can find any profile with student"() {
        List<Profile> p = Profile.where {
            student {}
        }
        .list()
        List<Student> s = p.collect { it.student }

        expect:
        s.size() == 3
        s.find { it.id ==  student1.id }
        s.find { it.id ==  student2.id }
        s.find { it.id ==  student3.id }
    }

    def "Can find student with profile type and has survey items"() {
        List<Profile> p = Profile.where {
            type == 'RP' && student.survey {
                items.size() > 0
            }

        }
        .list()
        List<Student> s = p.collect { it.student }

        expect:
        s.size() == 2
        s.find { it.id ==  student2.id }
        s.find { it.id ==  student3.id }
    }

    def "Can find student with profile type and item key"() {
        List<Profile> p = Profile.where {
            type == 'RP' && student.survey {
                items {
                    attributes {
                        key == 'myKey'
                    }
                }
            }

        }
        .list()
        List<Student> s = p.collect { it.student }

        expect:
        s.size() == 2
        s.find { it.id ==  student2.id }
        s.find { it.id ==  student3.id }
    }

    def "Can find student with profile type and item key/value pair"() {
        List<Profile> p = Profile.where {
            type == 'RP' && student.survey {
                items {
                    attributes {
                        key == 'myKey' && value == 'student3Val'
                    }
                }
            }

        }
        .list()
        List<Student> s = p.collect { it.student }

        expect:
        s.size() == 1
        s.find { it.id ==  student3.id }
    }
}

package demo

import grails.gorm.DetachedCriteria
import grails.plugin.spock.IntegrationSpec
import org.hibernate.criterion.CriteriaSpecification
import spock.lang.Ignore
import spock.lang.Shared

/**
 * Generated with love, on 12/19/14.
 */
class StudentDomainIntSpec extends IntegrationSpec {

    @Shared
    Student student1, student2, student3, student3AP, student4, orphan

    def setupSpec() {
        student1 = new Student(username: 'student1')
            .save(failOnError: true, flush: true)

        student2 = new Student(username: 'student Webb',
            survey: new Survey(
                items: [
                    new SurveyItem(
                        attributes: [
                            new ItemAttribute(key: 'studiedHard', value: 'true'),
                            new ItemAttribute(key: 'myKey', value: 'student1Val'),
                            new ItemAttribute(key: 'myKey2', value: 'student1Val')
                        ]
                    )
                ]
            ))
            .save(failOnError: true, flush: true)
        student3 = new Student(username: 'student3',
            survey: new Survey(
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
            .save(failOnError: true, flush: true)
        student3AP = new Student(username: 'student3AP',
            survey: new Survey(
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
            .save(failOnError: true, flush: true)
        student4 = new Student(username: 'student Weber',
            survey: new Survey(
                items: [
                    new SurveyItem(
                        attributes: [
                            new ItemAttribute(key: 'studiedHard', value: 'false'),
                            new ItemAttribute(key: 'myKey', value: 'student4Val'),
                            new ItemAttribute(key: 'myKey2', value: 'student4Val2'),
                            new ItemAttribute(key: 'aNewKey', value: 'student4Val3')
                        ]
                    )
                ]
            ))
            .save(failOnError: true, flush: true)

        //no profile
        orphan = new Student(username: 'orphan1')
            .save(failOnError: true, flush: true)

        // AP Type
        new Profile(student: student1, type: 'AP')
            .save(failOnError: true, flush: true)
        new Profile(student: student3AP, type: 'AP')
            .save(failOnError: true, flush: true)

        // RP type
        new Profile(student: student2, type: 'RP')
            .save(failOnError: true, flush: true)
        new Profile(student: student3, type: 'RP')
            .save(failOnError: true, flush: true)
        new Profile(student: student4, type: 'RP')
            .save(failOnError: true, flush: true)

        // no student
        new Profile(type: 'BAD')
            .save(failOnError: true, flush: true)
    }

    def "Can find students"() {
        expect:
        Student.count() == 6
    }

    def "Can find profiles"() {
        expect:
        Profile.count() == 6
    }

    def "Can find profile by type"() {
        List<Profile> p = Profile.where {
            type == 'AP'
        }
        .list()
        List<Student> s = p.collect { it.student }

        expect:
        p.size() == 2
        s.size() == 2
        s.find { it.id == student1.id }
        s.find { it.id == student3AP.id }
    }

    def "Can find any profile with student"() {
        List<Profile> p = Profile.where {
            student {}
        }
        .list()
        List<Student> s = p.collect { it.student }

        expect:
        s.size() == 5
        s.find { it.id == student1.id }
        s.find { it.id == student2.id }
        s.find { it.id == student3.id }
        s.find { it.id == student3AP.id }
        s.find { it.id == student4.id }
    }

    def "Can find student with profile type and has survey items"() {
        List<Profile> p = Profile.where {
            type == 'RP'
            student {
                survey {
                    items {
                        attributes.size() > 3
                    }
                }
            }
        }
        .list()
        List<Student> s = p.collect { it.student }

        expect:
        s.size() == 1
        s.find { it.id == student4.id }
    }

    def "Can rule out student with profile type and bad item key"() {
        List<Profile> p = Profile.where {
            type == 'RP'
            student {
                survey {
                    items {
                        attributes {
                            key == 'fake'
                        }
                    }
                }
            }
        }
        .list()

        expect:
        p.size() == 0
    }

    def "Can find student with profile type and item key/value pair"() {
        List<Profile> p = Profile.where {
            type == 'RP'
            student {
                survey {
                    items {
                        attributes {
                            key == 'myKey' && value == 'student3Val'
                        }
                    }
                }
            }
        }
        .list()
        List<Student> s = p.collect { it.student }

        expect:
        s.size() == 1
        s.find { it.id == student3.id }
    }

    def "Can find student with profile type and item key/value pair - With Criteria Builer"() {
        List<Profile> p = Profile.createCriteria().list {
            eq 'type', 'RP'
            student {
                survey {
                    items {
                        attributes {
                            and {
                                eq 'key', 'myKey'
                                eq 'value', 'student3Val'
                            }
                        }
                    }
                }
            }
        }
        List<Student> s = p.collect { it.student }

        expect:
        s.size() == 1
        s.find { it.id == student3.id }
    }

    def "Can find student by profile type and name"() {
        List<Profile> p = Profile.where {
            type == 'RP'
            student {
                username =~ '%web%'
            }
        }
        .list()
        List<Student> s = p.collect { it.student }

        expect:
        s.size() == 2
        s.find { it.id == student2.id }
        s.find { it.id == student4.id }
    }

    def "Can find student by profile type, name, and item criteria"() {
        List<Profile> p = Profile.where {
            type == 'RP'
            student {
                username =~ '%web%'
                survey {
                    items {
                        attributes {
                            key == 'aNewKey' && value == 'student4Val3'
                        }
                    }
                }
            }
        }
        .list()
        List<Student> s = p.collect { it.student }

        expect:
        s.size() == 1
        s.find { it.id == student4.id }
    }

    @Ignore("Still suffering from inner join issue as seen in original SQL!")
    def "Can find student by profile type, name, and multiple item criteria"() {
//        {
//        key == 'aNewKey' && value == 'student4Val3'
//        key == 'studiedHard' && value == 'false'
//    }
//                        attributes new DetachedCriteria(ItemAttribute).build {
//                            eq 'key', 'studiedHard'
//                        }
        List<Profile> p = Profile.where {
            type == 'RP'
            student {
                username =~ '%web%'
                survey {
                    items {
                        attributes {
                            property(id).of {
                                key == 'aNewKey' && value == 'student4Val3'
                            }
                        }
                    }
                }
            }
        }
        .list()
        List<Student> s = p.collect { it.student }

        expect:
        s.size() == 1
        s.find { it.id == student4.id }
    }

    def "Can find student by profile type, name, and multiple item criteria - With Criteria builder"() {
//        {
//        key == 'aNewKey' && value == 'student4Val3'
//        key == 'studiedHard' && value == 'false'
//    }
//                        attributes new DetachedCriteria(ItemAttribute).build {
//                            eq 'key', 'studiedHard'
//                        }
//        List<Profile> p = Profile.where {
//            type == 'RP'
//            student {
//                username =~ '%web%'
//                survey {
//                    items {
//                        attributes {
//                            property(id).of {
//                                key == 'aNewKey' && value == 'student4Val3'
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        .list()
        List<Profile> p = Profile.createCriteria().list {
            eq 'type', 'RP'
            student {
                survey {
                    and {
                        items {
                            attributes {
                                and {
                                    eq 'key', 'aNewKey'
                                    eq 'value', 'student4Val3'
                                }
                            }
                        }
                        items {
                            attributes {
                                and {
                                    eq 'key', 'studiedHard'
                                    eq 'value', 'false'
                                }
                            }
                        }
                    }
                }
            }
        }
        List<Student> s = p.collect { it.student }

        expect:
        s.size() == 1
        s.find { it.id == student4.id }
    }
}

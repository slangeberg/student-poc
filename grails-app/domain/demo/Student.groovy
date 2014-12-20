package demo

class Student {

    static constraints = {
        status nullable: true
        survey nullable: true
    }

    String name
    Integer status

//    Profile profile
    Survey survey
}

package demo

class Student {

    static constraints = {
        status nullable: true
        survey nullable: true
    }

    String username
    Integer status

//    Profile profile
    Survey survey
}

package demo

/**
 * Generated with love, on 12/19/14.
 */
class Profile {
    static belongsTo = [student: Student]
    static constraints = {
        student nullable: true
    }

    String type
}

import demo.Student
import grails.util.Environment

class BootStrap {

    def init = { servletContext ->
       if( Environment.DEVELOPMENT == Environment.current ){
          if(Student.count() == 0){
             [
                new Student(username: "Bruce Almighty", status: 1),
                new Student(username: "Devin Notsomuch", status: 1)

             ]*.save(flush: true, failOnError: true)
          }
       }
    }
    def destroy = {
    }
}

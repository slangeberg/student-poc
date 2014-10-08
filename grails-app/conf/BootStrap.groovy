import demo.Student
import grails.util.Environment

class BootStrap {

    def init = { servletContext ->
       if( Environment.DEVELOPMENT == Environment.current ){
          if(Student.count() == 0){
             [
                new Student(name: "Bruce Almighty"),
                new Student(name: "Devin Notsomuch")

             ]*.save(flush: true, failOnError: true)
          }
       }
    }
    def destroy = {
    }
}

package demo

class StudentController {

   static scaffold = true

   def test() {
      render "hi!"
   }

   def all() {
      [students: [new Student(name: 'Bruce')]]
   }
}

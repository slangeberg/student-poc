package demo

import grails.converters.JSON

class StudentController {

   static scaffold = true

   static defaultAction = "all"

   def test() {
      render "hi!"
   }

   def all() {
      [students: [new Student(name: 'Bruce'), new Student(id:-1, name: "Uh oh!!")]]
   }

   // Cheap REST GET
   def data() {
      if( params.id ){
         render Student.get(params.getLong('id')) as JSON
      } else {
         render Student.list() as JSON
      }
   }
}
package demo

import grails.converters.JSON

class StudentController {

   static scaffold = true

   static defaultAction = "all"

   def test() {
      render "hi!"
   }

   def all() {
      [students: Student.list()]
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
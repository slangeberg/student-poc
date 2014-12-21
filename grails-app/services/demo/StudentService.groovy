package demo

class StudentService {

    Student update(long id, int index) {

        Student student = Student.get(id)

        println "StudentService.update() - index: $index, targetId: $id"

        student.username = "conc-$index"
        student.status = index % 2 == 0 ? 0 : 1

        println "update() { $index, name: ${student.username}, status: ${student.status} }"

        student.save(flush: true, failOnError: true)
        student
    }

    Student updateWithLock(long id, int index) {

        Student student = Student.lock(id)

        log.info "StudentService.updateWithLock() - index: $index, targetId: $id"

        student.username = "conc-$index"
        student.status = index % 2 == 0 ? 0 : 1

        log.info "updateWithLock() { $index, name: ${student.username}, status: ${student.status} }"

        student.save(flush: true, failOnError: true)
        student
    }

    Student updateWithTransactionLock(long id, int index) {
        Student.withTransaction {
            Student student = Student.lock(id)

            println "StudentService.updateWithTransactionLock() - index: $index, targetId: $id"

            student.username = "conc-$index"
            student.status = index % 2 == 0 ? 0 : 1

            println "updateWithTransactionLock() { $index, name: ${student.username}, status: ${student.status} }"

            student.save(flush: true, failOnError: true)
            student
        }
    }
}

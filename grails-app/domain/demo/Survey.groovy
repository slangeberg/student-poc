package demo

/**
 * Generated with love, on 12/19/14.
 */
class Survey {
    static belongsTo = Student

    static hasMany = [items: SurveyItem]
}

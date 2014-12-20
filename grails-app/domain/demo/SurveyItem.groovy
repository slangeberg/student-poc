package demo

/**
 * Generated with love, on 12/19/14.
 */
class SurveyItem {


    static belongsTo = Survey

    static hasMany = [attributes: ItemAttribute]
}

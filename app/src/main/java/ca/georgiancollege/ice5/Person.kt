package ca.georgiancollege.ice5

class Person (private var name: String,  private var age: Float) {

    var Name: String
        get() = name
        set(value){
            if(value.isNotBlank()) name = value
        }

    var Age: Float
        get() = age
        set(value) {
            if (value >= 0) age = value
        }

    // overrides
    override fun toString(): String {
        return "Person(name='$name', age=$age)"
    }



}
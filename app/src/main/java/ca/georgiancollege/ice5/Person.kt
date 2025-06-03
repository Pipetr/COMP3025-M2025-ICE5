package ca.georgiancollege.ice5

import android.util.Log

/**
* The person class is the base class for all people
* @param name[String]
* @param age [Float]
*/
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

    fun sayHello(){
        Log.i("Person", "$name says Hello")
    }

    // overrides
    override fun toString(): String {
        return "Person(name='$name', age=$age)"
    }



}
package com.janfranco.tutorialkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Constant Integer
        val myInteger : Int = 15
        println(myInteger)

        // Constant Float
        val myFloat : Float = 3.14f
        println(myFloat)

        // Variable String
        var myName = "Jan"
        myName += " " + "Franco"
        println(myName)

        // Array

        val myArr = arrayOf("Jan", "Franco", "Jane", "Franco")
        println(myArr[0])
        val myIntArr = arrayOf(1, 2, 3, 4, 5)
        val myDoubleArr = doubleArrayOf(3.14, 12.45)
        val myFloatArr = floatArrayOf(12f, 34.123f)

        // ArrayList

        val myArrList = ArrayList<String>()
        val myArrList2 = arrayListOf<String>("Jan", "Franco")
        val myArrList3 = arrayListOf<Any>()
        val myArrList4 = ArrayList<Any>()

        // Dollar Sign

        println("myArrList[0] = ${myArrList[0]}")

        // Set

        var mySet = setOf<Int>(1, 2, 3, 4, 5, 1)
        mySet.forEach { println(it) }

        val myMixedSet = HashSet<Any>()
        myMixedSet.add("Jan Franco")
        myMixedSet.add(123)

        // HashMap

        val fruitCalMap = hashMapOf<String, Int>()
        fruitCalMap.put("Apple", 100)
        fruitCalMap.put("Banana", 150)

        val fruitCalMap2 = HashMap<String, Int>()
        fruitCalMap2.put("Apple", 100)

        // Switch - When

        val day = 3
        var dayString = ""

        when(day) {
            1 -> dayString = "Monday"
            2 -> dayString = "Tuesday"
            3 -> dayString = "Wednesday"
            else -> dayString = "Nevermind"
        }

        println(dayString)

        // For loop

        val myArrOfNumbers = arrayOf(112, 24, 54, 12, 98, 34)

        for (num in myArrOfNumbers)
            println(num)

        for (i in myArrOfNumbers.indices)
            println(myArrOfNumbers[i])

        for (i in 0..9)
            println(i)

        myArrOfNumbers.forEach { println(it) }

        // Functions

        fun myFunc(a: Int, b: Int) : Int {
            return a * b
        }

        var res = myFunc(12, 23)
        println(res)

        // Classes

        class Person(var name: String, var age: Int, var job: String) {

        }

        val person = Person("Jan Franco", 21, "Student")
        println(person.name)

        class PersonWithConst {
            var name = ""
            var age = 0
            var job = ""

            constructor(name: String, age: Int, job: String) {
                this.name = name
                this.age = age
                this.job = job
            }

            override fun toString() : String{
                return this.name + " " + this.age + " " + this.job
            }

        }

        val person2 = PersonWithConst("Jane Franco", 21, "Student")
        println(person2.toString())

        class PersonWithAccessorModifiers {

            private var name = ""
            private var age = 0
            private var job = ""

            constructor(name : String, age : Int, job : String) {
                this.name = name
                this.age = age
                this.job = job
            }

            fun setName(name: String) {
                this.name = name
            }

            fun getName() : String {
                return this.name
            }

        }

        // Nullable - Non-null

        var myInt : Int? = null
        /*
        println(myInt)
        println(myInt!! * 10)
         */

        // Safety
        // Option1: Null safety

        if (myInt != null) {
            println(myInt * 10)
        } else {
            println("myInt is null")
        }

        // Option2: Safe call

        println(myInt?.compareTo(2))

        // Option3: Elvis

        val myRes = myInt?.compareTo(2) ?: -100
        println(myRes)

    }

}

package com.dog.translator.talkingdog.prank.simulator.training

import com.dog.translator.talkingdog.prank.simulator.R
import java.io.Serializable

class TrainingModel : Serializable {
    var img: Int
    var title: String
    var content: String? = null
    var idSound = 0
    var imageDetail : Int = R.drawable.img_food

    constructor(img: Int, title: String, content: String?,imageDetail : Int) {
        this.img = img
        this.title = title
        this.content = content
        this.imageDetail = imageDetail
    }

    constructor(img: Int, title: String, idSound: Int) {
        this.img = img
        this.title = title
        this.idSound = idSound
    }
}
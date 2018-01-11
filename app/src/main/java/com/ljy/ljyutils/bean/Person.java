package com.ljy.ljyutils.bean;

import com.ljy.ljyutils.interfac.CatPet;

import javax.inject.Inject;

/**
 * Created by Mr.LJY on 2018/1/11.
 */

public class Person {
    //    private Dog mDog;
    private Pet mPet;

//    @Inject
//    public Person(Dog dog){
//        mDog=dog;
//    }

//    public void setDogName(String dogName){
//        mDog.setName(dogName);
//    }
//
//    public String callDog(){
//       return mDog.watchHome();
//    }

//    @Inject
//    public Person(@Named("sheep") Pet pet){
//        mPet=pet;
//    }

    @Inject
    public Person(@CatPet Pet pet) {
        mPet = pet;
    }

    public String petSay() {
        return mPet.say();
    }
}

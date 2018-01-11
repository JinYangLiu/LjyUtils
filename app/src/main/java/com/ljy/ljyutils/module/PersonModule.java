package com.ljy.ljyutils.module;

import com.ljy.ljyutils.bean.Person;
import com.ljy.ljyutils.bean.Pet;
import com.ljy.ljyutils.interfac.CatPet;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mr.LJY on 2018/1/11.
 */

@Module
public class PersonModule {
    @Provides
    Person providePerson(@CatPet Pet pet){
        return new Person(pet);
    }
}

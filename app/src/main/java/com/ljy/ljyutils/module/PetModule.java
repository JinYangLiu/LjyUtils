package com.ljy.ljyutils.module;

import com.ljy.ljyutils.bean.Cat;
import com.ljy.ljyutils.bean.Pet;
import com.ljy.ljyutils.bean.Sheep;
import com.ljy.ljyutils.interfac.CatPet;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mr.LJY on 2018/1/11.
 */

@Module
public class PetModule {

    @CatPet
    @Provides
    Pet provideCat(){
        return new Cat();
    }
//
    @Provides
    @Named("sheep")
    Pet provideSheep(){
        return new Sheep();
    }
}

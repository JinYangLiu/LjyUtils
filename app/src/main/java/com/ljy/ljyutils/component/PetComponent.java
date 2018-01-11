package com.ljy.ljyutils.component;


import com.ljy.ljyutils.bean.Pet;
import com.ljy.ljyutils.interfac.CatPet;
import com.ljy.ljyutils.module.PetModule;

import javax.inject.Named;

import dagger.Component;

/**
 * Created by Mr.LJY on 2018/1/11.
 */

@Component(modules = PetModule.class)
public interface PetComponent {
    @CatPet
    Pet getCatPet();

    @Named("sheep")
    Pet getSheepPet();
}

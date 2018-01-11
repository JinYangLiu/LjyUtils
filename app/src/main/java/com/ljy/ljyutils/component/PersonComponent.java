package com.ljy.ljyutils.component;

import com.ljy.ljyutils.bean.Person;
import com.ljy.ljyutils.module.PersonModule;

import dagger.Component;

/**
 * Created by Mr.LJY on 2018/1/11.
 */

@Component(modules = PersonModule.class,dependencies =PetComponent.class )
public interface PersonComponent  {
    Person getPerson();
}

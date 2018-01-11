package com.ljy.ljyutils.component;

import com.ljy.ljyutils.activity.DemoDa2Activity;

import dagger.Component;

/**
 * Created by Mr.LJY on 2018/1/11.
 */

//@Component
//@Component(modules = AnimalModule.class)
@Component(dependencies = PersonComponent.class)
public interface DemoDa2ActivityComponent {
    void inject(DemoDa2Activity activity);
}

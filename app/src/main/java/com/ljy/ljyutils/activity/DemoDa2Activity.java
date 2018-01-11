package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.bean.Person;
import com.ljy.ljyutils.component.DaggerDemoDa2ActivityComponent;
import com.ljy.ljyutils.component.DaggerPersonComponent;
import com.ljy.ljyutils.component.DaggerPetComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoDa2Activity extends AppCompatActivity {

    @BindView(R.id.tv1)
    TextView mTextView1;

    @Inject
    Person mPerson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demoda2);
        ButterKnife.bind(this);
        //使用普通的初始化
//        normalInit();
        //使用dagger2初始化
//        dagger2Init();
        //使用dagger2的@Module和@Provides，@Qualifier和@Named
//        dagger2Init2();
        //@Component的dependence
        dagger2Init3();
    }

    private void dagger2Init3() {
        DaggerDemoDa2ActivityComponent.builder()
                .personComponent(DaggerPersonComponent.builder().petComponent(DaggerPetComponent.create()).build())
                .build().inject(this);
        mTextView1.setText(mPerson.petSay());
    }

//    private void dagger2Init2() {
//        DaggerDemoDa2ActivityComponent.create().inject(this);
//        mTextView1.setText(mPerson.petSay());
//    }

//    private void dagger2Init() {
//        DaggerDemoDa2ActivityComponent.create().inject(this);
//        mPerson.setDogName("秋田");
//        mTextView1.setText(mPerson.callDog());
//    }
//
//    private void normalInit() {
//        Dog dog=new Dog();
//        Person person=new Person(dog);
//        person.setDogName("哈士奇");
//        mTextView1.setText(person.callDog());
//    }
}

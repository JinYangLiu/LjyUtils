package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.ljyutils.bean.Phone;
import com.ljy.ljyutils.bean.PhoneDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 数据库ORM框架GreenDao 3的使用
 *
 * 注意：数据库的初始化放到了application中
 */
public class GreenDaoActivity extends BaseActivity {

    @BindView(R.id.text_db)
    TextView mTextView;
    private PhoneDao phoneDao;

    private int lastCount=0;
    private String LAST_PHONE_COUNT="LAST_PHONE_COUNT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_dao);
        ButterKnife.bind(mActivity);

        phoneDao=getDaoInstance().getPhoneDao();
        lastCount= getSpUtilInstance().get(LAST_PHONE_COUNT,0);
    }




    public void onGreenDaoBtnClick(View view) {
        switch (view.getId()){
            case R.id.btn_insert:
                insertPhone();
                break;
            case R.id.btn_del:
                deletePhone();
                break;
            case R.id.btn_updata:
                updata();
                break;
            case R.id.btn_queryAll:
                queryAllPhone();
                break;
        }
    }

    private void updata() {
        List<Phone> phoneList= phoneDao.loadAll();
        Phone phone=phoneList.get(phoneList.size()-1);
        phone.setName("name_updata___"+lastCount);
        phone.setPrice(lastCount*1000);
        phone.setInfo("info_updata___"+lastCount);
        phoneDao.update(phone);
    }

    private void deletePhone() {
        List<Phone> phoneList= phoneDao.loadAll();
        Phone phone=phoneList.get(phoneList.size()-1);
        phoneDao.deleteByKey(phone.getId());
        getSpUtilInstance().save(LAST_PHONE_COUNT,lastCount);
    }

    private void insertPhone() {
        ++lastCount;
        Phone phone=new Phone();
        phone.setId(lastCount+1000);
        phone.setName("name_"+lastCount);
        phone.setPrice(lastCount*100);
        phone.setInfo("info_"+lastCount);
        phoneDao.insertOrReplace(phone);
        getSpUtilInstance().save(LAST_PHONE_COUNT,lastCount);
    }

    private void queryAllPhone() {
        List<Phone> phoneList= phoneDao.loadAll();
        StringBuilder stringBuilder=new StringBuilder();
        for (Phone phone:phoneList){
            stringBuilder.append(phone.toString());
            stringBuilder.append("\n\n");
        }
        mTextView.setText(stringBuilder.toString());
    }
}

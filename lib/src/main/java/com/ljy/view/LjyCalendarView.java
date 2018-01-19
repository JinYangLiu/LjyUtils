package com.ljy.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ljy.bean.CalendarBean;
import com.ljy.lib.R;
import com.ljy.util.LjyTimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Mr.LJY on 2018/1/19.
 */

public class LjyCalendarView extends RelativeLayout {
    private final TextView textTitle;
    private final ImageView btnLast;
    private final ImageView btnNext;
    private final GridView gridWeek;
    private final GridView gridMonth;
    private final int currentYearRaw;
    private final int currentMonthRaw;
    private int currentYear;
    private int currentMonth;
    private final int currentDayRaw;
    private final DaysGirdViewAdapter dayAdapter;
    //周title
    private String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};

    public LjyCalendarView(Context context) {
        this(context, null);
    }

    @SuppressLint("WrongConstant")
    public LjyCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_calendar, this, true);
        textTitle = rootView.findViewById(R.id.text_title);
        btnLast = rootView.findViewById(R.id.btn_last);
        btnNext = rootView.findViewById(R.id.btn_next);
        gridWeek = rootView.findViewById(R.id.grid_week);
        gridMonth = rootView.findViewById(R.id.grid_month);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        currentYearRaw = cal.get(Calendar.YEAR);
        currentMonthRaw = cal.get(Calendar.MONTH) + 1;
        currentDayRaw = cal.get(Calendar.DATE);
        currentYear = currentYearRaw;
        currentMonth = currentMonthRaw;
        textTitle.setText(currentYear + "年" + currentMonth + "月");

        btnLast.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMonth(false);
            }
        });

        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMonth(true);
            }
        });

        gridWeek.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridWeek.setAdapter(new WeekGirdViewAdapter(context, weeks));

        gridMonth.setSelector(new ColorDrawable(Color.TRANSPARENT));
        List<CalendarBean> list = LjyTimeUtil.getDaysListOfMonth();
        List<CalendarBean> listAll = initList(list);
        dayAdapter = new DaysGirdViewAdapter(context, listAll);
        gridMonth.setAdapter(dayAdapter);
    }

    private void changeMonth(boolean toNext) {
        int temp = toNext ? 1 : -1;
        currentMonth += temp;
        if (currentMonth == 0) {
            currentMonth = 12;
            currentYear -= 1;
        } else if (currentMonth > 12) {
            currentMonth = 1;
            currentYear += 1;
        }
        textTitle.setText(currentYear + "年" + currentMonth + "月");
        List newList = initList(LjyTimeUtil.getDaysListOfMonth(currentYear, currentMonth));
        dayAdapter.setNewList(newList);
    }

    @SuppressLint("WrongConstant")
    @NonNull
    private List<CalendarBean> initList(List<CalendarBean> list) {

        for (CalendarBean bean : list) {
            if (currentDayRaw == bean.getDay()&&currentMonthRaw==bean.getMonth()&&currentYearRaw==bean.getYear()) {
                bean.setToday(true);
                break;
            }
        }
        CalendarBean firstDay = list.get(0);
        int firstWeek = LjyTimeUtil.getWeekDayOnCertainDate(firstDay.getYear(), firstDay.getMonth(), firstDay.getDay());
        List<CalendarBean> listAll = new ArrayList<>();
        for (int i = 1; i < firstWeek; i++) {
            CalendarBean bean = new CalendarBean();
            listAll.add(bean);
        }
        listAll.addAll(list);
        return listAll;
    }

    class WeekGirdViewAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        public String[] mlist;

        public WeekGirdViewAdapter(Context context, String[] list) {
            this.mlist = list;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mlist.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (layoutInflater != null) {
                view = layoutInflater.inflate(R.layout.bbs_calendar_item, null);
                TextView textView = view.findViewById(R.id.textview_btm);
                textView.setText(mlist[position]);
                textView.setTextColor(0xffffffff);
            }
            return view;
        }

    }


    public class DaysGirdViewAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        public List<CalendarBean> mlist;

        public DaysGirdViewAdapter(Context context, List<CalendarBean> list) {
            this.mlist = list;
            layoutInflater = LayoutInflater.from(context);
        }

        public void setNewList(List<CalendarBean> list) {
            mlist = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (layoutInflater != null) {
                view = layoutInflater.inflate(R.layout.bbs_calendar_item, null);
                TextView textView = view.findViewById(R.id.textview_btm);
                View view_bg = view.findViewById(R.id.view_bg);
                if (mlist.get(position).getDay() > 0)
                    textView.setText("" + mlist.get(position).getDay());
                textView.setGravity(Gravity.CENTER);
                if (mlist.get(position).isToday()) {
                    textView.setTextColor(0xffffffff);
                    view_bg.setBackgroundResource(R.drawable.bg_days_circle);
                } else {
                    textView.setTextColor(0xff333333);
                    if (mlist.get(position).getDay() > 0)
                        view_bg.setBackgroundResource(R.drawable.gridview_day_background);
                    else
                        view_bg.setBackgroundColor(0xffffffff);
                }
                if (mlist.get(position).isSign()) {
                    textView.setBackgroundResource(R.drawable.gridview_day_signed);
                }
            }
            return view;
        }

    }
}

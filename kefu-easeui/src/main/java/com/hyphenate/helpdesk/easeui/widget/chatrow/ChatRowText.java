package com.hyphenate.helpdesk.easeui.widget.chatrow;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.helpdesk.R;
import com.hyphenate.chat.Message;
import com.hyphenate.helpdesk.easeui.UIProvider;
import com.hyphenate.helpdesk.easeui.adapter.MessageAdapter;
import com.hyphenate.helpdesk.easeui.kefu.Constant;
import com.hyphenate.helpdesk.easeui.util.SmileUtils;

import java.util.LinkedList;

public class ChatRowText extends ChatRow {

    private TextView contentView;

    public ChatRowText(Context context, Message message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct() == Message.Direct.RECEIVE ?
                R.layout.hd_row_received_message : R.layout.hd_row_sent_message, this);
    }

    @Override
    protected void onFindViewById() {
        contentView = (TextView) findViewById(R.id.tv_chatcontent);
    }

    @Override
    public void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.body();

        //解析html超链接
        CharSequence htmpTxt = Html.fromHtml(txtBody.getMessage().replace("<", "&lt;"));
        //解析表情
        Spannable span = SmileUtils.getSmiledText(context, htmpTxt);

//        //给超链接添加响应
//        URLSpan[] urlSpans = span.getSpans(0, htmpTxt.length(), URLSpan.class);
//        for (URLSpan span1 : urlSpans) {
//            int start = span.getSpanStart(span1);
//            int end = span.getSpanEnd(span1);
//            int flag = span.getSpanFlags(span1);
//            final String link = span1.getURL();
//            span.setSpan(new ClickableSpan() {
//                @Override
//                public void onClick(View widget) {
//                    //打开超链接
//                    if (link != null && link.startsWith("http")) {
//                        if (Constant.getInstance().getOnUrlLinkClickListener() != null) {
//                            Constant.getInstance().getOnUrlLinkClickListener().onClick(context,link);
//                            Log.i("ljy","link_OnUrlLinkClickListener");
//                        } else {
//                            Log.i("ljy","link_action.VIEW");
//                            Intent intent = new Intent();
//                            intent.setAction("android.intent.action.VIEW");
//                            Uri content_url = Uri.parse(link);
//                            intent.setData(content_url);
//                            getContext().startActivity(intent);
//                        }
//                    }
//                }
//            }, start, end, flag);
//            span.removeSpan(span1);
//        }

        contentView.setLinksClickable(true);
        contentView.setMovementMethod(LinkMovementMethod.getInstance());
        // 设置内容
        contentView.setText(span, TextView.BufferType.SPANNABLE);
        SetLinkClickIntercept(contentView);
        handleTextMessage();
    }

    /**
     * textView中超链接的拦截
     *
     * @param tv
     */
    private void SetLinkClickIntercept(TextView tv) {
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = tv.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) tv.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            Log.i("ljy", "urls.length:" + urls.length);
            if (urls.length == 0) {
                return;
            }

            SpannableStringBuilder spannable = new SpannableStringBuilder(text);
            // 只拦截 http:// URI
            LinkedList<String> myurls = new LinkedList<>();
            for (URLSpan uri : urls) {
                String uriString = uri.getURL();
                if (uriString.indexOf("http://") == 0||uriString.indexOf("https://") == 0) {
                    myurls.add(uriString);
                }
            }
            //循环把链接发过去
            for (URLSpan uri : urls) {
                String uriString = uri.getURL();
                if (uriString.indexOf("http://") == 0||uriString.indexOf("https://") == 0) {
                    MyURLSpan myURLSpan = new MyURLSpan(uriString, myurls);
                    spannable.setSpan(myURLSpan, sp.getSpanStart(uri),
                            sp.getSpanEnd(uri), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }
            tv.setText(spannable);
        }
    }

    /**
     * 处理TextView中的链接点击事件
     * 链接的类型包括：url，号码，email，地图
     * 这里只拦截url，即 http:// 开头的URI
     */
    private class MyURLSpan extends ClickableSpan {
        private String mUrl;                    // 当前点击的实际链接
        private LinkedList<String> mUrls; // 根据需求，一个TextView中存在多个link的话，这个和我求有关，可已删除掉

        // 无论点击哪个都必须知道该TextView中的所有link，因此添加改变量
        MyURLSpan(String url, LinkedList<String> urls) {
            mUrl = url;
            mUrls = urls;
        }

        @Override
        public void onClick(View widget) {
            // 这里你可以做任何你想要的处理
            // 比如在你自己的应用中用webview打开，而不是打开系统的浏览器
            String info = new String();
            if (mUrls.size() == 1) {
                // 只有一个url，根据策略弹出提示对话框
                info = mUrls.get(0);
                toWeb(info);
            } else {
                // 多个url，弹出选择对话框，意思一下
//                info  info = mUrls.get(0);// + "\n" + mUrls.get(1);
                MyPopupWindow popupWindow = new MyPopupWindow(context);
                popupWindow.showPopupWindowForFoot(message.direct() == Message.Direct.RECEIVE ?
                        R.layout.hd_row_received_message : R.layout.hd_row_sent_message,mUrls, new MyPopupWindow.Callback() {

                    @Override
                    public void callback(String text, int position) {
                        toWeb(text);
                    }

                });
            }

        }
    }

    private void toWeb(String link) {
        //打开超链接
        if (link != null && link.startsWith("http")) {
            if (Constant.getInstance().getOnUrlLinkClickListener() != null) {
                Constant.getInstance().getOnUrlLinkClickListener().onClick(context,link);
                Log.i("ljy","link_OnUrlLinkClickListener");
            } else {
                Log.i("ljy","link_action.VIEW");
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(link);
                intent.setData(content_url);
                getContext().startActivity(intent);
            }
        }
    }

    protected void handleTextMessage() {
        boolean isShowProgress = UIProvider.getInstance().isShowProgress();
        if (message.direct() == Message.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status()) {
                case CREATE:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    // 发送消息
                    break;
                case SUCCESS: // 发送成功
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    break;
                case FAIL: // 发送失败
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case INPROGRESS: // 发送中
                    if (isShowProgress)
                        progressBar.setVisibility(View.VISIBLE);
                    statusView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onUpdateView() {
        if (adapter instanceof MessageAdapter) {
            ((MessageAdapter) adapter).refresh();
        } else {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onBubbleClick() {
        // TODO Auto-generated method stub

    }

    public static class MyPopupWindow implements OnClickListener{

        private static PopupWindow mPopupWindow;
        private static ListView mListViwe;
        private static String mItemStr;
        private Context mContext;
        private LinearLayout mPopupView;

        public MyPopupWindow(Context context) {
            this.mContext = context;
        }

        public String showPopupWindowForFoot(int res, final LinkedList<String> arrays, final Callback callback) {
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.item_popupwindow, null);

            mPopupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, true);

            contentView.findViewById(R.id.pw_rootView).setOnClickListener(this);;
            contentView.findViewById(R.id.item_popupwindows_cancel).setOnClickListener(this);;

            mPopupView = (LinearLayout) contentView.findViewById(R.id.ll_popup);

            class MyAdapter extends BaseAdapter{
                @Override
                public int getCount() {
                    return arrays.size();
                }
                @Override
                public Object getItem(int position) {
                    return arrays.get(position);
                }
                @Override
                public long getItemId(int position) {
                    return position;
                }
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    ViewHolder holder;
                    if(convertView == null){
                        holder = new ViewHolder();
                        convertView = View.inflate(mContext, R.layout.item_list_popupwindow, null);
                        holder.btn_item = (Button) convertView.findViewById(R.id.btn_item);
                        convertView.setTag(holder);

                    }else {
                        holder = (ViewHolder) convertView.getTag();
                    }
                    holder.btn_item.setText(arrays.get(position));
                    holder.btn_item.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mItemStr = arrays.get(position);
                            callback.callback(mItemStr,position);
                            mPopupWindow.dismiss();
                        }
                    });
                    return convertView;
                }
                class ViewHolder{
                    Button btn_item;
                }
            }
            mListViwe = (ListView) contentView.findViewById(R.id.listview);
            mListViwe.setAdapter(new MyAdapter());

            View rootview = LayoutInflater.from(mContext).inflate(res, null);
            mPopupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
            return mItemStr;
        }

        @Override
        public void onClick(View v) {
            onDismiss();
//		}
        }

        public void onDismiss(){
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
            }
        }

        public interface Callback{
            public void callback(String text, int position);
        }
    }



}
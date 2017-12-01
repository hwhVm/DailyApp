package beini.com.dailyapp.ui.fragments;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import beini.com.dailyapp.GlobalApplication;
import beini.com.dailyapp.R;
import beini.com.dailyapp.bean.DailyBean;
import beini.com.dailyapp.bean.UserBean;
import beini.com.dailyapp.bind.ContentView;
import beini.com.dailyapp.bind.Event;
import beini.com.dailyapp.bind.ViewInject;
import beini.com.dailyapp.constant.Constants;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.ui.presenter.DailyPresenter;
import beini.com.dailyapp.ui.view.GlobalEditText;
import beini.com.dailyapp.util.BitmapUtil;
import io.objectbox.Box;


/**
 * Create by beini 2017/10/19
 */
@ContentView(R.layout.fragment_daily_edit)
public class DailyEditFragment extends BaseFragment {
    @Inject
    DailyPresenter dailyPresenter;
    @ViewInject(R.id.daily_title)
    GlobalEditText daily_title;
    @ViewInject(R.id.daily_author)
    GlobalEditText daily_author;
    @ViewInject(R.id.daily_content)
    GlobalEditText daily_content;
    @ViewInject(R.id.image_upload)
    ImageView image_upload;

    public void initData() {

    }

    @Override
    public void initView() {
        DailyComponent build = DaggerDailyComponent.builder().dailyModule(new DailyModule()).build();
        build.inject(this);
        //
        DailyBean dailyBean = getArguments().getParcelable(Constants.DAILY_EDIT_DATA);
        daily_title.setText(dailyBean.getTitle());
        daily_author.setText(dailyBean.getAuthor());
        daily_content.setText(dailyBean.getContent());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Event({R.id.btn_commit, R.id.btn_add_image})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                dailyPresenter.insertDaily(returnDailyBean(), this);
                break;
            case R.id.btn_add_image:
                String filePaht = "file:" + Constants.EXTEND_STORAGE_PATH + "a.jpg";
                //"http://imgsrc.baidu.com/imgad/pic/item/0df431adcbef760991443cbb24dda3cc7cd99e43.jpg"
//                Bitmap bitmap = BitmapUtil.getBitmapFromStorage(filePaht, 4);
//                BitmapUtil.getInfoFromImage(filePaht);
//                BLog.e("----------->bitmap.getConfig()=" + bitmap.getConfig() + "   bitmap.getAllocationByteCount()= " + bitmap.getAllocationByteCount());
//                image_upload.setImageBitmap(bitmap);
                Bitmap bitmap = BitmapUtil.getBitmapByUri(getActivity(), Uri.parse(filePaht));
                //
                String strBase64 = BitmapUtil.convertBitmapToBase64(bitmap);
                Bitmap bitmapTemp = BitmapUtil.convertBase64ToBitmap(strBase64);
                image_upload.setImageBitmap(bitmapTemp);
                break;
        }
    }

    public DailyBean returnDailyBean() {
        DailyBean dailyBean = new DailyBean();
        dailyBean.setAuthor(daily_author.getText().toString());
        dailyBean.setDate(new Date().toString());
        dailyBean.setContent(daily_content.getText().toString());
        Box<UserBean> userBeanBox = GlobalApplication.getInstance().getBoxStore().boxFor(UserBean.class);
        List<UserBean> userBeans = userBeanBox.getAll();
        if (userBeans != null && userBeans.size() > 0) {
            dailyBean.setUser_id(userBeans.get(userBeans.size() - 1).getUser_id());
        }
        dailyBean.setTitle(daily_title.getText().toString());
        return dailyBean;
    }

    public void onUIShow(boolean isSuccess) {
        if (isSuccess) {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.daily_add_successed), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.daily_add_failed), Toast.LENGTH_SHORT).show();
        }
    }


}

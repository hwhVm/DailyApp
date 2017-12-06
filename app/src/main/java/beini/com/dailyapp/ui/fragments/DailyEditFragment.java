package beini.com.dailyapp.ui.fragments;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import beini.com.dailyapp.GlobalApplication;
import beini.com.dailyapp.R;
import beini.com.dailyapp.adapter.BaseBean;
import beini.com.dailyapp.adapter.GalleryAdapter;
import beini.com.dailyapp.bean.DailyBean;
import beini.com.dailyapp.bean.UserBean;
import beini.com.dailyapp.bind.ContentView;
import beini.com.dailyapp.bind.Event;
import beini.com.dailyapp.bind.ViewInject;
import beini.com.dailyapp.constant.Constants;
import beini.com.dailyapp.constant.NetConstants;
import beini.com.dailyapp.net.response.FileResponse;
import beini.com.dailyapp.ui.component.DaggerDailyComponent;
import beini.com.dailyapp.ui.component.DailyComponent;
import beini.com.dailyapp.ui.module.DailyModule;
import beini.com.dailyapp.ui.presenter.DailyPresenter;
import beini.com.dailyapp.ui.presenter.FilePresenter;
import beini.com.dailyapp.ui.view.GlobalEditText;
import beini.com.dailyapp.util.ActivityResultListener;
import beini.com.dailyapp.util.BLog;
import beini.com.dailyapp.util.GifSizeFilter;
import beini.com.dailyapp.util.MD5Util;
import beini.com.dailyapp.util.StringUtil;
import io.objectbox.Box;

import static android.app.Activity.RESULT_OK;


/**
 * Create by beini 2017/10/19
 */
@ContentView(R.layout.fragment_daily_edit)
public class DailyEditFragment extends BaseFragment implements ActivityResultListener {
    @Inject
    DailyPresenter dailyPresenter;
    @Inject
    FilePresenter filePresenter;

    @ViewInject(R.id.daily_title)
    GlobalEditText daily_title;
    @ViewInject(R.id.daily_author)
    GlobalEditText daily_author;
    @ViewInject(R.id.daily_content)
    GlobalEditText daily_content;
    @ViewInject(R.id.recycle_gallery)
    RecyclerView recycle_gallery;
    private GalleryAdapter galleryAdapter;
    private List<Bitmap> bitmaps;
    private int REQUEST_GALLERY = 111;

    public void initData() {

    }


    @Override
    public void initView() {
        DailyComponent build = DaggerDailyComponent.builder().dailyModule(new DailyModule()).build();
        build.inject(this);
        if (getArguments() != null) {
            DailyBean dailyBean = getArguments().getParcelable(Constants.DAILY_EDIT_DATA);
            daily_title.setText(dailyBean.getTitle());
            daily_author.setText(dailyBean.getAuthor());
            daily_content.setText(dailyBean.getContent());
        }
        baseActivity.setActivityResultListener(this);

        bitmaps = new ArrayList<>();
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_addx));

        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 3);
        galleryAdapter = new GalleryAdapter(new BaseBean<>(R.layout.item_gallery, bitmaps));
        recycle_gallery.setLayoutManager(linearLayoutManager);
        recycle_gallery.setAdapter(galleryAdapter);
        galleryAdapter.setItemClick(((view, position) -> {
            if (position == (bitmaps.size() - 1)) {//最后一个
                Matisse.from(getActivity())
                        .choose(MimeType.ofAll(), true)//// 选择 mime 的类型
                        .countable(true)
                        .maxSelectable(6 - bitmaps.size() + 1)// 图片选择的最多数量
                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)// 缩略图的比例
                        .imageEngine(new GlideEngine())// 使用的图片加载引擎 GlideEngine
                        .forResult(REQUEST_GALLERY);//// 设置作为标记的请求码}
            }

        }));
        galleryAdapter.setOnItemLongClickListener(((view, position) -> {
            if (bitmaps.size() > 1) {
                bitmaps.remove(position);
                pathsSum.remove(position);
                galleryAdapter.notifyDataSetChanged();
            }
        }));
    }

    @Event({R.id.btn_commit})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                BLog.e("------------->pathsSum.size()=" + pathsSum.size());
                if (pathsSum.size() == 1) {
                    List<File> files = new ArrayList<>();
                    for (int i = 0; i < pathsSum.size(); i++) {
                        File oldFile = new File(pathsSum.get(i));
                        File newFile = new File(NetConstants.DIRECTORY_CACHE + "/" + MD5Util.file2Md5(oldFile) + StringUtil.returnStr(oldFile.getName()));
                        BLog.e("         " + newFile.getName());
                        oldFile.renameTo(newFile);
                        files.add(newFile);
                        newFile.delete();
                    }
                    filePresenter.uploadMultiFile(files, this);
                } else {
                    insertDaily(new FileResponse().setFileId(""));
                }
                break;
        }
    }

    public void insertDaily(FileResponse fileResponse) {
        DailyBean dailyBean = returnDailyBean();
        dailyBean.setPicUrl(fileResponse.getFileId());
        dailyPresenter.insertDaily(dailyBean, this);
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

    private List<String> pathsSum = new ArrayList<>();

    @Override
    public void resultCallback(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
//          List<Uri> mSelected = Matisse.obtainResult(data);
            List<String> paths = Matisse.obtainPathResult(data);
            bitmaps.remove(bitmaps.size() - 1);

            for (int i = 0; i < paths.size(); i++) {
                String path = paths.get(i);
                BLog.e("  path=" + path + "         " + (!pathsSum.contains(path)));
                File file = new File(path);
                BLog.e("-------->file.exists()=" + file.exists());
                if (!pathsSum.contains(path) && file.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    bitmaps.add(bitmap);
                    pathsSum.add(path);
                }
            }
            if (bitmaps.size() < 6) {
                bitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_addx));
            }
            galleryAdapter.notifyDataSetChanged();
        }

    }

}

package ccsskt.bokecc.base.example;

import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bokecc.ccdocview.DocView;
import com.bokecc.ccdocview.CCDocViewManager;
import com.bokecc.ccdocview.DocWebView;
import com.bokecc.sskt.base.CCAtlasClient;
import com.github.rongi.rotate_layout.layout.RotateLayout;

import butterknife.BindView;
import ccsskt.bokecc.base.example.base.BaseActivity;
import ccsskt.bokecc.base.example.util.DensityUtil;


/**
 * @author CC视频
 * @Date: on 2018/6/28.
 * @Email: houbs@bokecc.com
 * 白板
 */
public class WhiteBoardActivity extends BaseActivity {
    @BindView(R.id.id_docppt_display)
    DocWebView idDocpptDisplay;
    @BindView(R.id.id_doc_display)
    DocView idDocDisplay;
    @BindView(R.id.id_lecture_doc_area)
    RelativeLayout idLectureDocArea;
    @BindView(R.id.id_lecture_doc_rotate)
    RotateLayout idLectureDocRotate;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.id_fragment)
    RelativeLayout idFragment;
    @BindView(R.id.id_doc_fullscreen)
    ImageButton idDocFullscreen;
    @BindView(R.id.id_doc_exit_fullscreen)
    ImageButton idDocExitFullscreen;

    //基础SDK对象
    private CCAtlasClient ccAtlasClient;

    //文档插件组件对象
    private CCDocViewManager docViewManager;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_white_board;
    }

    @Override
    protected void onViewCreated() {

        //初始化基础SDK和白板组件
        ccAtlasClient = CCAtlasClient.getInstance();
        docViewManager = CCDocViewManager.getInstance();

        //是不是开始直播
        if (ccAtlasClient.isRoomLive()) {
            tvStart.setVisibility(View.GONE);
        } else {
            tvStart.setVisibility(View.VISIBLE);
        }

        //监听直播状态
        ccAtlasClient.setOnClassStatusListener(onClassStatusListener);

        //1.设置画笔
        idDocDisplay.setTouchInterceptor(false, 0);

        //2.设置文档展示界面
        docViewManager.setDocHistory(idDocDisplay, idDocpptDisplay);

        //3.白板与ppt动画的交换
        idDocpptDisplay.setDocSetVisibility(idDocDisplay);
        idDocDisplay.setDocWebViewSetVisibility(idDocpptDisplay);

        //4.设置白板的宽高
        int width = DensityUtil.getWidth(this);
        int height = width * 9 / 16;
        idDocDisplay.setWhiteboard(width, height);

        //5.设置白板父布局
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(idLectureDocRotate.getLayoutParams());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        layoutParams.width = DensityUtil.getHeight(this);
        int height2 = DensityUtil.getWidth(this);
        layoutParams.height = height2 * 9 / 16;
        idLectureDocRotate.setLayoutParams(layoutParams);

        //全屏白板
        idDocFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docFullScreen();
            }
        });

        //退出全屏
        idDocExitFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitDocFullScreen();
            }
        });
    }


    //全屏白板
    public void docFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        idDocFullscreen.setVisibility(View.GONE);
        idDocExitFullscreen.setVisibility(View.VISIBLE);
        //设置为横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //设置白板的宽高
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(idLectureDocRotate.getLayoutParams());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        layoutParams.width = DensityUtil.getHeight(this);
        layoutParams.height = DensityUtil.getWidth(this);
        idLectureDocRotate.setLayoutParams(layoutParams);
    }
    //退出全屏
    public void exitDocFullScreen() {
        idDocFullscreen.setVisibility(View.VISIBLE);
        idDocExitFullscreen.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //设置白板的宽高
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(idLectureDocRotate.getLayoutParams());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        int width = DensityUtil.getHeight(this);
        layoutParams.height = width * 9 / 16;
        idLectureDocRotate.setLayoutParams(layoutParams);
    }
    //直播状态的监听
    CCAtlasClient.OnClassStatusListener onClassStatusListener = new CCAtlasClient.OnClassStatusListener() {
        @Override
        public void onStart() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvStart.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onStop() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvStart.setVisibility(View.VISIBLE);
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

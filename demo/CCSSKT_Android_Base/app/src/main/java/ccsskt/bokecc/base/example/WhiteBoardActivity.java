package ccsskt.bokecc.base.example;

import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bokecc.ccdocview.DocView;
import com.bokecc.ccdocview.CCDocViewManager;
import com.bokecc.ccdocview.DocWebView;
import com.bokecc.sskt.base.CCAtlasCallBack;
import com.bokecc.sskt.base.CCAtlasClient;
import com.bokecc.sskt.base.doc.RoomDocs;
import com.github.rongi.rotate_layout.layout.RotateLayout;

import butterknife.BindView;
import butterknife.OnClick;
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
    @BindView(R.id.id_lecture_drag_child)
    LinearLayout mDrawLayout;

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
        idDocDisplay.setTouchInterceptor(true, 0);
//        idDocDisplay.setGestureAction(false);

        //2.设置文档展示界面
        docViewManager.setDocHistory(idDocDisplay, idDocpptDisplay);
        //3.白板与ppt动画的交换
        idDocpptDisplay.setDocSetVisibility(idDocDisplay);
        idDocDisplay.setDocWebViewSetVisibility(idDocpptDisplay);

        //4.设置白板的宽高
        ViewGroup.LayoutParams params = idLectureDocArea.getLayoutParams();
        int width = DensityUtil.getWidth(this);
        int height = width * 9 / 16;
        idDocDisplay.setWhiteboard(width, height,true);
//        idDocDisplay.setGestureAction(false);


        params.height = height;
        idLectureDocArea.setLayoutParams(params);

//        idDocDisplay.setGestureAction(true);


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

//        fetchRoomDocs();
    }

    private void fetchRoomDocs(){
        docViewManager.getRoomDocs(null, new CCDocViewManager.AtlasCallBack<RoomDocs>() {
            @Override
            public void onSuccess(RoomDocs roomDocs) {
                Log.i("wdh", "onSuccess: " + roomDocs);
            }

            @Override
            public void onFailure(String err) {

            }
        });
    }

    //全屏白板
    public void docFullScreen() {


        idDocFullscreen.setVisibility(View.GONE);
        idDocExitFullscreen.setVisibility(View.VISIBLE);

        ViewGroup.LayoutParams params = idLectureDocArea.getLayoutParams();
        params.height = DensityUtil.getWidth(this);
        idLectureDocArea.setLayoutParams(params);

        idDocDisplay.rotate(true);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置为横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }

    //退出全屏
    public void exitDocFullScreen() {
        idDocFullscreen.setVisibility(View.VISIBLE);
        idDocExitFullscreen.setVisibility(View.GONE);
        //设置
        idDocDisplay.rotate(false);

        int width = DensityUtil.getRealHeight(this).y;
        int height = width * 9 / 16;
        ViewGroup.LayoutParams params = idLectureDocArea.getLayoutParams();
        params.height = height;
        idLectureDocArea.setLayoutParams(params);


        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    //直播状态的监听
    CCAtlasClient.OnClassStatusListener onClassStatusListener = new CCAtlasClient.OnClassStatusListener() {
        @Override
        public void onStart() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvStart.setVisibility(View.GONE);
                    mDrawLayout.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public void onStop() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvStart.setVisibility(View.VISIBLE);
                    mDrawLayout.setVisibility(View.GONE);
                    idDocDisplay.clearAll();
                }
            });
        }
    };



    @OnClick(R.id.id_lecture_draw_eraser)
    void showEraser() {
        idDocDisplay.setEraser(true);
    }

    @OnClick(R.id.id_lecture_draw_paint)
    void showPaint() {
        idDocDisplay.setEraser(false);
        idDocDisplay.setGestureAction(false);
    }

    //点击撤销画笔按钮
    @OnClick(R.id.id_lecture_draw_undo)
    void doUndo() {
        //撤销所有人的画笔
        idDocDisplay.teacherUndo();
    }

    @OnClick(R.id.id_lecture_draw_gesture)
    void showGesture() {
        idDocDisplay.setGestureAction(true);
    }
    @Override
    public void onBackPressed() {
        if(idDocDisplay != null){
            idDocDisplay.recycle();
        }
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

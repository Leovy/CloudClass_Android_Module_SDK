package ccsskt.bokecc.base.example;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.bokecc.sskt.base.CCAtlasClient;
import com.bokecc.sskt.base.bean.CCUser;
import com.bokecc.sskt.base.bean.ChatMsg;
import com.bokecc.sskt.base.bean.ChatMsgHistory;
import com.bokecc.sskt.base.bean.ChatPublic;
import com.bokecc.sskt.base.util.SoftKeyboardUtil;
import com.example.ccchatlibrary.CCChatCallBack;
import com.example.ccchatlibrary.CCChatManager;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.compress.CompressImage;
import com.jph.takephoto.compress.CompressImageImpl;
import com.jph.takephoto.model.TImage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import ccsskt.bokecc.base.example.adapter.ChatAdapter;
import ccsskt.bokecc.base.example.base.BaseActivity;
import ccsskt.bokecc.base.example.bean.ChatEntity;


/**
 * @author CC视频
 * @Date: on 2018/6/28.
 * @Email: houbs@bokecc.com
 * 聊天
 * 分为：1.文字聊天、2.图片聊天
 * 单个禁言、全体禁言
 */
public class ChatActivity extends BaseActivity {
    @BindView(R.id.id_chat)
    Button idChat;
    @BindView(R.id.id_handup)
    Button idHandup;
    @BindView(R.id.id_lianmaistyle)
    Button idLianmaistyle;
    @BindView(R.id.id_student_bottom_layout)
    RelativeLayout idStudentBottomLayout;
    @BindView(R.id.id_chat_list)
    RecyclerView idChatList;
    @BindView(R.id.id_student_click_dismiss_chat)
    FrameLayout idStudentClickDismissChat;
    @BindView(R.id.id_student_chat_send)
    Button idStudentChatSend;
    @BindView(R.id.id_student_chat_open_img)
    ImageButton idStudentChatOpenImg;
    @BindView(R.id.id_student_chat_input)
    EditText idStudentChatInput;
    @BindView(R.id.id_student_chat_layout)
    RelativeLayout idStudentChatLayout;

    //聊天相关
    private ArrayList<ChatEntity> mChatEntities;
    private ChatAdapter mChatAdapter;
    private boolean isScroll = true;
    private boolean isStateIDLE = true;
    private boolean isClickChat = false;

    // 软键盘监听
    private SoftKeyboardUtil mSoftKeyBoardUtil;
    //相册回调参数
    private static final int REQUEST_SYSTEM_PICTURE = 0;

    private CCChatManager chatManager;
    private CCAtlasClient ccAtlasClient;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void onViewCreated() {

        onSoftInputChange();

        ccAtlasClient = CCAtlasClient.getInstance();
        chatManager = CCChatManager.getInstance();

        //初始化聊天列表
        idChatList.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mChatAdapter = new ChatAdapter(this, CCAtlasClient.TALKER);
        mChatEntities = new ArrayList<>();
        //开启直播后才可以获取历史数据
        chatManager.getChatHistory(chatMsgHistoryCCChatCallBack);

        mChatAdapter.bindDatas(mChatEntities);
        idChatList.setAdapter(mChatAdapter);

        //判断是否是禁言状态
        if (chatManager.isRoomGag() || chatManager.isGag()) {
            idChat.setBackgroundResource(R.drawable.student_mute_selector);
        }
        //消息滑动监听
        idChatList.addOnScrollListener(onScrollListener);
        //socket 监听消息
        chatManager.setOnChatListener(onChatListener);
        //禁言某个用户
        chatManager.setOnGagListener(onGagListener);
    }

    //更新聊天列表
    private void updateChatList(final ChatEntity chatEntity) {
        mChatEntities.add(chatEntity);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mChatAdapter.notifyItemInserted(mChatEntities.size() - 1);
                if (isScroll) {
                    idChatList.smoothScrollToPosition(mChatAdapter.getItemCount() - 1);// 进行定位
                }
            }
        });
    }

    //聊天按钮点击事件
    @OnClick(R.id.id_chat)
    void chat() {
        if (chatManager.isRoomGag() || chatManager.isGag()) {
            showToast("禁言中");
            return;
        }
        idStudentChatLayout.bringToFront();
        isClickChat = true;
        idChatList.setVisibility(View.GONE);
        idStudentClickDismissChat.setVisibility(View.VISIBLE);
        mSoftKeyBoardUtil.showKeyboard(idStudentChatInput);
    }

    //发送聊天按钮点击事件
    @OnClick(R.id.id_student_chat_send)
    void chatSend() {
        String content = idStudentChatInput.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            toastOnUiThread("禁止发送空消息");
            return;
        }
        content = chatManager.transformMsg(content);
        chatManager.sendMsg(content);
        idStudentChatInput.setText("");
        mSoftKeyBoardUtil.hideKeyboard(idStudentChatInput);
    }

    //打开图片点击事件
    @OnClick(R.id.id_student_chat_open_img)
    void openImg() {
        /*
         * 这个地方可以自己实现
         */
        openSystemAlbum();
    }

    /**
     * 开启系统相册
     */
    private void openSystemAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_SYSTEM_PICTURE);
    }

    //打开系统相册回调结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_SYSTEM_PICTURE:
                if (data == null) {
                    showToast("图片加载失败");
                    return;
                }
                Uri imageUri = data.getData();// 获得图片的uri
                if (imageUri == null) {
                    showToast("图片加载失败");
                    return;
                }
                String imgPath = chatManager.getImageAbsolutePath(imageUri);
                if (!TextUtils.isEmpty(imgPath)) {
                    try {
                       compressBitmap(imgPath, chatManager.readPictureDegree(imgPath));
                    } catch (IOException e) {
                        showToast("图片加载失败");
                    }
                } else {
                    showToast("图片加载失败");
                }
                break;
        }
    }
        ArrayList<TImage> images = new ArrayList<>();
    CompressConfig config = new CompressConfig.Builder()
            .enableQualityCompress(false)
            .setMaxSize(5 * 1024)
            .create();

    /**
     * 图片压缩处理
     * @param imgPath
     * @param degree
     */
    public void compressBitmap(String imgPath, int degree) {
        images.clear();
        File file = new File(imgPath);
        TImage image = TImage.of(file.getAbsolutePath(), TImage.FromType.OTHER);
        images.add(image);
        CompressImage compressImage = CompressImageImpl.of(this, config,
                images, new MyCompressListener(degree));
        compressImage.compress();
    }

    private class MyCompressListener implements CompressImage.CompressListener {

        private int degree;

        MyCompressListener(int degree) {
            this.degree = degree;
        }

        @Override
        public void onCompressSuccess(ArrayList<TImage> images) {
            //图片压缩成功
            BufferedOutputStream bos = null;
            try {
                File file = new File(images.get(0).getCompressPath());
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (degree != 0 && bitmap != null) {
                    Matrix m = new Matrix();
                    m.setRotate(degree, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
                    Bitmap temp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                    file.deleteOnExit();
                    bos = new BufferedOutputStream(new FileOutputStream(file));
                    temp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bitmap.recycle();
                }
                chatManager.updatePic1(file);
            } catch (Exception e) {
            } finally {
                images.clear();
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        @Override
        public void onCompressFailed(ArrayList<TImage> images, String msg) {
            //图片压缩失败
//            showToast("图片压缩失败,停止上传");
        }
    }

    //RecyclerView的滑动监听
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                isStateIDLE = true;
                if (!recyclerView.canScrollVertically(1)) {
                    //Log.i(TAG, "onScrollStateChanged: bottom");
                }
                if (!recyclerView.canScrollVertically(-1)) {
                    //Log.i(TAG, "onScrollStateChanged: top");
                }
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                    //Log.i(TAG, "onScrollStateChanged: last visible");
                    if (!isScroll) {
                        isScroll = true;
                    }
                }
            } else {
                isStateIDLE = false;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        }
    };

    //监听聊天历史记录消息
    CCChatCallBack<ChatMsgHistory> chatMsgHistoryCCChatCallBack = new CCChatCallBack<ChatMsgHistory>() {
        @Override
        public void onSuccess(ChatMsgHistory chatMsgHistory) {
            ArrayList<ChatPublic> chatPublics = chatMsgHistory.getChatHistoryData();
            for (int i = 0; i < chatPublics.size(); i++) {
                ChatMsg msg = chatPublics.get(i).getMsg();
                CCUser from = chatPublics.get(i).getFrom();
                final ChatEntity chatEntity = new ChatEntity();
                chatEntity.setType(msg.getType());
                chatEntity.setUserId(from.getUserId());
                chatEntity.setUserName(from.getUserName());
                chatEntity.setMsg(msg.getMsg());
                chatEntity.setTime(msg.getTime());
                chatEntity.setUserRole(from.getUserRole());
                updateChatList(chatEntity);
            }
        }

        @Override
        public void onFailure(String err) {

        }
    };

    //监听聊天消息
    CCChatManager.OnChatListener onChatListener = new CCChatManager.OnChatListener() {
        @Override
        public void onReceived(CCUser from, ChatMsg msg, boolean self) {
            final ChatEntity chatEntity = new ChatEntity();
            chatEntity.setType(msg.getType());
            chatEntity.setUserId(from.getUserId());
            chatEntity.setUserName(from.getUserName());
            chatEntity.setMsg(msg.getMsg());
            chatEntity.setTime(msg.getTime());
            chatEntity.setSelf(self);
            chatEntity.setUserRole(from.getUserRole());
            updateChatList(chatEntity);
        }

        @Override
        public void onError(String err) {

        }
    };
    //禁言用户
    CCChatManager.OnGagListener onGagListener = new CCChatManager.OnGagListener() {
        @Override
        public void onChatGagOne(final String userid, final boolean isAllowChat) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateChatStatus(userid, isAllowChat);
                }
            });
        }

        @Override
        public void onChatGagAll(final boolean isAllowChat) {
            //禁言全部用户

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateChatStatus("", isAllowChat);
                }
            });
        }
    };


    //更新聊天状态
    private void updateChatStatus(String userid, final boolean isAllowChat) {
        if (TextUtils.isEmpty(userid)) {
            Toast.makeText(this, isAllowChat ? "老师关闭全体禁言" : "老师开启全体禁言", Toast.LENGTH_SHORT).show();
            if (!isAllowChat) {
                idChat.setBackgroundResource(R.drawable.student_mute_selector);
            } else {
                if (chatManager.isGag()) {
                    idChat.setBackgroundResource(R.drawable.student_mute_selector);
                } else {
                    idChat.setBackgroundResource(R.drawable.student_chat_selector);
                }
            }
            return;
        }
        if (ccAtlasClient.getUserIdInPusher().equals(userid)) {
            Toast.makeText(this, isAllowChat ? "您被老师关闭禁言" : "您被老师开启禁言", Toast.LENGTH_SHORT).show();
            if (!isAllowChat) {
                idChat.setBackgroundResource(R.drawable.student_mute_selector);
            } else {
                if (chatManager.isRoomGag()) {
                    idChat.setBackgroundResource(R.drawable.student_mute_selector);
                } else {
                    idChat.setBackgroundResource(R.drawable.student_chat_selector);
                }
            }
        }
    }

    //自定义输入的框的监听
    private void onSoftInputChange() {
        idStudentChatLayout.bringToFront();
        mSoftKeyBoardUtil = new SoftKeyboardUtil(this);
        mSoftKeyBoardUtil.observeSoftKeyboard(this, new SoftKeyboardUtil.OnSoftKeyboardChangeListener() {
            @Override
            public void onSoftKeyBoardChange(int softKeybardHeight, boolean isShow, int changeHeight) {
                if (!isClickChat) { // 防止强制横屏引起的问题
                    return;
                }
                if (isShow) {
                    idStudentChatLayout.setVisibility(View.VISIBLE);
                } else {
                    idChatList.setVisibility(View.VISIBLE);
                    idStudentClickDismissChat.setVisibility(View.GONE);
                    idStudentChatLayout.setVisibility(View.GONE);
                }
            }
        });
    }


    //退出时要调用
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        if (chatManager != null) {
            chatManager.cancelChatListener();
        }
    }


    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            //这里加个异常处理 ，经测试可以避免闪退，具体副作用暂时还没有发现，可以作为临时解决方案
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }
}

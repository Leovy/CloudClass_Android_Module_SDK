package ccsskt.bokecc.base.example.bean;

import com.bokecc.sskt.base.SubscribeRemoteStream;
import com.bokecc.sskt.base.renderer.CCSurfaceRenderer;

/**
 * 作者 ${CC视频}.<br/>
 */

public class VideoStreamView {

    private SubscribeRemoteStream mStream;
    private CCSurfaceRenderer mRenderer;

    public SubscribeRemoteStream getStream() {
        return mStream;
    }

    public void setStream(SubscribeRemoteStream stream) {
        mStream = stream;
    }

    public CCSurfaceRenderer getRenderer() {
        return mRenderer;
    }

    public void setRenderer(CCSurfaceRenderer renderer) {
        mRenderer = renderer;
    }

}
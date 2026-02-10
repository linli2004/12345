package top.tangyh.lamp.sdkcore.param;


import top.tangyh.lamp.sdkcore.common.FileResult;

/**
 * 文件下载参数
 * @author 六如
 */
public abstract class DownloadRequest<Req> extends BaseParam<Req, FileResult> implements DownloadAware {

    @Override
    public Class<FileResult> getResponseClass() {
        return FileResult.class;
    }
}

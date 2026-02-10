var MAGIC_EDITOR_CONFIG = {
    title: '《灯灯》 IDE',
    header: {
        skin: true,    // 屏蔽皮肤按钮
        document: false,    // 屏蔽文档按钮
        repo: false,    // 屏蔽gitee和github
        qqGroup: false  // 屏蔽加入QQ群
    },
    // 其它配置参考本页中其它配置项
    request: {
        beforeSend: function (config) {
            // console.log('请求设置', window.location.href);
            config.headers.Authorization = "Bearer " + window.location.href.split('Authorization=')[1];
            return config;
        },
        onError: function (err) {
            // console.log('请求出错');
            return Promise.reject(err)
        }
    },
}
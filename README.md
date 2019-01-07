#hxrouter

此library是针对activity的跳转封装的一个统一路由，使用简单方便：

#配置方式

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
allprojects {

repositories {

	...

	maven { url 'https://jitpack.io' }

}
}

Step 2. Add the dependency

dependencies {

	...

	implementation 'com.github.yinzhengwei:hxrouter:2.5'
}


#使用方式

Step 1

在自定义的application中的onCreate()里初始化：
	
	HxRouter.init(this.applicationContext)

Step 2

定义目标界面的类名路径(以下两种方式都可以)：

	val classPath = "com.example.demo.HxArouterTestActivity"
	//val classPath = HxArouterTestActivity::class.java.name


需要跳转的地方：

方式一（无参数）：

	//无回调
	HxRouter.build(classPath).start() 
	//有回调
	HxRouter.build(classPath).start(mActivity, 1)

方式二（带参）：

	//无回调
	HxRouter.build(classPath).put("key", "yzw").start()
	//有回调
	HxRouter.build(classPath).put("key", "yzw").start(mActivity, 1)

方式三（在跳转时添加拦截器，拦截器里处理完了逻辑需要手动调用完成或取消的方法）：

	HxRouter.build(classPath).start(object :HxInterceptor{
            override fun process(interceptorResult: HxInterceptorResult) {
                //TODO 添加需要处理的逻辑，此处以'是否登陆'举例
                if(isLogin){
                    //如果已经登陆，则在启动跳转
                    interceptorResult.finish()
                }else{
                    //如果在处理完逻辑后想取消跳转，直接调用cancel()
                    interceptorResult.cancel()
                }
            }
        })

#注意

别忘了要跳转的目标activity在manifest.xml里注册！


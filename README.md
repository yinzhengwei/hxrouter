#hxrouter

此library是针对activity的跳转封装的一个统一路由，使用简单方便：

配置方式

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

	implementation 'com.github.yinzhengwei:hxrouter:1.0'
}


使用方式

Step 1

在自定义的application中的onCreate()里初始化：

HxRouter.init(this.applicationContext)


Step 2

定义目标界面的类名路径：

val classPath = "com.example.demo.HxArouterTestActivity"

或者

val classPath = HxArouterTestActivity::class.java.name


需要跳转的地方：

方式一：

HxRouter.build(classPath).start()

或

方式二（put方式表示需要传递的参数；start传两参数表示需要走当前界面的onActivityForResult回调）：

HxRouter.build(classPath).put("key", "yzw").start(this@MainActivity, 1)


注意

别忘了要跳转的目标activity在manifest.xml里注册！


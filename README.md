# NiceRatingBar
作者：KaelLi [https://www.kaelli.com](https://www.kaelli.com)  
----
** NiceRatingBar是一个简单的、比Android原生RatingBar更好用的评分控件。**

## 特点
* 支持点击评分
* 支持手势滑动评分
* 根据UI图来决定星星的样式。
* 可以自由设置星星的宽高和间距。
* 支持0.5（半星）的粒度，分数显示更准确。

> 1.0.0版本目前仅支持通过图片的方式展示星星，后期会加入控件自己绘制星星的能力。

## 演示图
### 通过点击的方式进行评分
<img src="./Image/1.gif"/>

### 通过滑动手势的方式进行评分
<img src="./Image/2.gif"/>


## 使用说明
只需要简单的几步操作，就可以使用NiceRatingBar了。

### 1.添加依赖
首先，在你的项目根build.gradle文件里的allprojects依赖仓库列表里添加jitpack：
```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
}
```
然后在需要的module里，添加本项目的依赖:
```gradle
implementation 'com.github.KaelLi1989:NiceRatingBar:1.0.0'
```

如果是把依赖放在你的基础公共库module里，则需要使用api代替implementation：
```gradle
api 'com.github.KaelLi1989:NiceRatingBar:1.0.0'
```

### 2.布局中声明
在布局文件的相应位置添加NiceRatingBar：
```xml
<com.kaelli.niceratingbar.NiceRatingBar
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

然后在布局里进行相应属性的设置：
```xml
// int型，星星的总数，也表示了最大评分值，默认值为5，不能设置为0或负数
app:nrb_starTotal="10"
// 单颗星星的宽，默认值24px
app:nrb_starImageWidth="16dp"
// 单颗星星的高，默认值24px
app:nrb_starImageHeight="16dp"
// 资源id型，完整的一颗星星的图片资源，必填
app:nrb_starFullResource="@drawable/star_full"
// 资源id型，完整的一颗星星的图片资源，必填
app:nrb_starEmptyResource="@drawable/star_empty"
// 资源id型，半颗星星的图片资源，非必填。不填写的话表面本次使用不支持0.5分的步进，只支持整数步进
app:nrb_starHalfResource="@drawable/star_half"
// float型，初始评分值
app:nrb_rating="1.1"
// 2颗星星之间的间距，默认4px
app:nrb_starImagePadding="2dp"
// 枚举型，只能填Enable或Disable。为Enable时控件会处理手势，改变评分；为Disable则仅显示评分
app:nrb_ratingStatus="Enable"
```

### 3.代码后续逻辑
在Java（Kotlin）里设置相应的评分监听：
```Java
niceRatingBar.setOnRatingChangedListener(new OnRatingChangedListener() {
    @Override
    public void onRatingChanged(float rating) {
        // 在这里，得到的rating值就是用户通过点击/滑动，给的评分了，可以把这个值传递到服务端
    }
});
```

通过调用接口把评分传给服务端成功后，可以设置控件的RatingStatus为Disable，这样被评分的对象接下来就不会被评分了：
```Java
niceRatingBar.setRatingStatus(RatingStatus.Disable);
```
当然如果该对象（如某个商品）的初始评分是通过接口获取的，而且能判断当前用户已经对该对象进行过评分，不能继续评分了，那么可以这样：
```Java
niceRatingBar.setRating(4.7f);
niceRatingBar.setRatingStatus(RatingStatus.Disable);
```

## License
```text
Copyright 2019 KaelLi

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
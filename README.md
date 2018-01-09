# LjyUtils
整理一些常用的工具类和自定义view 

### gradle中使用：
Step 1. Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        compile 'com.github.JinYangLiu:LjyUtils:v1.0.2'
	}
	
### 工具类介绍：（具体使用demo在app模块中都有）

+ LjyLogUtil：用于打印log的辅助工具类

默认定位（可选）,提供i，e，w三种log可用

+ LjySystemUtil：提供系统相关方法

isForeground: 判断当前栈顶的activity\
dp2px: 单位dp转成为px\
px2dp: 单位px转成为dp\
getDPI: 获取手机dpi\
checkSdkVersion: 判断当前手机api系统版本是否>=指定版本\
hasPermission: 判断当前应用是否有指定权限\
requestPermission：动态申请指定权限

+ LjyViewUtil：提供view相关方法

getScreenWidth: 获得屏幕的宽\
getScreenHeight: 获得屏幕的高\
setListViewHeight: 重新计算ListView item的高度\
setViewSize: View的宽高=屏幕的宽高*scaleW/H\
getViewWidth,getViewHeight:得到view的宽高\
touchMove:使view可以拖拽移动

+ LjyStringUtil：字符串操作的工具类

isNumber: 判断字符串是否纯数字\
keepAfterPoint: 使用BigDecimal进行精确运算\
byte2hex: byte[]转Hex(16进制)字符串\
hex2byte: Hex(16进制)字符串 转 byte[]\
byte2base64: byte[]转Base64字符串\
base642byte: Base64字符串 转 byte[]\
getRandomStr: 生成随机数，可以当做动态的密钥

+ LjyTimeUtil：时间&日期相关工具类

timestampToDate: 将时间戳转换为指定格式字符串\
isSameDay: 判断两个时间戳是否同一天

+ LjySPUtil：SharedPreferences工具类

save(保存)，get(获取)，clearAll(清空)，getAll(获取全部)

+ LjyEncryUtil：各种数据加密的工具类

包括: Caesar,AES,DES,RSA,MD5,SHA256/512,QrCode

+ LjyGlideUtil：对图片框架Glide的封装

+ LjyColorUtil：颜色相关工具类

randomColor(生成随机颜色)

+ LjyPhotoUtil：选取图片和拍照的工具类

doCamera（拍照并保存到指定path），doCameraAndCut（拍照后裁剪并保存到指定path），getPicture（从系统相册选取单张图片），
getPictureAndCut（从系统相册选取单张图片后裁剪并保存到指定path），getPictures（使用Matisse选取多张图片）

+ LjyBitmapUtil：bitmap处理的工具类

compressQuality(质量压缩),compressSize(尺寸压缩),compressSample(采样率压缩),
compressHuffman（哈夫曼压缩），compressMix（尺寸、质量，哈夫曼混合使用）\
getRatioSize（计算缩放比），addWatermark（给图片添加水印）


### 自定义View介绍：（具体使用demo在app模块中都有）

+ LjyArgueProgressView：辩论View（支持红/蓝方）

+ LjyVoteCheckBox：多选投票

+ LjyVoteRadioGroup：单选投票

+ LjyGestureLockView：九宫格图案锁

+ LjyPwdInputView：数字密码

+ LjyRadarView：雷达图/蛛网图/能力图

+ LjySwipeRefreshView：刷新和加载更多view，其中如果是recycleView需要使用LjyRecyclerView，配合LjyBaseAdapter效果

+ LjyZoomImageView：支持手势缩放的图片控件

+ LjyMDDialog:MD风格的Dialog

+ LjyTagView：仿Nice的标签View

+ LjyGifImageView：支持GIF的ImageView







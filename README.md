# LjyUtils
整理一些常用的工具类

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
	
### 工具类介绍：

+ LjyLogUtil:

用于打印log的辅助工具类, 默认定位（可选）,提供i，e，w三种log可用

+ LjySystemUtil:

提供系统相关方法\
isForeground: 判断当前栈顶的activity\
dp2px: 单位dp转成为px\
px2dp: 单位px转成为dp\
getDPI: 获取手机dpi

+ LjyViewUtil:

提供view相关方法\
getScreenWidth: 获得屏幕的宽\
getScreenHeight: 获得屏幕的高\
setListViewHeight: 重新计算ListView item的高度\
setViewSize: View的宽高=屏幕的宽高*scaleW/H

+ LjyStringUtil:

字符串操作的工具类\
isNumber: 判断字符串是否纯数字\
keepAfterPoint: 使用BigDecimal进行精确运算\
byte2hex:byte[]转Hex(16进制)字符串\
hex2byte:Hex(16进制)字符串 转 byte[]\
byte2base64:byte[]转Base64字符串\
base642byte:Base64字符串 转 byte[]\
getRandomStr:生成随机数，可以当做动态的密钥 

+ LjyTimeUtil:

时间&日期相关工具类\
timestampToDate: 将时间戳转换为指定格式字符串\
isSameDay: 判断两个时间戳是否同一天

+ LjySPUtil:

SharedPreferences工具类\
save(保存)，get(获取)，clearAll(清空)，getAll(获取全部)

+ LjyEncryUtil:

各种数据加密的工具类,包括:Caesar,AES,DES,RSA,MD5,SHA256/512,QrCode\





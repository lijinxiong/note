#### 百分号编码 ####
维基百科[https://zh.wikipedia.org/wiki/%E7%99%BE%E5%88%86%E5%8F%B7%E7%BC%96%E7%A0%81](https://zh.wikipedia.org/wiki/%E7%99%BE%E5%88%86%E5%8F%B7%E7%BC%96%E7%A0%81)  
在这次之前我并没有接触过百分号编码这个东西，实际上我还以为它是一种乱码，并不知道它是一种编码。/捂脸   
而位于java.net包的这两个类就是分别是编码和解码的。   
#### URLEncode ####
	
	/**
 	* This class is used to encode a string using the format required by
 	* {@code application/x-www-form-urlencoded} MIME content type.
 	*
 	* <p>All characters except letters ('a'..'z', 'A'..'Z') and numbers ('0'..'9')
 	* and characters '.', '-', '*', '_' are converted into their hexadecimal value
 	* prepended by '%'. For example: '#' -> %23. In addition, spaces are
 	* substituted by '+'.
 	*/
 这个类就是用于编码content-type为 application/x-www-form-urlencoded格式的字符串的，除了a-z，A-Z，0-9和. , - *_  其他的字符就会被%十六进制取代，  
 	
 	public static String encode(String s, String charsetName) throws UnsupportedEncodingException
charsetName 可以是utf-8，gbk...等等的字符编码，对应于以后的解码   
#### URLDecoder ####
	
	 /**
     * Decodes the argument which is assumed to be encoded in the {@code
     * x-www-form-urlencoded} MIME content type.
     * <p>
     *'+' will be converted to space, '%' and two following hex digit
     * characters are converted to the equivalent byte value. All other
     * characters are passed through unmodified. For example "A+B+C %24%25" ->
     * "A B C $%".
     *
就是编码的反过程
#### URLUtil.decode（） ####
	
	public static byte[] decode(byte[] url) throws IllegalArgumentException {
        if (url.length == 0) {
            return new byte[0];
        }

        // Create a new byte array with the same length to ensure capacity
        byte[] tempData = new byte[url.length];

        int tempCount = 0;
        for (int i = 0; i < url.length; i++) {
            byte b = url[i];
            if (b == '%') {
                if (url.length - i > 2) {
                    b = (byte) (parseHex(url[i + 1]) * 16
                            + parseHex(url[i + 2]));
                    i += 2;
                } else {
                    throw new IllegalArgumentException("Invalid format");
                }
            }
            tempData[tempCount++] = b;
        }
        byte[] retData = new byte[tempCount];
        System.arraycopy(tempData, 0, retData, 0, tempCount);
        return retData;
    }
这个方法只是将%ab（ab为十六进制）转为十进制，返回对应的数组，变回字符串的时候   

	new String(URLUtil.decode(str1.getBytes()), "对应编码的字符编码")
#### 其他 ####
百分号编码其实在浏览器很容易观察到的，比如你搜索的时候   
	
	https://www.google.com/search?newwindow=1&q=%E7%99%BE%E5%88%86%E5%8F%B7%E7%BC%96%E7%A0%81&oq=%E7%99%BE%E5%88%86%E5%8F%B7%E7%BC%96%E7%A0%81&gs_l=serp.3...2027199.2031529.0.2031714.16.15.0.0.0.0.279.1772.0j6j3.9.0....0...1c.4.64.serp..7.5.1102...0j0i13k1j0i30k1j0i10i30k1.rBduHGyMFxo
以上我是搜索百分号编码，那么浏览器post的时候就会将其中的某些字符进行编码（比如中文），但是有时候我们搜索的时候在浏览器的地址栏里还是能看到中文的，但是你复制下来到文本还是会变成%这种形式，那只是浏览器为了用户的视觉体验，把它在浏览器地址栏编码回中文，实际上URl是不予许出现中文的  

相关参考：
> [https://imququ.com/post/four-ways-to-post-data-in-http.html](https://imququ.com/post/four-ways-to-post-data-in-http.html)  
> [http://cnn237111.blog.51cto.com/2359144/1113546](http://cnn237111.blog.51cto.com/2359144/1113546)  
